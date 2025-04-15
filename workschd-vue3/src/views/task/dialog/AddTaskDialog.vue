<template>
  <q-dialog v-model="dialogVisible" @hide="onHide" >
    <q-card class="dialog-card large">
      <q-card-section class="dialog-title">
        <div class="text-h6">Register New Task</div>
      </q-card-section>

      <q-card-section class="dialog-content">
        <q-form @submit="onSubmit" class="dialog-form q-gutter-md">
          <div class="row q-col-gutter-md">
            <!-- First row --> 
            <div class="col-12 col-md-6">
              <q-select
                v-model="taskData.shop_id"
                label="Store"
                :options="getShopOptions"
                option-value="id"
                option-label="name"
                filled
                required
              />
            </div>

            <!-- Second row -->
            <div class="col-12">
              <q-input
                v-model="taskData.additional_info"
                label="Additional Info"
                filled
                type="textarea"
                autogrow
              />
            </div>

            <!-- Third row -->
            <div class="col-12 col-md-4">
              <q-input
                v-model="taskData.task_datetime"
                label="Task Date"
                filled
                type="date"
                required
              />
            </div>
            <div class="col-12 col-md-4">
              <q-input
                v-model="taskData.start_time"
                label="Start Time"
                filled
                type="time"
                required
              />
            </div>
            <div class="col-12 col-md-4">
              <q-input
                v-model="taskData.end_time"
                label="End Time"
                filled
                type="time"
                required
              />
            </div>

            <!-- Fourth row -->
            <div class="col-12 col-md-6">
              <q-input
                v-model.number="taskData.daily_wage"
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
import { useTeamStore } from '@/stores/modules/store_team'
import { storeToRefs } from 'pinia'

const $q = useQuasar()
const teamStore = useTeamStore()
const { getShopOptions } = storeToRefs(teamStore)

interface NewTask {
  
  shop_id: number | null
  additional_info: string
  task_datetime: string
  start_time: string
  end_time: string
  daily_wage: number
}

interface Shop {
  id: number
  name: string
}

const props = defineProps<{
  modelValue: boolean
  shops: Shop[]
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'submit', task: NewTask): void
}>()

const dialogVisible = ref(props.modelValue) 

const taskData = ref<NewTask>({
  shop_id: null,
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
  emit('submit', taskData.value)
}

const handleReset = () => {
  taskData.value = {    
    shop_id: null,
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