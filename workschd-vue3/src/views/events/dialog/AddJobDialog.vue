<template>
  <q-dialog v-model="dialogVisible" @hide="onHide" >
    <q-card style="width: 80vw; max-width: 1000px">
      <q-card-section class="bg-primary text-white">
        <div class="text-h6">Register New Job</div>
      </q-card-section>

      <q-card-section class="q-pa-md">
        <q-form @submit="onSubmit" class="q-gutter-md">
          <div class="row q-col-gutter-md">
            <!-- First row -->
            <div class="col-12 col-md-6">
              <q-select
                v-model="jobData.branch_id"
                label="Branch"
                :options="branches"
                option-value="id"
                option-label="name"
                filled
                required
              />
            </div>
            <div class="col-12 col-md-6">
              <q-select
                v-model="jobData.store_id"
                label="Store"
                :options="stores"
                option-value="id"
                option-label="name"
                filled
                required
              />
            </div>

            <!-- Second row -->
            <div class="col-12">
              <q-input
                v-model="jobData.additional_info"
                label="Additional Info"
                filled
                type="textarea"
                autogrow
              />
            </div>

            <!-- Third row -->
            <div class="col-12 col-md-4">
              <q-input
                v-model="jobData.task_datetime"
                label="Task Date"
                filled
                type="date"
                required
              />
            </div>
            <div class="col-12 col-md-4">
              <q-input
                v-model="jobData.start_time"
                label="Start Time"
                filled
                type="time"
                required
              />
            </div>
            <div class="col-12 col-md-4">
              <q-input
                v-model="jobData.end_time"
                label="End Time"
                filled
                type="time"
                required
              />
            </div>

            <!-- Fourth row -->
            <div class="col-12 col-md-6">
              <q-input
                v-model.number="jobData.daily_wage"
                label="Daily Wage"
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
            <q-btn label="Reset" type="reset" color="warning" flat @click="handleReset" class="q-mr-sm" />
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

interface NewJob {
  branch_id: number | null
  store_id: number | null
  additional_info: string
  task_datetime: string
  start_time: string
  end_time: string
  daily_wage: number
}

interface Branch {
  id: number
  name: string
}

interface Store {
  id: number
  name: string
}

const props = defineProps<{
  modelValue: boolean
  branches: Branch[]
  stores: Store[]
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'submit', job: NewJob): void
}>()

const dialogVisible = ref(props.modelValue)
const jobData = ref<NewJob>({
  branch_id: null,
  store_id: null,
  additional_info: '',
  task_datetime: '',
  start_time: '',
  end_time: '',
  daily_wage: 0
})

watch(() => props.modelValue, (newValue) => {
  dialogVisible.value = newValue
})

watch(() => dialogVisible.value, (newValue) => {
  emit('update:modelValue', newValue)
})

const onSubmit = async () => {
  emit('submit', jobData.value)
}

const handleReset = () => {
  jobData.value = {
    branch_id: null,
    store_id: null,
    additional_info: '',
    task_datetime: '',
    start_time: '',
    end_time: '',
    daily_wage: 0
  }
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