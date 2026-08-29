# 安全规范

本项目为公开仓库，所有贡献/提交请遵守以下规则。

## 禁止提交的内容

- ❌ 任何真实密码、token、密钥（数据库密码、JWT_SECRET、GitHub token、SSH 私钥等）
- ❌ 服务器公网/内网 IP（文档中用占位符，如 `<服务器地址>`）
- ❌ 云端控制台凭据、access key（AK/SK）

## 正确的凭据管理方式

| 场景 | 正确做法 |
|---|---|
| 本地开发 | 密码只放在 `dev-backend.bat`（已被 .gitignore 排除） |
| 服务器生产 | 密码只放在服务器 `.env`（不进入 git） |
| 文档示例 | 用 `PUT_YOUR_PASSWORD_HERE` 之类占位符 |

## 提交前自查清单

```bash
# 1. 确认敏感文件未被跟踪
git ls-files | grep -E '\.env$|\.pem$|dev-backend\.bat'

# 2. 搜索本次改动中的敏感内容
git diff HEAD | grep -iE 'password|secret|token|ghp_'

# 3. 提交前看一眼
git status
```

## 凭据泄露了怎么办

1. **立即轮换凭据**（改密码 > 改文件，历史里的旧值会失效）
2. 删除/替换文件中的明文
3. 如泄露的是无法轮换的信息（如私钥），需要重写 git 历史

## 平台侧防护（建议开启）

GitHub 仓库 → Settings → Code security and analysis：
- ✅ Secret scanning（免费，自动检测 push 的 token/密钥）
- ✅ Push protection（push 时拦截疑似密钥）
- ✅ Dependabot alerts（依赖漏洞提醒）
