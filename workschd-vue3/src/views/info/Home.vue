<script setup>
import { useI18n } from 'vue-i18n'
import { computed } from 'vue'
import { useCounterStore } from '@/stores/modules/counter.js'

const { t } = useI18n()
const counterStore = useCounterStore()

const count = computed(() => counterStore.count)
const doubleCount = computed(() => counterStore.doubleCount)
const increment = () => counterStore.increment()
</script>

<template>
  <q-page class="home-container">
    <div class="background-pattern absolute-full"></div>

    <!-- Header Section -->
    <div class="row justify-center items-center q-pa-xl bg-primary text-white">
      <div class="col-12 col-md-8 text-center">
        <h1 class="text-h3 q-mb-md">{{ t('home.header.title', '근무 스케줄표 자동 생성 서비스') }}</h1>
        <p class="text-h6 q-mb-lg">{{ t('home.header.subtitle', '효율적인 근무 관리를 위한 스케줄 자동화 솔루션') }}</p>
        <div class="row q-col-gutter-md justify-center">
          <div class="col-auto">
            <q-btn
              color="green"
              :label="t('home.header.button.start', '지금 시작하기')"
              href="https://gamma.app"
              class="q-px-md"
            />
          </div>
          <div class="col-auto">
            <q-btn
              outline
              color="white"
              :label="t('home.header.button.learnMore', '더 알아보기')"
              href="https://gamma.app"
              class="q-px-md"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Content Sections -->
    <div class="q-pa-md">
      <div class="row q-col-gutter-lg">
        <!-- Service Overview -->
        <div class="col-12 col-md-4">
          <q-card class="q-pa-md">
            <h2 class="text-h5 q-mb-md">{{ t('home.overview.title', '서비스 개요') }}</h2>
            <q-list>
              <q-item v-for="i in 3" :key="`feature${i}`">
                <q-item-section>
                  {{ t(`home.overview.feature${i}`, [
                    '직관적인 인터페이스: 사용자 친화적인 디자인으로 누구나 쉽게 사용할 수 있습니다.',
                    '실시간 동기화: 모든 팀원의 스케줄이 실시간으로 공유되어 효율적인 협업이 가능합니다.',
                    '맞춤형 옵션: 개인과 팀의 니즈에 맞춰 다양한 설정 옵션을 제공합니다.'
                  ][i-1]) }}
                </q-item-section>
              </q-item>
            </q-list>
          </q-card>
        </div>

        <!-- Features -->
        <div class="col-12 col-md-4">
          <q-card class="q-pa-md">
            <h2 class="text-h5 q-mb-md">{{ t('home.features.title', '주요 기능') }}</h2>
            <q-list>
              <q-item v-for="i in 3" :key="`description${i}`">
                <q-item-section>
                  {{ t(`home.features.description${i}`, [
                    '스케줄 자동화: 직원 근무 패턴을 학습하여 최적의 스케줄을 자동으로 생성합니다.',
                    '실시간 알림: 근무 시간 변경, 승인 요청 등의 알림을 실시간으로 전송합니다.',
                    '보고서 생성: 근태 관리, 시간 외 근무 등의 보고서를 손쉽게 생성할 수 있습니다.'
                  ][i-1]) }}
                </q-item-section>
              </q-item>
            </q-list>
          </q-card>
        </div>

        <!-- Usage Guide -->
        <div class="col-12 col-md-4">
          <q-card class="q-pa-md">
            <h2 class="text-h5 q-mb-md">{{ t('home.usage.title', '사용 방법') }}</h2>
            <q-list>
              <q-item v-for="i in 3" :key="`step${i}`">
                <q-item-section avatar>
                  <q-avatar color="primary" text-color="white">{{ i }}</q-avatar>
                </q-item-section>
                <q-item-section>
                  {{ t(`home.usage.step${i}`, [
                    '웹사이트나 모바일 앱에 로그인하세요.',
                    '개인 및 팀 근무 패턴을 입력하여 스케줄을 설정할 수 있습니다.',
                    '언제 어디서든 실시간으로 근무 스케줄을 확인할 수 있습니다.'
                  ][i-1]) }}
                </q-item-section>
              </q-item>
            </q-list>
          </q-card>
        </div>
      </div>
    </div>

    <!-- Footer -->
    <q-footer class="bg-grey-9 text-white q-pa-lg">
      <div class="row items-center justify-between">
        <div class="col-12 col-md-auto text-center text-md-left">
          {{ t('home.footer.support', '문의 및 지원: 언제든 연락주세요. 친절하게 상담해 드리겠습니다.') }}
        </div>
        <div class="col-12 col-md-auto text-center text-md-right q-mt-sm q-mt-md-none">
          <q-btn
            color="white"
            outline
            :label="t('home.footer.button.contact', '문의하기')"
            href="https://gamma.app"
          />
        </div>
      </div>
    </q-footer>
  </q-page>
</template>

<style lang="scss">
</style>
