# ===== 1) BUILD STAGE =====
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Gradle 캐시 최적화
COPY gradlew ./
COPY gradle gradle/
COPY build.gradle settings.gradle ./

# 권한 부여 및 줄바꿈 수정
RUN chmod +x gradlew && \
    sed -i 's/\r$//' gradlew

# 의존성 다운로드
RUN ./gradlew dependencies --no-daemon || true

# 소스 전체 복사 후 빌드
COPY src src/
RUN ./gradlew clean bootJar --no-daemon

# ===== 2) RUNTIME STAGE =====
FROM eclipse-temurin:21-jre
ENV TZ=Asia/Seoul \
    JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75 -Duser.timezone=Asia/Seoul"
WORKDIR /opt/app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]