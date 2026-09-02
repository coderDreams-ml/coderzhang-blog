import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  base: '/admin/',
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      // 本地开发：/api 转发到后端 8080
      '/api': {
        // 本地管理后台直连线上 API（修改的是生产数据库，操作前建议先备份）
        target: 'https://coderzhang.top',
        changeOrigin: true
      }
    }
  },
  build: {
    outDir: 'dist'
  }
})
