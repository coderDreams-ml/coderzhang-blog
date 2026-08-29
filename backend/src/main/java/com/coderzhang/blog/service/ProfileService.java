package com.coderzhang.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coderzhang.blog.entity.Setting;
import com.coderzhang.blog.entity.SocialLink;
import com.coderzhang.blog.mapper.SettingMapper;
import com.coderzhang.blog.mapper.SocialLinkMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfileService {

    private final SettingMapper settingMapper;
    private final SocialLinkMapper socialLinkMapper;

    public ProfileService(SettingMapper settingMapper, SocialLinkMapper socialLinkMapper) {
        this.settingMapper = settingMapper;
        this.socialLinkMapper = socialLinkMapper;
    }

    /** 读取全部设置，返回 key -> value */
    public Map<String, String> getSettings() {
        Map<String, String> map = new HashMap<>();
        settingMapper.selectList(null).forEach(s -> map.put(s.getKey(), s.getValue()));
        return map;
    }

    /** 批量保存设置（key 已存在则覆盖） */
    public void saveSettings(Map<String, String> settings) {
        settings.forEach((k, v) -> {
            Setting exist = settingMapper.selectOne(new LambdaQueryWrapper<Setting>().eq(Setting::getKey, k));
            if (exist == null) {
                Setting s = new Setting();
                s.setKey(k);
                s.setValue(v);
                settingMapper.insert(s);
            } else {
                exist.setValue(v);
                settingMapper.updateById(exist);
            }
        });
    }

    public List<SocialLink> listLinks() {
        return socialLinkMapper.selectList(new LambdaQueryWrapper<SocialLink>()
                .eq(SocialLink::getEnabled, 1).orderByAsc(SocialLink::getSort));
    }

    public SocialLink createLink(SocialLink link) {
        link.setId(null);
        if (link.getEnabled() == null) link.setEnabled(1);
        if (link.getSort() == null) link.setSort(0);
        socialLinkMapper.insert(link);
        return link;
    }

    public void updateLink(Long id, SocialLink link) {
        link.setId(id);
        socialLinkMapper.updateById(link);
    }

    public void deleteLink(Long id) {
        socialLinkMapper.deleteById(id);
    }
}
