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
              <el-dropdown-item command="password">
                <el-icon style="vertical-align:-2px"><Key /></el-icon>修改密码
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main><router-view /></el-main>
    </el-container>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="pwdVisible" title="修改密码" width="400px">
      <el-form label-width="80px" @keyup.enter="changePassword">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="至少 6 位" />
        </el-form-item>
        <el-form-item label="确认新密码">
          <el-input v-model="pwdForm.confirm" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdVisible = false">取消</el-button>
        <el-button type="primary" :loading="pwdSaving" @click="changePassword">确认修改</el-button>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import http from '../api'

const router = useRouter()
const nickname = computed(() => localStorage.getItem('nickname') || 'admin')

const pwdVisible = ref(false)
const pwdSaving = ref(false)
const pwdForm = reactive({ oldPassword: '', newPassword: '', confirm: '' })

function onCommand(cmd) {
  if (cmd === 'logout') {
    logout()
  } else if (cmd === 'password') {
    pwdForm.oldPassword = ''
    pwdForm.newPassword = ''
    pwdForm.confirm = ''
    pwdVisible.value = true
  }
}

async function changePassword() {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) return ElMessage.warning('请填写完整')
  if (pwdForm.newPassword.length < 6) return ElMessage.warning('新密码至少 6 位')
  if (pwdForm.newPassword !== pwdForm.confirm) return ElMessage.warning('两次输入的新密码不一致')
  pwdSaving.value = true
  try {
    await http.put('/admin/password', {
      oldPassword: pwdForm.oldPassword,
      newPassword: pwdForm.newPassword
    })
    ElMessage.success('密码已修改，请重新登录')
    pwdVisible.value = false
    logout()
  } finally {
    pwdSaving.value = false
  }
}

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('nickname')
  router.push('/login')
}
</script>
