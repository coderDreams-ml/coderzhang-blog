package com.coderzhang.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coderzhang.blog.entity.VisitLog;
import com.coderzhang.blog.mapper.VisitLogMapper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class VisitService {

    private final VisitLogMapper visitLogMapper;

    public VisitService(VisitLogMapper visitLogMapper) { this.visitLogMapper = visitLogMapper; }

    /** 记录一次访问 */
    public void record(String ip, String path) {
        VisitLog v = new VisitLog();
        v.setIpHash(hash(ip));
        v.setPath(path == null || path.isBlank() ? "/" : path);
        v.setCreatedAt(LocalDateTime.now());
        visitLogMapper.insert(v);
    }

    /** 统计：累计 PV/UV + 今日 PV/UV */
    public Map<String, Object> stats() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        Map<String, Object> m = new HashMap<>();
        m.put("pv", visitLogMapper.selectCount(null));
        m.put("uv", visitLogMapper.selectCount(
                new QueryWrapper<VisitLog>().select("DISTINCT ip_hash")));
        m.put("todayPv", visitLogMapper.selectCount(
                new LambdaQueryWrapper<VisitLog>().ge(VisitLog::getCreatedAt, todayStart)));
        m.put("todayUv", visitLogMapper.selectCount(
                new QueryWrapper<VisitLog>().select("DISTINCT ip_hash")
                        .ge("created_at", todayStart)));
        return m;
    }

    /** IP 哈希：不落库明文 IP */
    private String hash(String ip) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] d = md.digest((ip == null ? "unknown" : ip).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8 && i < d.length; i++) sb.append(String.format("%02x", d[i]));
            return sb.toString();
        } catch (Exception e) {
            return "unknown";
        }
    }
}
