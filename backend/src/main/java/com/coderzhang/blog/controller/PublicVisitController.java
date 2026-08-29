package com.coderzhang.blog.controller;

import com.coderzhang.blog.common.Result;
import com.coderzhang.blog.service.VisitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 访问统计（前台） */
@RestController
@RequestMapping("/api/public")
public class PublicVisitController {

    private final VisitService visitService;

    public PublicVisitController(VisitService visitService) { this.visitService = visitService; }

    /** 上报一次访问 */
    @PostMapping("/visit")
    public Result<Void> visit(HttpServletRequest request,
                              @RequestBody(required = false) Map<String, String> body) {
        String path = body == null ? "/" : body.getOrDefault("path", "/");
        visitService.record(clientIp(request), path);
        return Result.ok();
    }

    /** 访问统计 */
    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        return Result.ok(visitService.stats());
    }

    private String clientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) return xff.split(",")[0].trim();
        String xr = request.getHeader("X-Real-IP");
        if (xr != null && !xr.isBlank()) return xr.trim();
        return request.getRemoteAddr();
    }
}
