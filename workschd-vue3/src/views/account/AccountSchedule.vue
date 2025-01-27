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
                    :label="t('schedule.preferences.preferredDays', '선호 근무 요일')"
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

      <!-- Schedule Calendar and Grid -->
      <div class="col-12">
        <q-card>
          <q-card-section class="bg-primary text-white q-py-sm">
            <div class="text-subtitle1">{{ t('schedule.unavailable.title', '근무 불가능 일정') }}</div>
          </q-card-section>

          <q-card-section class="q-pa-sm-md">
            <q-tabs
              v-model="activeTab"
              dense
              class="text-grey"
              active-color="primary"
              indicator-color="primary"
              align="justify"
              narrow-indicator
            >
              <q-tab name="calendar" :label="t('schedule.view.calendar', '달력')" />
              <q-tab name="grid" :label="t('schedule.view.grid', '목록')" />
            </q-tabs>

            <q-separator />

            <q-tab-panels v-model="activeTab" animated>
              <q-tab-panel name="calendar">
                <q-date
                  v-model="selectedDates"
                  multiple
                  today-btn
                  mask="YYYY-MM-DD"
                  :title="t('schedule.unavailable.selectDates', '근무 불가능한 날짜를 선택하세요')"
                  class="full-width"
                />
              </q-tab-panel>

              <q-tab-panel name="grid" class="q-pa-none">
                <GridDefault
                  :columnDefs="columnDefs"
                  :rowData="gridData"
                  @onCellClicked="onGridCellClicked"
                />
              </q-tab-panel>
            </q-tab-panels>

            <div class="row justify-end q-mt-md">
              <q-btn
                :label="t('schedule.unavailable.save', '저장')"
                color="primary"
                :loading="isSavingDates"
                @click="handleUnavailableDatesUpdate"
                size="sm"
              />
            </div>
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useQuasar, Notify } from 'quasar'
import { format } from 'date-fns'
import { ko } from 'date-fns/locale'
import apiAccount from '@/api/modules/api-account'
import GridDefault from '@/components/grid/GridDefault.vue'
import type { ColDef } from 'ag-grid-community'

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

const activeTab = ref('calendar')
const selectedDates = ref<string[]>([])
const isSavingDates = ref(false)

const columnDefs = ref<ColDef[]>([
  {
    headerName: t('schedule.grid.date', '날짜'),
    field: 'date',
    sortable: true,
    filter: true,
    editable: false
  },
  {
    headerName: t('schedule.grid.dayOfWeek', '요일'),
    field: 'dayOfWeek',
    sortable: true,
    filter: true,
    editable: false
  },
  {
    headerName: t('schedule.grid.reason', '사유'),
    field: 'reason',
    sortable: true,
    filter: true,
    editable: true
  }
])

// Transform selected dates for grid display
const gridData = computed(() => {
  return selectedDates.value.map(date => ({
    date: format(new Date(date), 'yyyy-MM-dd'),
    dayOfWeek: format(new Date(date), 'EEEE', { locale: ko }),
    reason: ''
  }))
})

function onGridCellClicked(params: any) {
  console.log('Grid cell clicked:', params)
}

async function handleUnavailableDatesUpdate() {
  try {
    isSavingDates.value = true
    const unavailableDates = gridData.value.map(row => ({
      date: row.date,
      reason: row.reason
    }))
    
    await apiAccount.saveUnavailableDates(accountId, unavailableDates)
    
    Notify.create({
      type: 'positive',
      message: t('schedule.unavailable.saveSuccess', '근무 불가능 일정이 저장되었습니다')
    })
  } catch (error) {
    console.error('Failed to update unavailable dates:', error)
    Notify.create({
      type: 'negative',
      message: t('schedule.unavailable.saveError', '근무 불가능 일정 저장에 실패했습니다')
    })
  } finally {
    isSavingDates.value = false
  }
}

async function loadUnavailableDates() {
  try {
    const response = await apiAccount.getUnavailableDates(accountId)
    const dates = response.data.dates.map(item => item.date)
    selectedDates.value = dates
  } catch (error) {
    console.error('Failed to load unavailable dates:', error)
    Notify.create({
      type: 'negative',
      message: t('schedule.unavailable.loadError', '근무 불가능 일정 로드에 실패했습니다')
    })
  }
}

const handleScheduleUpdate = async () => {
  try {
    isSaving.value = true
    await apiAccount.saveSchedulePreferences(accountId, schedulePreferences.value)
    
    Notify.create({
      type: 'positive',
      message: t('schedule.preferences.saveSuccess', '스케줄 설정이 저장되었습니다')
    })
  } catch (error) {
    Notify.create({
      type: 'negative',
      message: t('schedule.preferences.saveError', '스케줄 설정 저장에 실패했습니다')
    })
  } finally {
    isSaving.value = false
  }
}

// Load data on component mount
loadUnavailableDates()
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

// Add styles for calendar
:deep(.q-date) {
  box-shadow: none;
  border: 1px solid rgba(0, 0, 0, 0.12);
  border-radius: 4px;
}

// Adjust grid panel padding
:deep(.q-tab-panel--grid) {
  padding: 0;
}

// Make grid more compact on mobile
@media (max-width: 599px) {
  :deep(.ag-header-cell-label) {
    font-size: 12px;
  }
  
  :deep(.ag-cell) {
    font-size: 12px;
    padding: 4px;
  }
}
</style> 