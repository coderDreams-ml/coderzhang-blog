<template>
  <div>
    <div style="margin-bottom:16px">
      <el-button type="primary" @click="openDialog()">
        <el-icon style="vertical-align:-2px"><Plus /></el-icon>&nbsp;添加项目
      </el-button>
    </div>
    <el-table :data="rows" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="名称" width="180" />
      <el-table-column prop="description" label="描述" min-width="260" show-overflow-tooltip />
      <el-table-column prop="groupName" label="分组" width="110" />
      <el-table-column prop="sort" label="排序" width="70" />
      <el-table-column label="展示" width="80">
        <template #default="{ row }">
          <el-tag :type="row.enabled === 1 ? 'success' : 'info'" size="small">{{ row.enabled === 1 ? '展示中' : '隐藏' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button link type="danger" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑项目' : '添加项目'" width="560px">
      <el-form label-width="80px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="链接"><el-input v-model="form.url" placeholder="https://..." /></el-form-item>
        <el-form-item label="分组"><el-input v-model="form.groupName" placeholder="如：AI 探索 / 效率工具" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
        <el-form-item label="展示">
          <el-switch v-model="form.enabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '../api'

const rows = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const form = reactive({ id: null, name: '', description: '', url: '', groupName: '', sort: 0, enabled: 1 })

async function load() {
  loading.value = true
  try { rows.value = await http.get('/admin/projects') } finally { loading.value = false }
}

function openDialog(row) {
  Object.assign(form, row ? { ...row } : { id: null, name: '', description: '', url: '', groupName: '', sort: 0, enabled: 1 })
  dialogVisible.value = true
}

async function save() {
  if (!form.name) return ElMessage.warning('名称不能为空')
  if (form.id) {
    await http.put('/admin/projects/' + form.id, form)
  } else {
    await http.post('/admin/projects', form)
  }
  ElMessage.success('已保存')
  dialogVisible.value = false
  load()
}

async function del(row) {
  await ElMessageBox.confirm('确定删除「' + row.name + '」？', '提示', { type: 'warning' })
  await http.delete('/admin/projects/' + row.id)
  ElMessage.success('已删除')
  load()
}

onMounted(load)
</script>
