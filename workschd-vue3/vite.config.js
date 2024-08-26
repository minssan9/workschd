import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite';
import { visualizer } from 'rollup-plugin-visualizer';
import { quasar, transformAssetUrls } from '@quasar/vite-plugin'

// https://vitejs.dev/config/
export default defineConfig({

  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  plugins: [
    vue({
      template: { transformAssetUrls }
    }),

    // @quasar/plugin-vite options list:
    // https://github.com/quasarframework/quasar/blob/dev/vite-plugin/index.d.ts
    quasar({
      sassVariables: 'src/assets/sass/quasar-variables.sass'
    }),
    Components({
      resolvers: []
    }),
    visualizer({
      open: true, // Automatically open the browser with the report
      filename: 'dist/bundle-analyzer-report.html', // Output file name
      gzipSize: true, // Show gzip size
      brotliSize: true, // Show brotli size
    })
  ],
  server: {
    port: 3000, // 개발 서버 포트 설정
    open: true, // 브라우저 자동 열기
    host: true, // 네트워크에서 접근 가능하도록 설정
    https: false, // HTTPS 사용 여부
    cors: true, // CORS 활성화
    proxy: {
      // API 프록시 설정
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
