<template>
  <div>
    <h3 class="text-h5 q-mb-md">{{ t('team.accountWorkHour.title', 'Schedule Configuration') }}</h3>

    <div class="row q-col-gutter-md">
      <!-- Minimum Staff Per Day Section -->
      <div class="col-12 col-md-6">
        <q-card class="config-card">
          <q-card-section>
            <div class="text-h6">{{ t('team.accountWorkHour.minStaffTitle', 'Minimum Staff Per Day') }}</div>
            <q-separator class="q-my-sm" />
            
            <div class="row q-col-gutter-sm">
              <div v-for="day in daysOfWeek" :key="day.value" class="col-12 col-sm-6">
                <q-input
                  v-model="minStaffPerDay[day.value]"
                  :label="t(`days.${day.value.toLowerCase()}`, day.label)"
                  type="number"
                  filled
                  dense
                  min="0"
                >
                  <template v-slot:prepend>
                    <q-icon name="event" />
                  </template>
                </q-input>
              </div>
            </div>
          </q-card-section>
        </q-card>
      </div>

      <!-- Maximum Off Days Per Month Section -->
      <div class="col-12 col-md-6">
        <q-card class="config-card">
          <q-card-section>
            <div class="text-h6">{{ t('team.accountWorkHour.maxOffDaysTitle', 'Maximum Off Days Per Month') }}</div>
            <q-separator class="q-my-sm" />
            
            <div class="row q-col-gutter-sm">
              <div v-for="month in months" :key="month.value" class="col-12 col-sm-6 col-lg-4">
                <q-input
                  v-model="maxOffDaysPerMonth[month.value]"
                  :label="t(`months.${month.value}`, month.label)"
                  type="number"
                  filled
                  dense
                  min="0"
                >
                  <template v-slot:prepend>
                    <q-icon name="calendar_month" />
                  </template>
                </q-input>
              </div>
            </div>
          </q-card-section>
        </q-card>
      </div>
      
      <!-- Additional Configuration Options -->
      <div class="col-12">
        <q-card class="config-card">
          <q-card-section>
            <div class="text-h6">{{ t('team.accountWorkHour.additionalOptions', 'Additional Options') }}</div>
            <q-separator class="q-my-sm" />
            
            <div class="row q-col-gutter-md">
              <div class="col-12 col-md-6">
                <q-toggle
                  v-model="allowWeekendWork"
                  :label="t('team.accountWorkHour.allowWeekendWork', 'Allow Weekend Work')"
                />
              </div>
              <div class="col-12 col-md-6">
                <q-toggle
                  v-model="enforceMinimumRest"
                  :label="t('team.accountWorkHour.enforceMinimumRest', 'Enforce Minimum Rest Between Shifts')"
                />
              </div>
              <div class="col-12 col-md-6">
                <q-input
                  v-model="maxConsecutiveWorkDays"
                  :label="t('team.accountWorkHour.maxConsecutiveWorkDays', 'Maximum Consecutive Work Days')"
                  type="number"
                  filled
                  dense
                  min="1"
                  max="14"
                />
              </div>
              <div class="col-12 col-md-6">
                <q-select
                  v-model="scheduleGenerationFrequency"
                  :options="scheduleFrequencyOptions"
                  :label="t('team.accountWorkHour.generationFrequency', 'Schedule Generation Frequency')"
                  filled
                  dense
                />
              </div>
            </div>
          </q-card-section>
        </q-card>
      </div>
    </div>

    <div class="row justify-end q-mt-md">
      <q-btn 
        :label="t('common.reset', 'Reset')" 
        color="secondary" 
        flat 
        class="q-mr-sm" 
        @click="resetConfiguration" 
      />
      <q-btn 
        :label="t('team.accountWorkHour.saveConfig', 'Save Configuration')"
        color="primary" 
        @click="saveConfiguration" 
        :loading="isSaving"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, defineProps } from 'vue'
import { useQuasar } from 'quasar'
import { useI18n } from 'vue-i18n'
import { useTeamStore } from '@/stores/modules/store_team'
import { ScheduleConfig, DayConfig, MonthConfig } from '@/interface/accountWorkHour'

const props = defineProps({
  teamId: {
    type: Number,
    required: true
  }
})

const { t } = useI18n()
const $q = useQuasar()

const teamStore = useTeamStore()
const isSaving = ref(false)

// Day and month configurations
const daysOfWeek: DayConfig[] = [
  { value: 'MONDAY', label: 'Monday' },
  { value: 'TUESDAY', label: 'Tuesday' },
  { value: 'WEDNESDAY', label: 'Wednesday' },
  { value: 'THURSDAY', label: 'Thursday' },
  { value: 'FRIDAY', label: 'Friday' },
  { value: 'SATURDAY', label: 'Saturday' },
  { value: 'SUNDAY', label: 'Sunday' }
]

const months: MonthConfig[] = [
  { value: 1, label: 'January' },
  { value: 2, label: 'February' },
  { value: 3, label: 'March' },
  { value: 4, label: 'April' },
  { value: 5, label: 'May' },
  { value: 6, label: 'June' },
  { value: 7, label: 'July' },
  { value: 8, label: 'August' },
  { value: 9, label: 'September' },
  { value: 10, label: 'October' },
  { value: 11, label: 'November' },
  { value: 12, label: 'December' }
]

// Schedule configuration values
const minStaffPerDay = ref({
  MONDAY: 1,
  TUESDAY: 1,
  WEDNESDAY: 1,
  THURSDAY: 1,
  FRIDAY: 1,
  SATURDAY: 1,
  SUNDAY: 1
})

const maxOffDaysPerMonth = ref({
  1: 4, 2: 4, 3: 4, 4: 4, 5: 4, 6: 4,
  7: 4, 8: 4, 9: 4, 10: 4, 11: 4, 12: 4
})

// Additional configuration options
const allowWeekendWork = ref(true)
const enforceMinimumRest = ref(true)
const maxConsecutiveWorkDays = ref(5)
const scheduleGenerationFrequency = ref('MONTHLY')
const scheduleFrequencyOptions = [
  { label: t('team.accountWorkHour.frequency.weekly', 'Weekly'), value: 'WEEKLY' },
  { label: t('team.accountWorkHour.frequency.biweekly', 'Bi-weekly'), value: 'BIWEEKLY' },
  { label: t('team.accountWorkHour.frequency.monthly', 'Monthly'), value: 'MONTHLY' }
]

// Default values for reset
const defaultMinStaffPerDay = {
  MONDAY: 1,
  TUESDAY: 1,
  WEDNESDAY: 1,
  THURSDAY: 1,
  FRIDAY: 1,
  SATURDAY: 1,
  SUNDAY: 1
}

const defaultMaxOffDaysPerMonth = {
  1: 4, 2: 4, 3: 4, 4: 4, 5: 4, 6: 4,
  7: 4, 8: 4, 9: 4, 10: 4, 11: 4, 12: 4
}

const fetchScheduleConfig = async () => {
  try {
    const response = await fetch(`/accountWorkHour-config/${props.teamId}`)
    const data = await response.json()
    
    if (data) {
      minStaffPerDay.value = data.minStaffPerDay || minStaffPerDay.value
      maxOffDaysPerMonth.value = data.maxOffDaysPerMonth || maxOffDaysPerMonth.value
      
      // Set additional options if available
      if (data.additionalOptions) {
        allowWeekendWork.value = data.additionalOptions.allowWeekendWork ?? allowWeekendWork.value
        enforceMinimumRest.value = data.additionalOptions.enforceMinimumRest ?? enforceMinimumRest.value
        maxConsecutiveWorkDays.value = data.additionalOptions.maxConsecutiveWorkDays ?? maxConsecutiveWorkDays.value
        scheduleGenerationFrequency.value = data.additionalOptions.scheduleGenerationFrequency ?? scheduleGenerationFrequency.value
      }
    }
  } catch (error) {
    console.error('Failed to fetch accountWorkHour configuration', error)
    $q.notify({
      type: 'negative',
      message: t('team.accountWorkHour.fetchError', 'Failed to load accountWorkHour configuration')
    })
  }
}

const saveConfiguration = async () => {
  try {
    isSaving.value = true
    
    await fetch(`/accountWorkHour-config/${props.teamId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        minStaffPerDay: minStaffPerDay.value,
        maxOffDaysPerMonth: maxOffDaysPerMonth.value,
        additionalOptions: {
          allowWeekendWork: allowWeekendWork.value,
          enforceMinimumRest: enforceMinimumRest.value,
          maxConsecutiveWorkDays: maxConsecutiveWorkDays.value,
          scheduleGenerationFrequency: scheduleGenerationFrequency.value
        }
      })
    })
    
    $q.notify({
      type: 'positive',
      message: t('team.accountWorkHour.saveSuccess', 'Configuration saved successfully')
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('team.accountWorkHour.saveError', 'Failed to save configuration')
    })
  } finally {
    isSaving.value = false
  }
}

const resetConfiguration = () => {
  $q.dialog({
    title: t('team.accountWorkHour.resetConfirmTitle', 'Reset Configuration'),
    message: t('team.accountWorkHour.resetConfirmMessage', 'Are you sure you want to reset all configuration values to defaults?'),
    cancel: true,
    persistent: true
  }).onOk(() => {
    minStaffPerDay.value = { ...defaultMinStaffPerDay }
    maxOffDaysPerMonth.value = { ...defaultMaxOffDaysPerMonth }
    allowWeekendWork.value = true
    enforceMinimumRest.value = true
    maxConsecutiveWorkDays.value = 5
    scheduleGenerationFrequency.value = 'MONTHLY'
    
    $q.notify({
      type: 'info',
      message: t('team.accountWorkHour.resetSuccess', 'Configuration reset to defaults')
    })
  })
}

// Watch for changes in teamId to reload data
watch(() => props.teamId, () => {
  fetchScheduleConfig()
})

onMounted(() => {
  fetchScheduleConfig()
})
</script>

<style scoped>
.config-card {
  height: 100%;
  transition: all 0.3s ease;
}

.config-card:hover {
  box-shadow: 0 8px 12px rgba(0, 0, 0, 0.1);
}

.q-card {
  border-radius: 8px;
}

.q-card__section {
  padding: 16px;
}

.text-h6 {
  font-weight: 500;
  margin-bottom: 8px;
}
</style>