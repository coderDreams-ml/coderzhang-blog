<template>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-card>
        <template #header>站点信息（前台「关于我」从这里读取）</template>
        <el-form label-width="90px">
          <el-form-item label="首页副标题"><el-input v-model="form.intro" type="textarea" :rows="3" /></el-form-item>
          <el-form-item label="首页终端"><el-input v-model="form.terminal" type="textarea" :rows="6" placeholder="$ whoami&#10;coderDreams —— 苏科大学生 / 全栈与 AI 学徒" /></el-form-item>
          <el-form-item label="关于我"><el-input v-model="form.about" type="textarea" :rows="5" /></el-form-item>
          <el-form-item label="技能栈"><el-input v-model="form.skills" placeholder="用英文逗号分隔" /></el-form-item>
          <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveProfile">保存设置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card>
        <template #header>社交链接（前台页脚/联系区从这里读取）</template>
        <el-table :data="links" border>
          <el-table-column prop="platform" label="平台" width="100" />
          <el-table-column prop="url" label="链接" show-overflow-tooltip />
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button link type="primary" @click="editLink(row)">编辑</el-button>
              <el-button link type="danger" @click="delLink(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="display:flex;gap:8px;margin-top:14px">
          <el-input v-model="linkForm.platform" placeholder="平台名" style="width:110px" />
          <el-input v-model="linkForm.url" placeholder="https://..." />
          <el-button type="primary" @click="saveLink">添加</el-button>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '../api'

const form = reactive({ intro: '', terminal: '', about: '', skills: '', email: '' })
const links = ref([])
const linkForm = reactive({ id: null, platform: '', url: '' })

async function load() {
  const d = await http.get('/admin/profile')
  Object.assign(form, {
    intro: d.intro || '', terminal: d.terminal || '',
    about: d.about || '', skills: d.skills || '', email: d.email || ''
  })
  links.value = await http.get('/admin/links')
}

async function saveProfile() {
  await http.put('/admin/profile', form)
  ElMessage.success('已保存')
}

async function saveLink() {
  if (!linkForm.platform || !linkForm.url) return ElMessage.warning('平台和链接都要填')
  if (linkForm.id) {
    await http.put('/admin/links/' + linkForm.id, linkForm)
  } else {
    await http.post('/admin/links', linkForm)
  }
  ElMessage.success('已保存')
  linkForm.id = null; linkForm.platform = ''; linkForm.url = ''
  links.value = await http.get('/admin/links')
}

function editLink(row) {
  Object.assign(linkForm, { ...row })
}

async function delLink(row) {
  await ElMessageBox.confirm('确定删除「' + row.platform + '」？', '提示', { type: 'warning' })
  await http.delete('/admin/links/' + row.id)
  links.value = await http.get('/admin/links')
}

onMounted(load)
</script>
