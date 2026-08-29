<template>
  <div>
    <h3 style="margin-bottom:20px">欢迎回来，{{ nickname }} 👋</h3>
    <el-row :gutter="20">
      <el-col :span="6" v-for="s in stats" :key="s.label">
        <el-card shadow="hover" style="text-align:center">
          <div style="font-size:30px;font-weight:700;color:#4da3ff">{{ s.value }}</div>
          <div style="color:#999;margin-top:6px;font-size:13px">{{ s.label }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top:20px">
      <template #header>快速入口</template>
      <el-button type="primary" @click="$router.push('/articles/edit')">
        <el-icon style="vertical-align:-2px"><EditPen /></el-icon>&nbsp;写文章
      </el-button>
      <el-button @click="$router.push('/projects')">管理项目作品</el-button>
      <el-button @click="$router.push('/profile')">修改站点信息</el-button>
      <el-button @click="openSite">
        <el-icon style="vertical-align:-2px"><Link /></el-icon>&nbsp;查看前台
      </el-button>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import http from '../api'

const nickname = localStorage.getItem('nickname') || 'admin'
const stats = ref([])

onMounted(async () => {
  try {
    const d = await http.get('/admin/stats')
    stats.value = [
      { label: '文章总数', value: d.articleTotal },
      { label: '已发布', value: d.articlePublished },
      { label: '展示项目', value: d.projectTotal },
      { label: '文章总浏览', value: d.totalViews },
      { label: '累计访客 UV', value: d.uv ?? '--' },
      { label: '站点总访问 PV', value: d.pv ?? '--' }
    ]
  } catch (e) { /* 后端未启动时静默 */ }
})

function openSite() { window.open('/') }
</script>
