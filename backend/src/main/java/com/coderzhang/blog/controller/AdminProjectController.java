package com.coderzhang.blog.controller;

import com.coderzhang.blog.common.Result;
import com.coderzhang.blog.entity.Project;
import com.coderzhang.blog.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/projects")
public class AdminProjectController {

    private final ProjectService projectService;

    public AdminProjectController(ProjectService projectService) { this.projectService = projectService; }

    @GetMapping
    public Result<List<Project>> list() { return Result.ok(projectService.adminList()); }

    @PostMapping
    public Result<Project> create(@RequestBody Project project) { return Result.ok(projectService.create(project)); }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Project project) {
        projectService.update(id, project);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return Result.ok();
    }
}
