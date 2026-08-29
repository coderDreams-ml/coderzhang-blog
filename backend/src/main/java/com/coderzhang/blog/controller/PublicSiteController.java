package com.coderzhang.blog.controller;

import com.coderzhang.blog.common.Result;
import com.coderzhang.blog.entity.Project;
import com.coderzhang.blog.entity.SocialLink;
import com.coderzhang.blog.service.ProjectService;
import com.coderzhang.blog.service.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 前台站点数据：项目 / 社交链接 / 关于我 */
@RestController
@RequestMapping("/api/public")
public class PublicSiteController {

    private final ProjectService projectService;
    private final ProfileService profileService;

    public PublicSiteController(ProjectService projectService, ProfileService profileService) {
        this.projectService = projectService;
        this.profileService = profileService;
    }

    @GetMapping("/projects")
    public Result<List<Project>> projects() { return Result.ok(projectService.publicList()); }

    @GetMapping("/profile")
    public Result<Map<String, Object>> profile() {
        Map<String, Object> m = new HashMap<>();
        m.put("settings", profileService.getSettings());
        m.put("links", profileService.listLinks());
        return Result.ok(m);
    }
}
