# coderzhang-blog

个人博客系统：**Spring Boot 3 后端 + Vue 3 后台管理 + 静态前台展示**，代码托管于 GitHub，推送即自动部署。

## 架构

```
┌────────────── 浏览器 ──────────────┐
│  coderzhang.top/       前台静态页（fetch /api 渲染）   │
│  coderzhang.top/admin/ Vue3 后台管理（SPA）           │
│  coderzhang.top/blog/  博客列表/详情（API + marked）  │
└──────────────┬────────────────────┘
               ▼
┌────────── Nginx（80/443，已有 HTTPS）─────────┐
│  /            → /var/www/coderzhang.top/      │
│  /admin/      → /var/www/coderzhang.top/admin/│
│  /api/        → 127.0.0.1:8080（后端）        │
└──────────────┬───────────────────────────────┘
               ▼
┌──────── docker compose（服务器）────────┐
│  backend: Spring Boot 3 (8080)         │
│  mysql:   MySQL 8 (127.0.0.1:3306)     │
└─────────────────────────────────────────┘
```

## 目录结构

```
coderzhang-blog/
├── backend/          Spring Boot 3 后端（REST API，JWT 鉴权）
├── admin/            Vue 3 + Element Plus 后台管理
├── front/            前台静态站（主页/博客，fetch API 动态渲染）
├── docker-compose.yml
├── .github/workflows/deploy.yml
└── README.md
```

## 本地开发

### 后端（需要 JDK 17+）

```bash
# 1. 启动 MySQL（用 docker 最省事）
docker compose up -d mysql

# 2. 设置 JAVA_HOME（Windows 可用 IDEA 自带 JBR）
#    set JAVA_HOME=C:\Program Files\...\jbr

# 3. 启动
cd backend
mvn spring-boot:run
```

首次启动自动初始化：管理员 **admin / admin123**（登录后请修改），以及示例分类/文章/项目/社交链接。

### 后台管理

```bash
cd admin
npm install
npm run dev      # http://localhost:5173，/api 已代理到 8080
```

### 前台

front 是纯静态文件，直接打开或由任意静态服务器托管即可；页面数据在运行时从 /api/public 拉取，API 不可用时显示内置的静态兜底内容。

## 服务器部署（一次性准备）

```bash
# 1. 克隆仓库（公开仓库直接 clone；私有仓库用 deploy key）
mkdir -p /opt && cd /opt
git clone https://github.com/coderDreams-ml/coderzhang-blog.git

# 2. 配置环境变量
cd coderzhang-blog
cp .env.example .env
vim .env    # 修改数据库密码与 JWT_SECRET

# 3. 起后端 + MySQL
docker compose up -d --build

# 4. 部署静态文件（或直接让 GitHub Actions 干这件事）
cp -a front/. /var/www/coderzhang.top/
mkdir -p /var/www/coderzhang.top/admin
```

### Nginx 追加配置（现有站点配置中）

```nginx
# 后台管理 SPA
location /admin/ {
    alias /var/www/coderzhang.top/admin/;
    try_files $uri $uri/ /admin/index.html;
}

# 后端 API
location /api/ {
    proxy_pass http://127.0.0.1:8080;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
}
```

```bash
nginx -t && systemctl reload nginx
```

## GitHub Actions 自动部署

仓库 Settings → Secrets and variables → Actions 添加：

| Secret | 值 |
|---|---|
| SERVER_HOST | 服务器公网 IP 或域名 |
| SERVER_SSH_KEY | 服务器 root 私钥（整段粘贴） |

推送 main 分支即自动：构建 admin → 上传静态文件 → 服务器拉代码重建后端容器 → reload nginx。

## 常用 API 一览

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | /api/auth/login | 后台登录，返回 JWT |
| GET/POST/PUT/DELETE | /api/admin/articles | 文章管理（需 token） |
| GET/POST/PUT/DELETE | /api/admin/categories | 分类管理 |
| GET/POST/PUT/DELETE | /api/admin/projects | 项目作品管理 |
| GET/PUT | /api/admin/profile | 站点设置 |
| GET/POST/PUT/DELETE | /api/admin/links | 社交链接 |
| GET | /api/admin/stats | 统计 |
| GET | /api/public/articles | 前台文章分页（仅已发布） |
| GET | /api/public/articles/{id} | 文章详情（浏览量+1） |
| GET | /api/public/projects | 前台项目 |
| GET | /api/public/profile | 前台站点信息 |
