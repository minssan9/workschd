<template>
  <q-page padding class="q-px-sm q-px-md-lg">
    <div class="row q-col-gutter-md">
      <!-- Schedule Preferences -->
      <div class="col-12 col-sm-10 col-md-8 self-center">
        <q-card>
          <q-card-section class="bg-primary text-white q-py-sm">
            <div class="text-subtitle1">{{ t('schedule.preferences.title', '스케줄 설정') }}</div>
          </q-card-section>

          <q-card-section class="q-pa-sm-md">
            <q-form @submit="handleScheduleUpdate" class="q-gutter-y-md">
              <div class="row q-col-gutter-sm">
                <div class="col-12">
                  <q-select
                    v-model="schedulePreferences.preferredDays"
                    :label="t('schedule.preferences.preferredDays', '선호 근무일')"
                    :options="daysOfWeek"
                    multiple
                    dense
                    outlined
                    use-chips
                    options-dense
                  />
                </div>
                <div class="col-12">
                  <q-select
                    v-model="schedulePreferences.preferredShifts"
                    :label="t('schedule.preferences.preferredShifts', '선호 근무 시간대')"
                    :options="shiftOptions"
                    multiple
                    dense
                    outlined
                    use-chips
                    options-dense
                  />
                </div>
              </div>

              <div class="row justify-end q-mt-sm">
                <q-btn
                  :label="t('schedule.preferences.save', '설정 저장')"
                  type="submit"
                  color="primary"
                  :loading="isSaving"
                  size="sm"
                />
              </div>
            </q-form>
          </q-card-section>
        </q-card>
      </div>

      <!-- Schedule Calendar -->
      <div class="col-12">
        <q-card>
          <q-card-section class="bg-primary text-white q-py-sm">
            <div class="text-subtitle1">{{ t('schedule.calendar.title', '근무 일정') }}</div>
          </q-card-section>

          <q-card-section class="q-pa-sm-md">
            <!-- Add your calendar component here -->
          </q-card-section>
        </q-card>
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

interface SchedulePreferences {
  preferredDays: string[]
  preferredShifts: string[]
}

const schedulePreferences = ref<SchedulePreferences>({
  preferredDays: [],
  preferredShifts: []
})

const isSaving = ref(false)

const daysOfWeek = [
  { label: t('days.monday', '월요일'), value: 'MONDAY' },
  { label: t('days.tuesday', '화요일'), value: 'TUESDAY' },
  { label: t('days.wednesday', '수요일'), value: 'WEDNESDAY' },
  { label: t('days.thursday', '목요일'), value: 'THURSDAY' },
  { label: t('days.friday', '금요일'), value: 'FRIDAY' },
  { label: t('days.saturday', '토요일'), value: 'SATURDAY' },
  { label: t('days.sunday', '일요일'), value: 'SUNDAY' }
]

const shiftOptions = [
  { label: t('shifts.morning', '오전 (06:00-14:00)'), value: 'MORNING' },
  { label: t('shifts.afternoon', '오후 (14:00-22:00)'), value: 'AFTERNOON' },
  { label: t('shifts.night', '야간 (22:00-06:00)'), value: 'NIGHT' }
]

const handleScheduleUpdate = async () => {
  try {
    isSaving.value = true
    // Add your API call here
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    $q.notify({
      type: 'positive',
      message: t('schedule.preferences.saveSuccess', '스케줄 설정이 저장되었습니다')
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('schedule.preferences.saveError', '스케줄 설정 저장에 실패했습니다')
    })
  } finally {
    isSaving.value = false
  }
}
</script>

<style lang="scss" scoped>
// Add responsive padding
@media (max-width: 599px) {
  .q-card-section {
    padding: 8px !important;
  }
  
  .q-gutter-y-md > * {
    margin-top: 8px !important;
    margin-bottom: 8px !important;
  }
}

// Make chips smaller on mobile
:deep(.q-chip) {
  font-size: 12px;
  padding: 0 6px;
  height: 24px;
}
</style> 