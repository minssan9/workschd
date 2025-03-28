<template>
  <q-dialog v-model="dialogVisible" @hide="onHide">
    <q-card style="width: 80vw; max-width: 1000px">
      <q-card-section class="bg-primary text-white">
        <div class="text-h6">Record Attendance</div>
      </q-card-section>

      <q-card-section class="q-pa-md">
        <q-form @submit="onSubmit" class="q-gutter-md">
          <div class="row q-col-gutter-md">
            <!-- First row -->
            <div class="col-12 col-md-6">
              <q-input
                v-model="formData.actualStartTime"
                label="Actual Start Time"
                filled
                type="datetime-local"
                required
              />
            </div>
            <div class="col-12 col-md-6">
              <q-input
                v-model="formData.actualEndTime"
                label="Actual End Time"
                filled
                type="datetime-local"
                required
              />
            </div>

            <!-- Second row -->
            <div class="col-12 col-md-6">
              <q-input
                v-model.number="formData.calculatedDailyWage"
                label="Calculated Daily Wage"
                filled
                type="number"
                prefix="$"
                required
              >
                <template v-slot:append>
                  <q-icon name="attach_money" />
                </template>
              </q-input>
            </div>
          </div>

          <q-separator class="q-my-md" />

          <div class="row justify-end q-mt-md">
            <q-btn label="Cancel" color="negative" flat v-close-popup class="q-mr-sm" />
            <q-btn label="Submit" type="submit" color="primary" unelevated />
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue'
import { useQuasar } from 'quasar'

const $q = useQuasar()

interface AttendanceFormData {
  actualStartTime: string
  actualEndTime: string
  calculatedDailyWage: number
}

const props = defineProps<{
  modelValue: boolean
  initialData: AttendanceFormData
  jobId?: number
  branchId?: number
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'submit', formData: AttendanceFormData): void
}>()

const dialogVisible = ref(props.modelValue)
const formData = ref<AttendanceFormData>({
  actualStartTime: props.initialData.actualStartTime,
  actualEndTime: props.initialData.actualEndTime,
  calculatedDailyWage: props.initialData.calculatedDailyWage
})

// Update dialog visibility when modelValue changes
watch(() => props.modelValue, (newValue) => {
  dialogVisible.value = newValue
})

// Update modelValue when dialog visibility changes
watch(() => dialogVisible.value, (newValue) => {
  emit('update:modelValue', newValue)
})

// Update form data when initialData changes
watch(() => props.initialData, (newValue) => {
  formData.value = { ...newValue }
}, { deep: true })

const onSubmit = () => {
  // Validate form data
  if (!formData.value.actualStartTime || !formData.value.actualEndTime) {
    $q.notify({
      type: 'negative',
      message: 'Please fill in all required fields'
    })
    return
  }

  // Check if end time is after start time
  if (new Date(formData.value.actualEndTime) <= new Date(formData.value.actualStartTime)) {
    $q.notify({
      type: 'negative',
      message: 'End time must be after start time'
    })
    return
  }

  emit('submit', formData.value)
  dialogVisible.value = false
}

const onHide = () => {
  emit('update:modelValue', false)
}
</script>

<style scoped>
.q-card {
  border-radius: 8px;
}

:deep(.q-field) {
  margin-bottom: 8px;
}

:deep(.q-field--filled .q-field__control) {
  border-radius: 8px;
  background-color: #f5f5f5;
}

:deep(.q-field--filled .q-field__control:hover) {
  background-color: #eeeeee;
}

:deep(.q-field--filled.q-field--focused .q-field__control) {
  background-color: #e0e0e0;
}
</style> 