<template>
  <q-page padding class="modern-page page-container">
    <div class="modern-bg__pattern absolute-full"></div>

    <!-- Header Section -->
    <div class="modern-page__header fade-in">
      <div class="row justify-center items-center q-py-xl">
        <div class="col-12 col-md-8 text-center">
          <h1 class="modern-text__title q-mb-md text-h3">{{ t('contact.header.title', '문의하기') }}</h1>
          <p class="modern-text__subtitle q-mb-xl text-h6">{{ t('contact.header.subtitle', '궁금한 점이 있으시면 언제든지 연락주세요') }}</p>
        </div>
      </div>
    </div>

    <!-- Contact Content -->
    <div class="modern-page__content q-pt-xl">
      <div class="row q-col-gutter-xl">
        <!-- Contact Information -->
        <div class="col-12 col-md-6">
          <div class="modern-card slide-up stagger-1" style="height: 100%;">
            <div class="modern-card__header q-pa-lg">
              <h2 class="text-h5 q-mb-md">{{ t('contact.info.title', '연락처') }}</h2>
              <p class="text-body2">{{ t('contact.info.subtitle', '다양한 방법으로 연락하실 수 있습니다.') }}</p>
            </div>
            <div class="modern-card__body q-pa-lg">
              <div class="contact-info">
                <div class="contact-item q-mb-lg">
                  <q-icon name="email" size="md" color="primary" class="q-mr-md"/>
                  <div>
                    <div class="text-subtitle1 text-weight-medium">{{ t('contact.info.email', '이메일') }}</div>
                    <div class="text-body2">info@workschedule.voyagerss.com</div>
                    <div class="text-caption text-grey-6">{{ t('contact.info.emailNote', '24시간 내 답변 드립니다') }}</div>
                  </div>
                </div>

                <div class="contact-item q-mb-lg">
                  <q-icon name="phone" size="md" color="primary" class="q-mr-md"/>
                  <div>
                    <div class="text-subtitle1 text-weight-medium">{{ t('contact.info.phone', '전화') }}</div>
                    <div class="text-body2">+82 02-123-4567</div>
                    <div class="text-caption text-grey-6">{{ t('contact.info.phoneNote', '평일 09:00 - 18:00') }}</div>
                  </div>
                </div>

                <div class="contact-item q-mb-lg">
                  <q-icon name="location_on" size="md" color="primary" class="q-mr-md"/>
                  <div>
                    <div class="text-subtitle1 text-weight-medium">{{ t('contact.info.address', '주소') }}</div>
                    <div class="text-body2">서울특별시 강남구 테헤란로 123</div>
                    <div class="text-caption text-grey-6">{{ t('contact.info.addressNote', '방문 상담 가능 (사전 예약 필수)') }}</div>
                  </div>
                </div>

                <div class="contact-item">
                  <q-icon name="schedule" size="md" color="primary" class="q-mr-md"/>
                  <div>
                    <div class="text-subtitle1 text-weight-medium">{{ t('contact.info.hours', '운영시간') }}</div>
                    <div class="text-body2">{{ t('contact.info.hoursDetail', '평일 09:00 - 18:00') }}</div>
                    <div class="text-caption text-grey-6">{{ t('contact.info.hoursNote', '주말 및 공휴일 휴무') }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Contact Form -->
        <div class="col-12 col-md-6">
          <div class="modern-card slide-up stagger-2" style="height: 100%;">
            <div class="modern-card__header q-pa-lg">
              <h2 class="text-h5 q-mb-md">{{ t('contact.form.title', '문의 양식') }}</h2>
              <p class="text-body2">{{ t('contact.form.subtitle', '아래 양식을 작성해주시면 빠르게 답변 드리겠습니다.') }}</p>
            </div>
            <div class="modern-card__body q-pa-lg">
              <q-form @submit="onSubmit" class="q-gutter-md">
                <div class="row q-col-gutter-md">
                  <div class="col-12 col-sm-6">
                    <q-input
                      v-model="form.name"
                      :label="t('contact.form.name', '이름')"
                      outlined
                      required
                      :rules="[val => !!val || t('contact.form.nameRequired', '이름을 입력해주세요')]"
                    />
                  </div>
                  <div class="col-12 col-sm-6">
                    <q-input
                      v-model="form.company"
                      :label="t('contact.form.company', '회사명')"
                      outlined
                    />
                  </div>
                </div>

                <q-input
                  v-model="form.email"
                  type="email"
                  :label="t('contact.form.email', '이메일')"
                  outlined
                  required
                  :rules="[
                    val => !!val || t('contact.form.emailRequired', '이메일을 입력해주세요'),
                    val => /.+@.+\..+/.test(val) || t('contact.form.emailInvalid', '올바른 이메일을 입력해주세요')
                  ]"
                />

                <q-input
                  v-model="form.phone"
                  :label="t('contact.form.phone', '전화번호')"
                  outlined
                />

                <q-select
                  v-model="form.type"
                  :options="inquiryTypes"
                  :label="t('contact.form.type', '문의 유형')"
                  outlined
                  required
                  :rules="[val => !!val || t('contact.form.typeRequired', '문의 유형을 선택해주세요')]"
                />

                <q-input
                  v-model="form.subject"
                  :label="t('contact.form.subject', '제목')"
                  outlined
                  required
                  :rules="[val => !!val || t('contact.form.subjectRequired', '제목을 입력해주세요')]"
                />

                <q-input
                  v-model="form.message"
                  type="textarea"
                  :label="t('contact.form.message', '문의 내용')"
                  outlined
                  rows="4"
                  required
                  :rules="[val => !!val || t('contact.form.messageRequired', '문의 내용을 입력해주세요')]"
                />

                <div class="row justify-end q-mt-lg">
                  <q-btn
                    type="submit"
                    color="primary"
                    :label="t('contact.form.submit', '문의하기')"
                    :loading="isSubmitting"
                    unelevated
                    no-caps
                    class="q-px-xl"
                  />
                </div>
              </q-form>
            </div>
          </div>
        </div>
      </div>

      <!-- FAQ Section -->
      <div class="row justify-center q-mt-xl">
        <div class="col-12 col-md-10">
          <div class="text-center q-mb-lg">
            <h2 class="text-h4 q-mb-md">{{ t('contact.faq.title', '자주 묻는 질문') }}</h2>
            <p class="text-body1">{{ t('contact.faq.subtitle', '빠른 답변을 위해 자주 묻는 질문을 확인해보세요') }}</p>
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
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useQuasar } from 'quasar'

const { t } = useI18n()
const $q = useQuasar()

const isSubmitting = ref(false)

const form = ref({
  name: '',
  company: '',
  email: '',
  phone: '',
  type: '',
  subject: '',
  message: ''
})

const inquiryTypes = [
  { label: t('contact.types.general', '일반 문의'), value: 'general' },
  { label: t('contact.types.sales', '영업 문의'), value: 'sales' },
  { label: t('contact.types.support', '기술 지원'), value: 'support' },
  { label: t('contact.types.partnership', '파트너십'), value: 'partnership' },
  { label: t('contact.types.other', '기타'), value: 'other' }
]

const faqs = [
  {
    question: '무료 체험은 어떻게 신청하나요?',
    answer: '홈페이지 상단의 "무료 체험" 버튼을 클릭하거나 요금제 페이지에서 원하는 플랜을 선택하여 신청할 수 있습니다. 신용카드 정보 없이도 14일간 무료로 체험 가능합니다.'
  },
  {
    question: '기존 시스템과 연동이 가능한가요?',
    answer: '네, REST API를 통해 대부분의 HR 시스템, 급여 시스템과 연동 가능합니다. 엔터프라이즈 플랜에서는 맞춤형 연동 솔루션도 제공합니다.'
  },
  {
    question: '데이터 보안은 어떻게 보장되나요?',
    answer: 'SSL 암호화, 정기적인 백업, 역할 기반 접근 제어 등 다층 보안 시스템을 운영하며, GDPR 및 개인정보보호법을 준수합니다.'
  },
  {
    question: '모바일 앱도 제공되나요?',
    answer: '네, iOS와 Android용 모바일 앱을 제공합니다. 오프라인 모드, 푸시 알림, 위치 기반 체크인 등의 기능을 지원합니다.'
  },
  {
    question: '교육이나 온보딩 지원이 있나요?',
    answer: '프로페셔널 플랜 이상에서는 온보딩 지원을, 엔터프라이즈 플랜에서는 전담 계정 관리자와 맞춤형 교육을 제공합니다.'
  }
]

const onSubmit = async () => {
  try {
    isSubmitting.value = true
    
    // API 호출 시뮬레이션
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    $q.notify({
      type: 'positive',
      message: t('contact.form.success', '문의가 성공적으로 전송되었습니다. 빠른 시일 내에 답변 드리겠습니다.'),
      position: 'top'
    })
    
    // 폼 초기화
    form.value = {
      name: '',
      company: '',
      email: '',
      phone: '',
      type: '',
      subject: '',
      message: ''
    }
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('contact.form.error', '문의 전송 중 오류가 발생했습니다. 다시 시도해주세요.'),
      position: 'top'
    })
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style lang="scss" scoped>
.contact-info {
  .contact-item {
    display: flex;
    align-items: flex-start;
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
}

@keyframes slideUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style> 