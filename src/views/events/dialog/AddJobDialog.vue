<template>
  <q-dialog v-model="dialogVisible" @hide="onHide" maximized>
    <q-card style="width: 800px; max-width: 90vw">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">{{ t('events.dialog.registerNewJob', 'Register New Job') }}</div>
        <q-space />
        <q-btn icon="close" flat round dense v-close-popup />
      </q-card-section>

      <q-card-section>
        <q-form @submit="onSubmit" class="q-gutter-md">
          <!-- Basic Information Section -->
          <div class="text-subtitle1 q-mb-sm">{{ t('events.dialog.basicInfo', 'Basic Information') }}</div>
          <div class="row q-col-gutter-md">
            <div class="col-12 col-md-6">
              <q-select
                v-model="jobData.branch_id"
                :label="t('events.dialog.branch', 'Branch')"
                :options="branches"
                option-value="id"
                option-label="name"
                required
                :rules="[val => !!val || t('validation.required', 'Field is required')]"
              />
            </div>
            <div class="col-12 col-md-6">
              <q-select
                v-model="jobData.store_id"
                :label="t('events.dialog.store', 'Store')"
                :options="stores"
                option-value="id"
                option-label="name"
                required
                :rules="[val => !!val || t('validation.required', 'Field is required')]"
              />
            </div>
            <div class="col-12">
              <q-input
                v-model="jobData.additional_info"
                :label="t('events.dialog.additionalInfo', 'Additional Info')"
                type="textarea"
                rows="2"
              />
            </div>
          </div>

          <!-- Time and Wage Section -->
          <q-separator class="q-my-md" />
          <div class="text-subtitle1 q-mb-sm">{{ t('events.dialog.timeAndWage', 'Time & Wage') }}</div>
          <div class="row q-col-gutter-md">
            <div class="col-12 col-md-6">
              <q-input
                v-model="jobData.task_datetime"
                :label="t('events.dialog.taskDateTime', 'Task Date')"
                type="date"
                required
                :rules="[val => !!val || t('validation.required', 'Field is required')]"
              />
            </div>
            <div class="col-12 col-md-6">
              <div class="row q-col-gutter-sm">
                <div class="col-6">
                  <q-input
                    v-model="jobData.start_time"
                    :label="t('events.dialog.startTime', 'Start Time')"
                    type="time"
                    required
                    :rules="[val => !!val || t('validation.required', 'Field is required')]"
                  />
                </div>
                <div class="col-6">
                  <q-input
                    v-model="jobData.end_time"
                    :label="t('events.dialog.endTime', 'End Time')"
                    type="time"
                    required
                    :rules="[val => !!val || t('validation.required', 'Field is required')]"
                  />
                </div>
              </div>
            </div>
            <div class="col-12 col-md-6">
              <q-input
                v-model.number="jobData.daily_wage"
                :label="t('events.dialog.dailyWage', 'Daily Wage')"
                type="number"
                prefix="$"
                required
                :rules="[val => !!val || t('validation.required', 'Field is required')]"
              />
            </div>
          </div>

          <!-- Additional Options Section -->
          <q-separator class="q-my-md" />
          <div class="text-subtitle1 q-mb-sm">{{ t('events.dialog.additionalOptions', 'Additional Options') }}</div>
          <div class="row q-col-gutter-md">
            <div class="col-12 col-md-6">
              <q-select
                v-model="jobData.priority"
                :label="t('events.dialog.priority', 'Priority')"
                :options="priorityOptions"
                emit-value
                map-options
              />
            </div>
            <div class="col-12 col-md-6">
              <q-toggle
                v-model="jobData.is_recurring"
                :label="t('events.dialog.isRecurring', 'Recurring Job')"
                color="primary"
              />
            </div>
            <div class="col-12" v-if="jobData.is_recurring">
              <div class="row q-col-gutter-md">
                <div class="col-12 col-md-6">
                  <q-select
                    v-model="jobData.recurrence_pattern"
                    :label="t('events.dialog.recurrencePattern', 'Recurrence Pattern')"
                    :options="recurrencePatterns"
                    emit-value
                    map-options
                  />
                </div>
                <div class="col-12 col-md-6">
                  <q-input
                    v-model="jobData.recurrence_end_date"
                    :label="t('events.dialog.recurrenceEndDate', 'End Date')"
                    type="date"
                  />
                </div>
              </div>
            </div>
          </div>

          <!-- Action Buttons -->
          <div class="row justify-end q-mt-lg">
            <q-btn 
              :label="t('common.cancel', 'Cancel')" 
              color="negative" 
              v-close-popup 
              class="q-mr-sm" 
            />
            <q-btn 
              :label="t('common.reset', 'Reset')" 
              type="reset" 
              color="warning" 
              @click="handleReset" 
              class="q-mr-sm" 
            />
            <q-btn 
              :label="t('common.submit', 'Submit')" 
              type="submit" 
              color="primary" 
            />
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useQuasar } from 'quasar'

const { t } = useI18n()
const $q = useQuasar()

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'job-added'])

// Computed property for dialog visibility
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// Form data
const jobData = ref({
  branch_id: null,
  store_id: null,
  additional_info: '',
  task_datetime: '',
  start_time: '',
  end_time: '',
  daily_wage: 0,
  priority: 'medium',
  is_recurring: false,
  recurrence_pattern: 'weekly',
  recurrence_end_date: ''
})

// Mock data for dropdowns
const branches = ref([
  { id: 1, name: 'Branch A' },
  { id: 2, name: 'Branch B' },
  { id: 3, name: 'Branch C' }
])

const stores = ref([
  { id: 1, name: 'Store 1' },
  { id: 2, name: 'Store 2' },
  { id: 3, name: 'Store 3' },
  { id: 4, name: 'Store 4' }
])

const priorityOptions = [
  { label: 'High', value: 'high' },
  { label: 'Medium', value: 'medium' },
  { label: 'Low', value: 'low' }
]

const recurrencePatterns = [
  { label: 'Daily', value: 'daily' },
  { label: 'Weekly', value: 'weekly' },
  { label: 'Monthly', value: 'monthly' }
]

// Methods
const onSubmit = async () => {
  try {
    // Simulate API call
    const newJob = {
      id: Math.floor(Math.random() * 1000),
      ...jobData.value
    }
    
    // Emit the new job to parent component
    emit('job-added', newJob)
    
    // Close dialog and reset form
    dialogVisible.value = false
    handleReset()
    
    // Show success notification
    $q.notify({
      type: 'positive',
      message: t('events.dialog.successMessage', 'Job registered successfully')
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('events.dialog.errorMessage', 'Failed to register job')
    })
  }
}

const handleReset = () => {
  jobData.value = {
    branch_id: null,
    store_id: null,
    additional_info: '',
    task_datetime: '',
    start_time: '',
    end_time: '',
    daily_wage: 0,
    priority: 'medium',
    is_recurring: false,
    recurrence_pattern: 'weekly',
    recurrence_end_date: ''
  }
}

const onHide = () => {
  handleReset()
}
</script>

<style scoped>
/* Add any custom styles here */
</style> 