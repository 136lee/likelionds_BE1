package com.example.likelionds_BE1.domain.post.post.controller;

import com.example.likelionds_BE1.domain.member.member.entity.Member;
import com.example.likelionds_BE1.domain.member.member.repository.MemberRepository;
import com.example.likelionds_BE1.domain.post.post.domain.Post;
import com.example.likelionds_BE1.domain.post.post.service.PostCommentService;
import com.example.likelionds_BE1.domain.post.post.service.PostLikeService;
import com.example.likelionds_BE1.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostLikeService likeService;
    private final MemberRepository memberRepository;
    private final PostCommentService commentService;

    private Long meId() {
        return getCurrentUser().getId();
    }


    // 모든 게시물 목록
    @GetMapping
    public String list(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "post/list"; // src/main/resources/templates/post/list.html
    }

    // 글 작성 페이지
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "post/create"; // src/main/resources/templates/post/create.html
    }

    // 글 작성 처리
    @PostMapping
    public String create(
            Post post,
            @RequestParam(value = "tagNames", required = false) java.util.List<String> tagNames
    ) {
        post.setUser(getCurrentUser());

        Long postId = postService.createPost(post).getId();

        if (tagNames != null) {
            for (String name : tagNames) {
                if (name != null && !name.isBlank()) {
                    postService.addTag(postId, name.trim());
                }
            }
        }
        return "redirect:/posts/" + postId;
    }

    // 게시물 상세 페이지
    @GetMapping("/{id:\\d+}")
    public String detail(@PathVariable Long id, Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.getForPost(id));
        model.addAttribute("memberId", meId());

        Long memberId = meId();
        model.addAttribute("likeCount", likeService.count(id));
        model.addAttribute("liked", likeService.liked(id, memberId));

        boolean isOwner = post.getUser() != null && post.getUser().getId().equals(memberId);
        model.addAttribute("isOwner", isOwner);

        return "post/detail"; // src/main/resources/templates/post/detail.html
    }

    // 글 수정 페이지
    @GetMapping("/{id:\\d+}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Post post = postService.getPost(id);

        boolean isOwner = post.getUser() != null && post.getUser().getId().equals(meId());

        model.addAttribute("post", post);
        model.addAttribute("isOwner", true);
        return "post/edit";
    }

    @InitBinder("post")
    public void initPostBinder(WebDataBinder binder) {
        binder.setDisallowedFields("tags");
    }

    // 글 수정 처리
    @PostMapping("/{id:\\d+}/edit")
    public String edit(@PathVariable Long id,
                       @ModelAttribute("post") Post form,
                       @RequestParam(value = "tags", required = false) List<String> tags) {
        postService.updatePost(id, form);
        postService.replaceTags(id, tags);
        return "redirect:/posts/" + id;
    }

    // 글 삭제
    @PostMapping("/{id:\\d+}/delete")
    public String delete(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    // 로그인한 사용자 가져오기
    private Member getCurrentUser() {
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인 사용자 정보 없음"));
    }

    // 좋아요 기능
    @PostMapping("/{id:\\d+}/like")
    public String toggleLike(@PathVariable Long id, RedirectAttributes ra) {
        Long memberId = meId();
        boolean nowLiked = likeService.toggle(id, memberId);
        return "redirect:/posts/" + id;
    }

    // 태그 추가 기능
    @PostMapping("/{id:\\d+}/tags")
    public String addTag(@PathVariable Long id, @RequestParam String name,
                         RedirectAttributes ra) {
        var post = postService.getPost(id);
        if (!post.getUser().getId().equals(meId())) {
            throw new SecurityException("작성자만 태그를 추가할 수 있습니다.");
        }
        postService.addTag(id, name);
        return "redirect:/posts/" + id;
    }

    // 태그 삭제 기능
    @PostMapping("/{id:\\d+}/tags/{tagId}/delete")
    public String deleteTag(@PathVariable Long id, @PathVariable Long tagId,
                            RedirectAttributes ra) {
        var post = postService.getPost(id);
        if (!post.getUser().getId().equals(meId())) {
            throw new SecurityException("작성자만 태그를 삭제할 수 있습니다.");
        }
        postService.removeTag(id, tagId);
        return "redirect:/posts/" + id;
    }

    // 태그로 글 목록 보기
    @GetMapping("/tags/{name}")
    public String listByTag(@PathVariable String name, Model model) {
        var posts = postService.findByTagName(name);
        model.addAttribute("tagName", name);
        model.addAttribute("posts", posts);
        return "post/taglist";
    }
}
