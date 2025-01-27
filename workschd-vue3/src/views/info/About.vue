<template>
  <q-page class="about-container">

    <div class="q-pa-md">
      <div class="row q-col-gutter-md">
        <!-- Service Overview -->
        <div class="col-12">
          <q-card class="q-pa-md">
            <h2 class="text-h5 q-mb-md">{{ t('about.overview.title', '서비스 개요') }}</h2>
            <div class="row q-col-gutter-md">
              <div class="col-12 col-md-6">
                <div class="row q-gutter-y-md">
                  <q-item v-for="(feature, index) in 3" :key="index">
                    <q-item-section avatar>
                      <q-avatar color="green" text-color="white">{{ index + 1 }}</q-avatar>
                    </q-item-section>
                    <q-item-section>
                      <p>{{ t(`about.overview.feature${index + 1}`, ['직관적인 인터페이스', '실시간 동기화', '맞춤형 옵션'][index]) }}</p>
                    </q-item-section>
                  </q-item>
                </div>
              </div>
              <div class="col-12 col-md-6">
                <q-img src="https://placehold.co/400x300" class="rounded-borders" />
              </div>
            </div>
          </q-card>
        </div>

        <!-- Features -->
        <div class="col-12">
          <h2 class="text-h5 q-mb-md">{{ t('about.features.title', '주요 기능') }}</h2>
          <div class="row q-col-gutter-md">
            <div class="col-12 col-md-4" v-for="i in 3" :key="i">
              <q-card class="q-pa-md">
                <h3 class="text-h6 q-mb-sm">{{ t(`about.features.feature${i}`, ['스케줄 자동화', '실시간 알림', '보고서 생성'][i-1]) }}</h3>
                <p>{{ t(`about.features.feature${i}.description`, ['직원 근무 패턴을 학습하여 최적의 스케줄을 자동으로 생성합니다.', '근무 시간 변경, 승인 요청 등의 알림을 실시간으로 전송합니다.', '근태 관리, 시간 외 근무 등의 보고서를 손쉽게 생성할 수 있습니다.'][i-1]) }}</p>
              </q-card>
            </div>
          </div>
        </div>

        <!-- Pricing -->
        <div class="col-12">
          <q-card class="q-pa-md">
            <h2 class="text-h5 q-mb-md">{{ t('about.pricing.title', '요금 정책') }}</h2>
            <q-table
              :rows="pricingRows"
              :columns="pricingColumns"
              row-key="type"
              flat
              bordered
            />
          </q-card>
        </div>

        <!-- Customer Feedback -->
        <div class="col-12">
          <q-card class="q-pa-md">
            <h2 class="text-h5 q-mb-md">{{ t('about.customerFeedback.title', '고객 후기') }}</h2>
            <div class="row q-col-gutter-md">
              <div class="col-12 col-md-6">
                <q-card-section>
                  <p class="text-body1">{{ t('about.customerFeedback.quote', '이전에는 팀의 스케줄 관리를 하는 것이 너무 복잡했지만, 이 서비스를 사용한 후 정말 편리해졌어요...') }}</p>
                </q-card-section>
              </div>
              <div class="col-12 col-md-6">
                <q-img src="https://placehold.co/300x300" class="rounded-borders" />
              </div>
            </div>
          </q-card>
        </div>
      </div>
    </div>

    <q-footer class="bg-grey-9 text-white q-pa-md">
      <div class="text-center">
        <p>{{ t('about.footer.copyright', '© 2024 Work Schedule Automation Service. All rights reserved.') }}</p>
      </div>
    </q-footer>
  </q-page>
</template>

<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { ref } from 'vue'

const { t } = useI18n()

const pricingColumns = [
  { name: 'type', label: t('about.pricing.criteria', '기준'), field: 'type', align: 'left' },
  { name: 'standard', label: t('about.pricing.standard', 'Standard'), field: 'standard', align: 'center' },
  { name: 'premium', label: t('about.pricing.premium', 'Premium'), field: 'premium', align: 'center' }
]

const pricingRows = [
  { type: t('about.pricing.team10', '10인 이하 팀'), standard: '$9/user', premium: '$25/user' },
  { type: t('about.pricing.team10to50', '10-50인 팀'), standard: '$15/user', premium: '$25/user' },
  { type: t('about.pricing.team50', '50인 이상 팀'), standard: '$25/user', premium: '$25/user' }
]
</script>