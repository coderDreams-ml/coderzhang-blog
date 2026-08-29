package com.coderzhang.blog.controller;

import com.coderzhang.blog.common.Result;
import com.coderzhang.blog.dto.ChangePasswordRequest;
import com.coderzhang.blog.entity.SocialLink;
import com.coderzhang.blog.service.AuthService;
import com.coderzhang.blog.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminProfileController {

    private final ProfileService profileService;
    private final AuthService authService;

    public AdminProfileController(ProfileService profileService, AuthService authService) {
        this.profileService = profileService;
        this.authService = authService;
    }

    /** 修改登录密码（需要登录态，uid 由 JWT 拦截器注入） */
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestAttribute("uid") Long uid,
                                       @Valid @RequestBody ChangePasswordRequest req) {
        authService.changePassword(uid, req.getOldPassword(), req.getNewPassword());
        return Result.ok();
    }

    @GetMapping("/profile")
    public Result<Map<String, String>> getProfile() { return Result.ok(profileService.getSettings()); }

    @PutMapping("/profile")
    public Result<Void> saveProfile(@RequestBody Map<String, String> settings) {
        profileService.saveSettings(settings);
        return Result.ok();
    }

    @GetMapping("/links")
    public Result<List<SocialLink>> listLinks() { return Result.ok(profileService.listLinks()); }

    @PostMapping("/links")
    public Result<SocialLink> createLink(@RequestBody SocialLink link) { return Result.ok(profileService.createLink(link)); }

    @PutMapping("/links/{id}")
    public Result<Void> updateLink(@PathVariable Long id, @RequestBody SocialLink link) {
        profileService.updateLink(id, link);
        return Result.ok();
    }

    @DeleteMapping("/links/{id}")
    public Result<Void> deleteLink(@PathVariable Long id) {
        profileService.deleteLink(id);
        return Result.ok();
    }
}
