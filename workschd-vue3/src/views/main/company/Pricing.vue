<template>
  <q-page padding class="modern-page page-container">
    <div class="modern-bg__pattern absolute-full"></div>

    <!-- Header Section -->
    <div class="modern-page__header fade-in">
      <div class="row justify-center items-center q-py-xl">
        <div class="col-12 col-md-8 text-center">
          <h1 class="modern-text__title q-mb-md text-h3">{{ t('pricing.header.title', '요금제') }}</h1>
          <p class="modern-text__subtitle q-mb-xl text-h6">{{ t('pricing.header.subtitle', '비즈니스 규모에 맞는 최적의 요금제를 선택하세요') }}</p>
        </div>
      </div>
    </div>

    <!-- Pricing Cards -->
    <div class="modern-page__content q-pt-xl">
      <div class="row q-col-gutter-xl justify-center">
        <!-- Starter Plan -->
        <div class="col-12 col-md-6 col-lg-4">
          <div class="pricing-card slide-up stagger-1">
            <div class="pricing-card__header text-center q-pa-lg">
              <h3 class="text-h5 q-mb-sm">{{ t('pricing.starter.title', '스타터') }}</h3>
              <p class="text-caption q-mb-md">{{ t('pricing.starter.subtitle', '소규모 팀에 적합') }}</p>
              <div class="price-display">
                <span class="text-h3 text-weight-bold">₩19,000</span>
                <span class="text-caption">/월</span>
              </div>
              <p class="text-caption">{{ t('pricing.starter.per', '사용자당') }}</p>
            </div>
            <div class="pricing-card__body q-pa-lg">
              <q-list>
                <q-item v-for="feature in starterFeatures" :key="feature" dense>
                  <q-item-section avatar>
                    <q-icon name="check" color="positive" size="sm"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label class="text-body2">{{ feature }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
            <div class="pricing-card__footer q-pa-lg">
              <q-btn 
                class="full-width" 
                color="primary" 
                :label="t('pricing.starter.button', '시작하기')" 
                to="/signup" 
                unelevated
                no-caps
              />
            </div>
          </div>
        </div>

        <!-- Professional Plan -->
        <div class="col-12 col-md-6 col-lg-4">
          <div class="pricing-card pricing-card--featured slide-up stagger-2">
            <div class="featured-badge">
              <span>{{ t('pricing.professional.badge', '인기') }}</span>
            </div>
            <div class="pricing-card__header text-center q-pa-lg">
              <h3 class="text-h5 q-mb-sm">{{ t('pricing.professional.title', '프로페셔널') }}</h3>
              <p class="text-caption q-mb-md">{{ t('pricing.professional.subtitle', '중소기업에 최적') }}</p>
              <div class="price-display">
                <span class="text-h3 text-weight-bold">₩39,000</span>
                <span class="text-caption">/월</span>
              </div>
              <p class="text-caption">{{ t('pricing.professional.per', '사용자당') }}</p>
            </div>
            <div class="pricing-card__body q-pa-lg">
              <q-list>
                <q-item v-for="feature in professionalFeatures" :key="feature" dense>
                  <q-item-section avatar>
                    <q-icon name="check" color="positive" size="sm"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label class="text-body2">{{ feature }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
            <div class="pricing-card__footer q-pa-lg">
              <q-btn 
                class="full-width" 
                color="primary" 
                :label="t('pricing.professional.button', '시작하기')" 
                to="/signup" 
                unelevated
                no-caps
              />
            </div>
          </div>
        </div>

        <!-- Enterprise Plan -->
        <div class="col-12 col-md-6 col-lg-4">
          <div class="pricing-card slide-up stagger-3">
            <div class="pricing-card__header text-center q-pa-lg">
              <h3 class="text-h5 q-mb-sm">{{ t('pricing.enterprise.title', '엔터프라이즈') }}</h3>
              <p class="text-caption q-mb-md">{{ t('pricing.enterprise.subtitle', '대기업 및 맞춤형 솔루션') }}</p>
              <div class="price-display">
                <span class="text-h3 text-weight-bold">{{ t('pricing.enterprise.price', '문의') }}</span>
              </div>
              <p class="text-caption">{{ t('pricing.enterprise.per', '맞춤 견적') }}</p>
            </div>
            <div class="pricing-card__body q-pa-lg">
              <q-list>
                <q-item v-for="feature in enterpriseFeatures" :key="feature" dense>
                  <q-item-section avatar>
                    <q-icon name="check" color="positive" size="sm"/>
                  </q-item-section>
                  <q-item-section>
                    <q-item-label class="text-body2">{{ feature }}</q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
            <div class="pricing-card__footer q-pa-lg">
              <q-btn 
                class="full-width" 
                color="primary" 
                :label="t('pricing.enterprise.button', '문의하기')" 
                to="/contact" 
                unelevated
                no-caps
              />
            </div>
          </div>
        </div>
      </div>

      <!-- FAQ Section -->
      <div class="row justify-center q-mt-xl">
        <div class="col-12 col-md-8">
          <div class="text-center q-mb-lg">
            <h2 class="text-h4 q-mb-md">{{ t('pricing.faq.title', '자주 묻는 질문') }}</h2>
          </div>
          <q-list bordered separator class="rounded-borders">
            <q-expansion-item 
              v-for="(faq, index) in faqs" 
              :key="index"
              :label="faq.question"
              icon="help_outline"
              class="q-pa-md"
            >
              <div class="q-pa-md">
                {{ faq.answer }}
              </div>
            </q-expansion-item>
          </q-list>
        </div>
      </div>

      <!-- CTA Section -->
      <div class="row justify-center q-mt-xl q-py-xl">
        <div class="col-12 col-md-8 text-center">
          <div class="modern-bg__glass q-pa-xl" style="border-radius: 24px;">
            <h2 class="modern-text__title q-mb-md">{{ t('pricing.cta.title', '14일 무료 체험') }}</h2>
            <p class="q-mb-xl">{{ t('pricing.cta.description', '신용카드 없이도 모든 기능을 체험할 수 있습니다.') }}</p>
            <q-btn 
              class="modern-button" 
              color="primary" 
              :label="t('pricing.cta.button', '무료 체험 시작')" 
              to="/signup" 
              no-caps 
              size="lg" 
            />
          </div>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const starterFeatures = [
  '최대 10명 팀원',
  '기본 스케줄링',
  '모바일 앱 지원',
  '이메일 지원',
  '기본 보고서'
]

const professionalFeatures = [
  '최대 50명 팀원',
  '고급 스케줄링',
  '실시간 알림',
  '우선 지원',
  '상세 분석',
  'API 접근',
  '맞춤 보고서'
]

const enterpriseFeatures = [
  '무제한 팀원',
  '맞춤형 기능',
  '전담 계정 관리자',
  '24/7 지원',
  '고급 보안',
  'SSO 연동',
  '온프레미스 설치'
]

const faqs = [
  {
    question: '무료 체험 기간 동안 모든 기능을 사용할 수 있나요?',
    answer: '네, 14일 무료 체험 기간 동안 선택하신 요금제의 모든 기능을 제한 없이 사용하실 수 있습니다.'
  },
  {
    question: '요금제는 언제든지 변경할 수 있나요?',
    answer: '네, 언제든지 요금제를 업그레이드하거나 다운그레이드할 수 있습니다. 변경된 요금은 다음 결제일부터 적용됩니다.'
  },
  {
    question: '계약 기간이 있나요?',
    answer: '아니요, 모든 요금제는 월 단위 구독으로 운영되며 언제든지 해지할 수 있습니다.'
  },
  {
    question: '결제는 어떻게 이루어지나요?',
    answer: '신용카드, 계좌이체, 그리고 기업용 청구서 결제를 지원합니다. 모든 결제는 안전하게 암호화되어 처리됩니다.'
  },
  {
    question: '사용자 수가 늘어나면 어떻게 되나요?',
    answer: '사용자 수가 요금제 한도를 초과하면 자동으로 상위 요금제로 안내해드리며, 고객님의 동의 하에 업그레이드가 진행됩니다.'
  }
]
</script>

<style lang="scss" scoped>
.pricing-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
  }
  
  &--featured {
    border: 2px solid var(--q-primary);
    transform: scale(1.05);
    
    .pricing-card__header {
      background: linear-gradient(135deg, var(--q-primary), var(--q-secondary));
      color: white;
    }
  }
  
  &__body {
    flex: 1;
  }
}

.featured-badge {
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--q-primary);
  color: white;
  padding: 4px 16px;
  border-radius: 16px;
  font-size: 12px;
  font-weight: bold;
}

.price-display {
  margin: 16px 0;
  
  .text-h3 {
    color: var(--q-primary);
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
}

@keyframes slideUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style> 