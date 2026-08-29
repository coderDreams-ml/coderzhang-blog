package com.coderzhang.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coderzhang.blog.common.BizException;
import com.coderzhang.blog.entity.Project;
import com.coderzhang.blog.mapper.ProjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectMapper projectMapper;

    public ProjectService(ProjectMapper projectMapper) { this.projectMapper = projectMapper; }

    /** 后台全部列表 */
    public List<Project> adminList() {
        return projectMapper.selectList(new LambdaQueryWrapper<Project>().orderByAsc(Project::getSort));
    }

    /** 前台启用的项目，按分组排序 */
    public List<Project> publicList() {
        return projectMapper.selectList(new LambdaQueryWrapper<Project>()
                .eq(Project::getEnabled, 1)
                .orderByAsc(Project::getGroupName).orderByAsc(Project::getSort));
    }

    public Project create(Project p) {
        p.setId(null);
        p.setCreatedAt(LocalDateTime.now());
        if (p.getEnabled() == null) p.setEnabled(1);
        if (p.getSort() == null) p.setSort(0);
        projectMapper.insert(p);
        return p;
    }

    public void update(Long id, Project p) {
        Project db = projectMapper.selectById(id);
        if (db == null) throw new BizException("项目不存在");
        p.setId(id);
        projectMapper.updateById(p);
    }

    public void delete(Long id) {
        projectMapper.deleteById(id);
    }
}
