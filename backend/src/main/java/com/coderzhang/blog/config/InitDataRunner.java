package com.coderzhang.blog.config;

import com.coderzhang.blog.entity.*;
import com.coderzhang.blog.mapper.*;
import com.coderzhang.blog.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/** 首次启动自动初始化：管理员账号 + 示例数据（数据为空时才写入，可放心重复启动） */
@Slf4j
@Component
public class InitDataRunner implements CommandLineRunner {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final ArticleMapper articleMapper;
    private final ProjectMapper projectMapper;
    private final SocialLinkMapper socialLinkMapper;
    private final SettingMapper settingMapper;
    private final AuthService authService;

    public InitDataRunner(UserMapper userMapper, CategoryMapper categoryMapper, ArticleMapper articleMapper,
                          ProjectMapper projectMapper, SocialLinkMapper socialLinkMapper,
                          SettingMapper settingMapper, AuthService authService) {
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
        this.articleMapper = articleMapper;
        this.projectMapper = projectMapper;
        this.socialLinkMapper = socialLinkMapper;
        this.settingMapper = settingMapper;
        this.authService = authService;
    }

    @Override
    public void run(String... args) {
        initUser();
        initCategory();
        initArticle();
        initProject();
        initLinks();
        initSettings();
        log.info("init data check done");
    }

    private void initUser() {
        if (userMapper.selectCount(null) > 0) return;
        User u = new User();
        u.setUsername("admin");
        u.setPassword(authService.encode("admin123"));
        u.setNickname("coderDreams");
        u.setCreatedAt(LocalDateTime.now());
        userMapper.insert(u);
        log.info("init user: admin / admin123 （请尽快在后台修改密码）");
    }

    private void initCategory() {
        if (categoryMapper.selectCount(null) > 0) return;
        for (String name : List.of("AI 探索", "效率工具", "随笔")) {
            Category c = new Category();
            c.setName(name);
            c.setSort(0);
            c.setCreatedAt(LocalDateTime.now());
            categoryMapper.insert(c);
        }
        log.info("init categories");
    }

    private void initArticle() {
        if (articleMapper.selectCount(null) > 0) return;
        Article a1 = new Article();
        a1.setTitle("你好，coderzhang.top —— 博客部署手记");
        a1.setSummary("从一台空白服务器到正式上线的全过程记录。");
        a1.setContent("这是一篇示例文章，记录本站从一台空白服务器到正式上线的过程。\n\n## 服务器与域名\n一台 Ubuntu 24.04 云服务器 + 一个 .top 域名。\n\n## 域名解析\n添加两条 A 记录指向服务器公网 IP，用 nslookup 验证。\n\n## Nginx 与 HTTPS\n安装 Nginx 托管静态页面，certbot 一键签发 HTTPS 证书并自动续期。\n\n网站上线只是开始，接下来把这里替换成你真正的文章吧 🎉");
        a1.setStatus(1);
        a1.setViews(0);
        a1.setCategoryId(3L);
        a1.setCreatedAt(LocalDateTime.now());
        a1.setUpdatedAt(LocalDateTime.now());
        articleMapper.insert(a1);

        Article a2 = new Article();
        a2.setTitle("Nginx 配置速查：从入门到反向代理");
        a2.setSummary("常用配置片段与踩坑笔记。");
        a2.setContent("记录一些常用 Nginx 配置片段。\n\n## 最小静态站\n    server {\n        listen 80;\n        server_name example.com;\n        root /var/www/example.com;\n        index index.html;\n    }\n\n## 反向代理\n    location /api/ {\n        proxy_pass http://127.0.0.1:8080;\n        proxy_set_header Host $host;\n        proxy_set_header X-Real-IP $remote_addr;\n    }\n\n## 检查与重载\n- nginx -t 检查语法\n- systemctl reload nginx 平滑重载");
        a2.setStatus(1);
        a2.setViews(0);
        a2.setCategoryId(3L);
        a2.setCreatedAt(LocalDateTime.now());
        a2.setUpdatedAt(LocalDateTime.now());
        articleMapper.insert(a2);
        log.info("init articles");
    }

    private void initProject() {
        if (projectMapper.selectCount(null) > 0) return;
        List<Project> projects = List.of(
                project("MiniMind 学习", "跟着 MiniMind 从零复现一个小语言模型，拆解 Transformer 的每个零件。当前状态：模型能说话，但说的不一定对。",
                        "https://github.com/coderDreams-ml/minimind-learn", "AI 探索", 1),
                project("PyTorch CIFAR-10 实验", "用 PyTorch 训练图像分类模型，AI 基础课的课后作业，也是我的第一个「炼丹炉」。",
                        "https://gitee.com/coderdreams/little-hill--pytorch-cifar-10", "AI 探索", 2),
                project("校园网自动登录脚本", "苏科大校园网每天掉线重连的痛，只能用脚本治愈。自动检测断网 → 自动登录。",
                        "https://gitee.com/coderdreams/sust-school-net-script", "效率工具", 1),
                project("桌面提醒小工具", "AI 辅助编程写的桌面提醒应用，专治「又忘了交作业」。AI 生成的代码，我负责 debug。",
                        "https://gitee.com/coderdreams/desktop-memo", "效率工具", 2)
        );
        projects.forEach(projectMapper::insert);
        log.info("init projects");
    }

    private Project project(String name, String desc, String url, String group, int sort) {
        Project p = new Project();
        p.setName(name);
        p.setDescription(desc);
        p.setUrl(url);
        p.setGroupName(group);
        p.setSort(sort);
        p.setEnabled(1);
        p.setCreatedAt(LocalDateTime.now());
        return p;
    }

    private void initLinks() {
        if (socialLinkMapper.selectCount(null) > 0) return;
        List.of(
                link("GitHub", "https://github.com/coderDreams-ml", 1),
                link("Gitee", "https://gitee.com/coderdreams/", 2),
                link("掘金", "https://juejin.cn/user/127981962405726", 3),
                link("Bilibili", "https://space.bilibili.com/353529000", 4)
        ).forEach(socialLinkMapper::insert);
        log.info("init social links");
    }

    private SocialLink link(String platform, String url, int sort) {
        SocialLink s = new SocialLink();
        s.setPlatform(platform);
        s.setUrl(url);
        s.setSort(sort);
        s.setEnabled(1);
        return s;
    }

    private void initSettings() {
        if (settingMapper.selectCount(null) > 0) return;
        List.of(
                setting("siteName", "coderzhang.top"),
                setting("intro", "你好，我是 coderDreams。苏州科技大学在读，从 Java / Go 全栈一路写过来，目前正头也不回地扎进 AI 大模型。"),
                setting("about", "从 Java 全栈（Spring Boot + Redis）和 Go 微服务（Kitex）起步；现在的主线是 AI：PyTorch 打基础 → MiniMind 从零复现小语言模型 → RAG 企业知识库实战。信奉「项目驱动学习」，这里的大多数项目都是学习路上的副产品。"),
                setting("skills", "Java / Spring Boot,Python / PyTorch,MySQL / Redis,Docker / Nginx,Linux,Git,AI 辅助编程"),
                setting("email", "hello@coderzhang.top")
        ).forEach(settingMapper::insert);
        log.info("init settings");
    }

    private Setting setting(String k, String v) {
        Setting s = new Setting();
        s.setKey(k);
        s.setValue(v);
        return s;
    }
}
