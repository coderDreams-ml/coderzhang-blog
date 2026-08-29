<template>
  <div style="height:100vh;display:flex;align-items:center;justify-content:center;background:linear-gradient(135deg,#0b0f14 0%,#1a2332 100%)">
    <el-card style="width:380px;padding:10px">
      <h2 style="text-align:center;margin-bottom:6px">coderzhang.top</h2>
      <p style="text-align:center;color:#999;font-size:13px;margin-bottom:24px">博客后台管理 · 登录</p>
      <el-form @keyup.enter="login">
        <el-form-item>
          <el-input v-model="form.username" placeholder="用户名" size="large">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password>
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="login">登 录</el-button>
      </el-form>
      <p style="color:#bbb;font-size:12px;margin-top:16px;text-align:center">初始账号 admin / admin123，登录后请尽快修改</p>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import http from '../api'

const router = useRouter()
const loading = ref(false)
const form = reactive({ username: 'admin', password: '' })

async function login() {
  if (!form.username || !form.password) return ElMessage.warning('请输入用户名和密码')
  loading.value = true
  try {
    const data = await http.post('/auth/login', form)
    localStorage.setItem('token', data.token)
    localStorage.setItem('nickname', data.nickname || data.username)
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } finally {
    loading.value = false
  }
}
</script>
