<template>
  <q-page padding>
    <h2>{{ t('team.schedule.title', 'Schedule Configuration') }}</h2>

    <div class="schedule-section">
      <h3>{{ t('team.schedule.minStaffTitle', 'Minimum Staff Per Day') }}</h3>
      <q-input
        v-for="day in daysOfWeek"
        :key="day.value"
        v-model="minStaffPerDay[day.value]"
        :label="t('team.schedule.minStaffFor', 'Minimum staff for {day}', { day: t(`days.${day.value.toLowerCase()}`, day.label) })"
        type="number"
        class="q-mb-md"
      />
    </div>

    <div class="schedule-section">
      <h3>{{ t('team.schedule.maxOffDaysTitle', 'Maximum Off Days Per Month') }}</h3>
      <q-input
        v-for="month in months"
        :key="month.value"
        v-model="maxOffDaysPerMonth[month.value]"
        :label="t('team.schedule.maxOffDaysFor', 'Max off days for {month}', { month: t(`months.${month.value}`, month.label) })"
        type="number"
        class="q-mb-md"
      />
    </div>

    <div class="q-mt-lg">
      <q-btn :label="t('team.schedule.saveConfig', 'Save Configuration')" color="primary" @click="saveConfiguration" />
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useQuasar } from 'quasar'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const $q = useQuasar()

interface DayConfig {
  value: string
  label: string
}

interface MonthConfig {
  value: number
  label: string
}

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

const saveConfiguration = async () => {
  try {
    await fetch('/api/schedule-config', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        minStaffPerDay: minStaffPerDay.value,
        maxOffDaysPerMonth: maxOffDaysPerMonth.value
      })
    })
    
    $q.notify({
      type: 'positive',
      message: 'Configuration saved successfully'
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to save configuration'
    })
  }
}
</script>

<style scoped>
.q-page {
  max-width: 800px;
  margin: auto;
}

.schedule-section {
  margin-bottom: 2rem;
}
</style>