package com.coderzhang.blog.controller;

import com.coderzhang.blog.common.Result;
import com.coderzhang.blog.entity.SocialLink;
import com.coderzhang.blog.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminProfileController {

    private final ProfileService profileService;

    public AdminProfileController(ProfileService profileService) { this.profileService = profileService; }

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
