<template>
  <div>
    <div style="display:flex;justify-content:space-between;margin-bottom:16px;gap:12px;flex-wrap:wrap">
      <div style="display:flex;gap:12px">
        <el-input v-model="query.keyword" placeholder="按标题搜索" style="width:220px" clearable @keyup.enter="load(1)" />
        <el-select v-model="query.status" placeholder="状态" style="width:120px" clearable @change="load(1)">
          <el-option label="已发布" :value="1" />
          <el-option label="草稿" :value="0" />
        </el-select>
        <el-button type="primary" @click="load(1)">搜索</el-button>
      </div>
      <el-button type="primary" @click="$router.push('/articles/edit')">
        <el-icon style="vertical-align:-2px"><Plus /></el-icon>&nbsp;新建文章
      </el-button>
    </div>

    <el-table :data="rows" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="220" show-overflow-tooltip />
      <el-table-column prop="categoryName" label="分类" width="110" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
            {{ row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="views" label="浏览" width="80" />
      <el-table-column prop="updatedAt" label="更新时间" width="170">
        <template #default="{ row }">{{ (row.updatedAt || '').replace('T', ' ').slice(0, 16) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="$router.push('/articles/edit/' + row.id)">编辑</el-button>
          <el-button link type="danger" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination style="margin-top:16px;justify-content:flex-end"
      layout="total, prev, pager, next" :total="total"
      :page-size="query.size" :current-page="query.page" @current-change="load" />
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '../api'

const rows = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, size: 10, keyword: '', status: null })

async function load(page) {
  if (page) query.page = page
  loading.value = true
  try {
    const d = await http.get('/admin/articles', { params: query })
    rows.value = d.list
    total.value = d.total
  } finally {
    loading.value = false
  }
}

async function del(row) {
  await ElMessageBox.confirm('确定删除「' + row.title + '」？', '提示', { type: 'warning' })
  await http.delete('/admin/articles/' + row.id)
  ElMessage.success('已删除')
  load(query.page)
}

onMounted(() => load(1))
</script>
