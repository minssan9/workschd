
import path from 'path'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite';
import { VitePWA } from 'vite-plugin-pwa'
import { visualizer } from 'rollup-plugin-visualizer';
import { quasar, transformAssetUrls } from '@quasar/vite-plugin'
import { defineConfig, loadEnv } from 'vite'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue({
      template: { transformAssetUrls }
    }),
    VitePWA({
      srcDir: "/",
      filename: "service-worker.js",
      registerType: 'autoUpdate',
      devOptions: {
        enabled: false,
        type: 'module',
        navigateFallback: 'index.html',
      },
      workbox: {
        globPatterns: ['**/*.{ts,js,css,html,ico,png,svg}'],
        maximumFileSizeToCacheInBytes: 5 * 1024 ** 2,
        disableDevLogs: false,
        sourcemap: true,
      },
      includeAssets: ['logo.svg'],
      manifest: {
        name: 'Hyundai Autoever PHM',
        short_name: 'PHM',
        description: 'Hyundai Autoever PHM',
        theme_color: '#ffffff',
        icons: [
          {
            src: 'icon-192x192.png',
            sizes: '192x192',
            type: 'image/png'
          },
          {
            src: 'icon-512x512.png',
            sizes: '512x512',
            type: 'image/png'
          },
          {
            src: 'icon-512x512.png',
            sizes: '512x512',
            type: 'image/png',
            purpose: 'any maskable'
          }
        ]
      }
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
        target: 'http://localhost:24000',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
    }
    // alias: {
    //   '@': fileURLToPath(new URL('./src', import.meta.url))
    // }
  },
})
