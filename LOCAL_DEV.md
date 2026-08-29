# 本地开发指南

本文档针对 Windows 本机开发（服务器部署见 README）。

## 环境要求（你本机已具备）

| 组件 | 状态 | 说明 |
|---|---|---|
| JDK 17+ | ✅ IDEA 自带 JBR 25 | `C:\Users\admin\AppData\Local\Programs\IntelliJ IDEA\jbr` |
| Maven 3.9 | ✅ 已装 | 建议用仓库内 `mvn-settings.xml`（阿里云镜像 + 工作区本地仓库） |
| Node 20+ | ✅ v24 已装 | |
| MySQL 8 | ✅ 本机 3306 已有 | 直接用本机实例建库即可 |
| IDEA | ✅ 已装 | 记得安装/确认 Lombok 插件 |

## 一、初始化本机数据库（一次性）

用你本机的 MySQL 客户端（Navicat / 命令行）执行：

```sql
CREATE DATABASE IF NOT EXISTS coderzhang_blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'blog'@'localhost' IDENTIFIED BY 'blog123456';
GRANT ALL PRIVILEGES ON coderzhang_blog.* TO 'blog'@'localhost';
FLUSH PRIVILEGES;
```

然后导入建表脚本（命令行的话）：

```bash
mysql -uroot -p coderzhang_blog < backend/src/main/resources/schema.sql
```

> 表结构导入后，**数据不用手动插**——后端启动时会自动初始化管理员账号和示例数据（InitDataRunner）。

## 一点五、可选：直接连服务器开发库（不动本机 MySQL）

服务器上已建好独立的开发库 `coderzhang_blog_dev`（与线上生产库完全隔离，容器 MySQL 只监听服务器回环 3307，不暴露公网）。

**1. 开 SSH 隧道**（单独开一个终端保持运行）：

```powershell
ssh -i "D:\密钥\Ubuntu-lghc.pem" -N -L 13306:127.0.0.1:3307 root@8.133.213.52
```

**2. 后端环境变量改为**（密码见服务器 `/opt/coderzhang-blog/.env` 的 `MYSQL_PASSWORD`）：

```
MYSQL_HOST=127.0.0.1
MYSQL_PORT=13306
MYSQL_DB=coderzhang_blog_dev
MYSQL_USER=blog
MYSQL_PASSWORD=<见 .env>
```

> 好处：本机不用装/配 MySQL；开发数据不会污染线上内容；改坏开发库随时重建（删库后重启后端即可自动初始化）。
> 注意：隧道断开后后端会报数据库连接失败，重新开隧道即可。

## 二、启动后端（两种方式任选）

### 方式 A：IDEA（推荐日常开发）

1. File → Open，选择 `backend/pom.xml`（以项目方式打开）
2. Project Structure → SDK 选择 IDEA 自带的 JBR（或自行安装 JDK 17）
3. 确认插件里 Lombok 已启用
4. 直接运行 `BlogApplication`（端口 8080）
5. 如果本机 MySQL 账号密码与默认不同，在 Run Configuration 的 Environment variables 里加：
   ```
   MYSQL_USER=root;MYSQL_PASSWORD=你的密码
   ```

### 方式 B：命令行

```powershell
# 指向 IDEA 自带 JDK
$env:JAVA_HOME = 'C:\Users\admin\AppData\Local\Programs\IntelliJ IDEA\jbr'
# 本机 MySQL 账号密码
$env:MYSQL_USER = 'root'
$env:MYSQL_PASSWORD = '你的密码'

cd D:\Desktop\agent_util_scripts\coderzhang-blog\backend
mvn -s ..\..\mvn-settings.xml spring-boot:run
```

> 注：如果遇到 `Unknown host maven-central.aliyun.com`，说明当前网络解析不了旧阿里云镜像域名，用仓库外的 `mvn-settings.xml`（指向 maven.aliyun.com）即可。

启动成功标志：日志出现 `Tomcat started on port 8080` 和 `init data check done`。

**默认管理员：admin / admin123**

## 三、启动后台管理界面

```powershell
cd D:\Desktop\agent_util_scripts\coderzhang-blog\admin
npm install        # 首次
npm run dev
```

浏览器打开 **http://localhost:5173**，登录 admin / admin123。开发服务器已把 `/api` 代理到 8080，无需额外配置。

## 四、本地预览前台（front）

front 是静态页面，数据在浏览器端 fetch `/api`。仓库自带零依赖预览服务器：

```powershell
cd D:\Desktop\agent_util_scripts\coderzhang-blog
node scripts/dev-front.js
```

浏览器打开 **http://localhost:8081**（/api 已代理到本地 8080 后端）。

## 五、日常开发循环

```
改后端代码 → IDEA 重启（或 devtools 热部署）→ 后台管理界面直接验证
改 admin 界面 → Vite 热更新自动生效
改 front 静态页 → 刷新 8081 预览 → 满意后走部署流程
```

## 六、常见问题

| 问题 | 解决 |
|---|---|
| 8080 端口被占 | 改 application.yml 的 server.port，并同步 admin/vite.config.js 代理 target |
| Lombok 报红 | IDEA 设置 → Plugins 安装 Lombok；Settings → Build → Compiler → Annotation Processors 勾选 Enable |
| 登录 401 | token 过期，重新登录即可 |
| 数据库连不上 | 检查本机 MySQL 是否启动、账号密码环境变量是否正确 |
