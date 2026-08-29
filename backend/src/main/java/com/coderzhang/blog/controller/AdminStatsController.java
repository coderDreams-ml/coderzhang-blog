package com.coderzhang.blog.controller;

import com.coderzhang.blog.common.Result;
import com.coderzhang.blog.service.StatsService;
import com.coderzhang.blog.service.VisitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final StatsService statsService;
    private final VisitService visitService;

    public AdminStatsController(StatsService statsService, VisitService visitService) {
        this.statsService = statsService;
        this.visitService = visitService;
    }

    @GetMapping
    public Result<Map<String, Object>> stats() {
        Map<String, Object> m = statsService.stats();
        m.putAll(visitService.stats());   // 合并 pv/uv/todayPv/todayUv
        return Result.ok(m);
    }
}
