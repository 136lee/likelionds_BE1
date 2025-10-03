package com.example.likelionds_BE1.domain.post.post.controller;

import com.example.likelionds_BE1.domain.member.member.repository.MemberRepository;
import com.example.likelionds_BE1.domain.post.post.domain.PostComment;
import com.example.likelionds_BE1.domain.post.post.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService commentService;
    private final MemberRepository memberRepository;

    private Long meId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("로그인 사용자 정보 없음"))
                .getId();
    }

    // 댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public String write(@PathVariable Long postId, @RequestParam String content) {
        commentService.write(postId, meId(), content);
        return "redirect:/posts/" + postId;
    }

    // 수정 폼
    @GetMapping("/comments/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        PostComment comment = commentService.get(id);
        model.addAttribute("comment", comment);
        return "post/edit-comment";
    }

    // 수정 처리
    @PostMapping("/comments/{id}/edit")
    public String edit(@PathVariable Long id,
                       @RequestParam String content,
                       RedirectAttributes ra) {
        Long postId = commentService.edit(id, meId(), content);
        ra.addFlashAttribute("msg", "댓글이 수정되었습니다.");
        return "redirect:/posts/" + postId;
    }

    // 삭제
    @PostMapping("/comments/{id}/delete")
    public String delete(@PathVariable Long id) {
        Long postId = commentService.delete(id, meId());
        return "redirect:/posts/" + postId;
    }
}