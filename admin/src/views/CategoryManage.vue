<template>
  <div style="max-width:640px">
    <div style="display:flex;gap:10px;margin-bottom:16px">
      <el-input v-model="newName" placeholder="新分类名称" style="width:220px" @keyup.enter="create" />
      <el-button type="primary" @click="create">添加分类</el-button>
    </div>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="sort" label="排序" width="90" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button link type="primary" @click="edit(row)">改名</el-button>
          <el-button link type="danger" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '../api'

const rows = ref([])
const newName = ref('')
const loading = ref(false)

async function load() {
  loading.value = true
  try { rows.value = await http.get('/admin/categories') } finally { loading.value = false }
}

async function create() {
  if (!newName.value.trim()) return ElMessage.warning('请输入名称')
  await http.post('/admin/categories', { name: newName.value.trim() })
  newName.value = ''
  ElMessage.success('已添加')
  load()
}

async function edit(row) {
  const { value } = await ElMessageBox.prompt('新的分类名', '改名', { inputValue: row.name })
  await http.put('/admin/categories/' + row.id, { name: value })
  ElMessage.success('已更新')
  load()
}

async function del(row) {
  await ElMessageBox.confirm('确定删除「' + row.name + '」？', '提示', { type: 'warning' })
  await http.delete('/admin/categories/' + row.id)
  ElMessage.success('已删除')
  load()
}

onMounted(load)
</script>
