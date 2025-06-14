<template>
  <q-page padding class="modern-page page-container">
    <div class="modern-bg__pattern absolute-full"></div>

    <!-- Header Section -->
    <div class="modern-page__header fade-in">
      <div class="row justify-center items-center q-py-xl">
        <div class="col-12 col-md-8 text-center">
          <h1 class="modern-text__title q-mb-md text-h3">{{ t('features.header.title', '강력한 기능들') }}</h1>
          <p class="modern-text__subtitle q-mb-xl text-h6">{{ t('features.header.subtitle', '효율적인 근무 관리를 위한 모든 기능을 한 곳에서') }}</p>
        </div>
      </div>
    </div>

    <!-- Features Grid -->
    <div class="modern-page__content q-pt-xl">
      <div class="row q-col-gutter-xl">
        <!-- Automated Scheduling -->
        <div class="col-12 col-md-6 col-lg-4">
          <div class="modern-card slide-up stagger-1">
            <div class="modern-card__header text-center q-pa-lg">
              <q-avatar size="80px" color="primary" text-color="white" icon="schedule" class="q-mb-md"/>
              <h3 class="text-h5 q-mb-sm">{{ t('features.automation.title', '자동 스케줄링') }}</h3>
            </div>
            <div class="modern-card__body">
              <p class="q-mb-md">{{ t('features.automation.description', 'AI 기반 알고리즘으로 최적의 근무 스케줄을 자동으로 생성합니다.') }}</p>
              <q-list dense>
                <q-item v-for="feature in automationFeatures" :key="feature">
                  <q-item-section avatar>
                    <q-icon name="check_circle" color="positive" size="sm"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label class="text-caption">{{ feature }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </div>
        </div>

        <!-- Real-time Collaboration -->
        <div class="col-12 col-md-6 col-lg-4">
          <div class="modern-card slide-up stagger-2">
            <div class="modern-card__header text-center q-pa-lg">
              <q-avatar size="80px" color="secondary" text-color="white" icon="people" class="q-mb-md"/>
              <h3 class="text-h5 q-mb-sm">{{ t('features.collaboration.title', '실시간 협업') }}</h3>
            </div>
            <div class="modern-card__body">
              <p class="q-mb-md">{{ t('features.collaboration.description', '팀원들과 실시간으로 소통하고 스케줄을 공유합니다.') }}</p>
              <q-list dense>
                <q-item v-for="feature in collaborationFeatures" :key="feature">
                  <q-item-section avatar>
                    <q-icon name="check_circle" color="positive" size="sm"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label class="text-caption">{{ feature }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </div>
        </div>

        <!-- Analytics & Reports -->
        <div class="col-12 col-md-6 col-lg-4">
          <div class="modern-card slide-up stagger-3">
            <div class="modern-card__header text-center q-pa-lg">
              <q-avatar size="80px" color="accent" text-color="white" icon="analytics" class="q-mb-md"/>
              <h3 class="text-h5 q-mb-sm">{{ t('features.analytics.title', '분석 및 보고서') }}</h3>
            </div>
            <div class="modern-card__body">
              <p class="q-mb-md">{{ t('features.analytics.description', '상세한 분석과 보고서로 업무 효율성을 향상시킵니다.') }}</p>
              <q-list dense>
                <q-item v-for="feature in analyticsFeatures" :key="feature">
                  <q-item-section avatar>
                    <q-icon name="check_circle" color="positive" size="sm"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label class="text-caption">{{ feature }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </div>
        </div>

        <!-- Mobile App -->
        <div class="col-12 col-md-6 col-lg-4">
          <div class="modern-card slide-up stagger-4">
            <div class="modern-card__header text-center q-pa-lg">
              <q-avatar size="80px" color="info" text-color="white" icon="smartphone" class="q-mb-md"/>
              <h3 class="text-h5 q-mb-sm">{{ t('features.mobile.title', '모바일 앱') }}</h3>
            </div>
            <div class="modern-card__body">
              <p class="q-mb-md">{{ t('features.mobile.description', '언제 어디서나 스케줄을 확인하고 관리할 수 있습니다.') }}</p>
              <q-list dense>
                <q-item v-for="feature in mobileFeatures" :key="feature">
                  <q-item-section avatar>
                    <q-icon name="check_circle" color="positive" size="sm"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label class="text-caption">{{ feature }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </div>
        </div>

        <!-- Integration -->
        <div class="col-12 col-md-6 col-lg-4">
          <div class="modern-card slide-up stagger-5">
            <div class="modern-card__header text-center q-pa-lg">
              <q-avatar size="80px" color="warning" text-color="white" icon="integration_instructions" class="q-mb-md"/>
              <h3 class="text-h5 q-mb-sm">{{ t('features.integration.title', '시스템 연동') }}</h3>
            </div>
            <div class="modern-card__body">
              <p class="q-mb-md">{{ t('features.integration.description', '기존 시스템과 쉽게 연동하여 사용할 수 있습니다.') }}</p>
              <q-list dense>
                <q-item v-for="feature in integrationFeatures" :key="feature">
                  <q-item-section avatar>
                    <q-icon name="check_circle" color="positive" size="sm"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label class="text-caption">{{ feature }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </div>
        </div>

        <!-- Security -->
        <div class="col-12 col-md-6 col-lg-4">
          <div class="modern-card slide-up stagger-6">
            <div class="modern-card__header text-center q-pa-lg">
              <q-avatar size="80px" color="negative" text-color="white" icon="security" class="q-mb-md"/>
              <h3 class="text-h5 q-mb-sm">{{ t('features.security.title', '보안') }}</h3>
            </div>
            <div class="modern-card__body">
              <p class="q-mb-md">{{ t('features.security.description', '강력한 보안으로 데이터를 안전하게 보호합니다.') }}</p>
              <q-list dense>
                <q-item v-for="feature in securityFeatures" :key="feature">
                  <q-item-section avatar>
                    <q-icon name="check_circle" color="positive" size="sm"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label class="text-caption">{{ feature }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </div>
        </div>
      </div>

      <!-- CTA Section -->
      <div class="row justify-center q-mt-xl q-py-xl">
        <div class="col-12 col-md-8 text-center">
          <div class="modern-bg__glass q-pa-xl" style="border-radius: 24px;">
            <h2 class="modern-text__title q-mb-md">{{ t('features.cta.title', '지금 바로 체험해보세요') }}</h2>
            <p class="q-mb-xl">{{ t('features.cta.description', '모든 기능을 무료로 체험할 수 있습니다.') }}</p>
            <div class="row q-col-gutter-md justify-center">
              <div class="col-auto">
                <q-btn class="modern-button" color="primary" :label="t('features.cta.start', '무료 체험 시작')" to="/signup" no-caps size="lg" />
              </div>
              <div class="col-auto">
                <q-btn class="modern-button modern-button--secondary" :label="t('features.cta.demo', '데모 보기')" to="/about" no-caps size="lg" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const automationFeatures = [
  '직원 선호도 기반 배치',
  '법정 근무시간 준수',
  '공휴일 자동 반영',
  '교대 패턴 최적화'
]

const collaborationFeatures = [
  '실시간 알림 시스템',
  '스케줄 공유 및 동기화',
  '팀원 간 메시지',
  '요청 및 승인 시스템'
]

const analyticsFeatures = [
  '근무시간 분석',
  '생산성 레포트',
  '비용 분석',
  '예측 분석'
]

const mobileFeatures = [
  'iOS/Android 앱',
  '오프라인 모드',
  '푸시 알림',
  '위치 기반 체크인'
]

const integrationFeatures = [
  'HR 시스템 연동',
  'API 제공',
  '급여 시스템 연동',
  '출입 통제 시스템'
]

const securityFeatures = [
  'SSL 암호화',
  '데이터 백업',
  '역할 기반 접근 제어',
  'GDPR 준수'
]
</script>

<style lang="scss" scoped>
.modern-card {
  height: 100%;
  
  .modern-card__header {
    background: linear-gradient(145deg, #f8f9fa, #e9ecef);
  }
}

.fade-in-up {
  opacity: 0;
  transform: translateY(20px);
  animation: fadeInUp 0.8s ease forwards;
}

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.slide-up {
  opacity: 0;
  transform: translateY(30px);
  animation: slideUp 0.6s ease forwards;
  
  &.stagger-1 { animation-delay: 0.1s; }
  &.stagger-2 { animation-delay: 0.2s; }
  &.stagger-3 { animation-delay: 0.3s; }
  &.stagger-4 { animation-delay: 0.4s; }
  &.stagger-5 { animation-delay: 0.5s; }
  &.stagger-6 { animation-delay: 0.6s; }
}

@keyframes slideUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style> 