<template>
  <el-form label-width="70px" style="max-width:960px">
    <el-form-item label="标题">
      <el-input v-model="form.title" placeholder="文章标题" />
    </el-form-item>
    <el-form-item label="摘要">
      <el-input v-model="form.summary" type="textarea" :rows="2" placeholder="一句话摘要，用于列表展示" />
    </el-form-item>
    <el-form-item label="分类">
      <el-select v-model="form.categoryId" placeholder="选择分类" style="width:200px">
        <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
    </el-form-item>
    <el-form-item label="标签">
      <el-select v-model="form.tags" multiple filterable allow-create default-first-option
        placeholder="输入后回车创建标签" style="width:400px" />
    </el-form-item>
    <el-form-item label="状态">
      <el-radio-group v-model="form.status">
        <el-radio :value="0">草稿</el-radio>
        <el-radio :value="1">发布</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="正文">
      <md-editor v-model="form.content" :height="520" style="width:100%" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      <el-button @click="$router.back()">返回</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import http from '../api'

const route = useRoute()
const router = useRouter()
const saving = ref(false)
const categories = ref([])
const form = reactive({
  title: '', summary: '', content: '', categoryId: null, status: 0, tags: []
})

const id = route.params.id

onMounted(async () => {
  const cats = await http.get('/admin/categories')
  categories.value = cats
  if (id) {
    const a = await http.get('/admin/articles/' + id)
    Object.assign(form, {
      title: a.title, summary: a.summary, content: a.content,
      categoryId: a.categoryId, status: a.status, tags: a.tags || []
    })
  }
})

async function save() {
  if (!form.title) return ElMessage.warning('标题不能为空')
  saving.value = true
  try {
    if (id) {
      await http.put('/admin/articles/' + id, form)
    } else {
      await http.post('/admin/articles', form)
    }
    ElMessage.success('保存成功')
    router.push('/articles')
  } finally {
    saving.value = false
  }
}
</script>
