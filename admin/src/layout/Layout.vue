<template>
  <el-container style="height: 100vh">
    <el-aside width="200px" style="background:#1d2129">
      <div style="color:#fff;font-size:16px;font-weight:700;padding:18px 20px;border-bottom:1px solid #333">
        coderzhang.top
        <span style="font-size:12px;color:#999;font-weight:400">后台管理</span>
      </div>
      <el-menu :default-active="$route.path" router background-color="#1d2129" text-color="#b0b3b8" active-text-color="#4da3ff" style="border:none">
        <el-menu-item index="/dashboard"><el-icon><DataLine /></el-icon>仪表盘</el-menu-item>
        <el-menu-item index="/articles"><el-icon><Document /></el-icon>文章管理</el-menu-item>
        <el-menu-item index="/categories"><el-icon><CollectionTag /></el-icon>分类管理</el-menu-item>
        <el-menu-item index="/projects"><el-icon><FolderOpened /></el-icon>项目作品</el-menu-item>
        <el-menu-item index="/profile"><el-icon><Setting /></el-icon>站点设置</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background:#fff;display:flex;align-items:center;justify-content:space-between;box-shadow:0 1px 4px rgba(0,0,0,.08)">
        <span style="font-size:15px;color:#333">{{ $route.meta.title }}</span>
        <el-dropdown @command="onCommand">
          <span style="cursor:pointer;color:#333">
            <el-icon style="vertical-align:-2px"><User /></el-icon>
            {{ nickname }}
            <el-icon style="vertical-align:-2px"><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const nickname = computed(() => localStorage.getItem('nickname') || 'admin')

function onCommand(cmd) {
  if (cmd === 'logout') {
    localStorage.removeItem('token')
    localStorage.removeItem('nickname')
    router.push('/login')
  }
}
</script>
