<template>
  <q-dialog v-model="dialogVisible" @hide="onHide" >
    <q-card class="dialog-card large">
      <q-card-section class="dialog-title">
        <div class="text-h6">Register New Task</div>
      </q-card-section>

      <q-card-section class="dialog-content">
        <q-form @submit="onSubmit" class="dialog-form q-gutter-md">
          <div class="row q-col-gutter-md">
            <!-- Title and Shop selection --> 
            <div class="col-12 col-md-6">
              <q-input
                v-model="taskData.title"
                label="Title"
                filled
                required
              />
            </div>
            <div class="col-12 col-md-6">
              <q-select
                v-model="selectedShop"
                label="Store"
                :options="getShopOptions"
                option-value="value"
                option-label="label"
                filled
                required
              />
            </div>

            <!-- Description -->
            <div class="col-12">
              <q-input
                v-model="taskData.description"
                label="Description"
                filled
                type="textarea"
                autogrow
              />
            </div>

            <!-- Worker Count -->
            <div class="col-12 col-md-4">
              <q-input
                v-model.number="taskData.workerCount"
                label="Worker Count"
                filled
                type="number"
                required
              />
            </div>

            <!-- Date and Time -->
            <div class="col-12 col-md-4">
              <q-input
                v-model="startDate"
                label="Start Date"
                filled
                type="date"
                required
                @update:model-value="updateStartDateTime"
              />
            </div>
            <div class="col-12 col-md-4">
              <q-input
                v-model="startTime"
                label="Start Time"
                filled
                type="time"
                required
                @update:model-value="updateStartDateTime"
              />
            </div>
            
            <div class="col-12 col-md-4">
              <q-input
                v-model="endDate"
                label="End Date"
                filled
                type="date"
                required
                @update:model-value="updateEndDateTime"
              />
            </div>
            <div class="col-12 col-md-4">
              <q-input
                v-model="endTime"
                label="End Time"
                filled
                type="time"
                required
                @update:model-value="updateEndDateTime"
              />
            </div>

            <!-- Status -->
            <div class="col-12 col-md-4">
              <q-select
                v-model="taskData.status"
                label="Status"
                :options="statusOptions"
                filled
                required
              />
            </div>

            <!-- Active -->
            <div class="col-12 col-md-4">
              <q-toggle
                v-model="taskData.active"
                label="Active"
              />
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
import { ref, watch, defineProps, defineEmits, computed } from 'vue'
import { useQuasar } from 'quasar'
import { useTeamStore } from '@/stores/modules/store_team'
import { useUserStore } from '@/stores/modules/store_user'
import { storeToRefs } from 'pinia'
import { Task } from '@/api/modules/api-task'

const $q = useQuasar()
const teamStore = useTeamStore()
const userStore = useUserStore()
const { getShopOptions } = storeToRefs(teamStore)

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
  (e: 'submit', task: Task): void
}>()

const dialogVisible = ref(props.modelValue)

// Status options
const statusOptions = ['OPEN', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED'] 

// Shop selection
const selectedShop = ref(null)

// Split date and time for easier input
const startDate = ref(new Date().toISOString().split('T')[0])
const startTime = ref('08:00')
const endDate = ref(new Date().toISOString().split('T')[0])
const endTime = ref('17:00')

// Combine date and time into ISO strings
const updateStartDateTime = () => {
  taskData.value.startDateTime = `${startDate.value}T${startTime.value}:00`
}

const updateEndDateTime = () => {
  taskData.value.endDateTime = `${endDate.value}T${endTime.value}:00`
}

// Main task data
const taskData = ref<Task>({
  title: '',
  description: '',
  workerCount: 1,
  startDateTime: `${startDate.value}T${startTime.value}:00`,
  endDateTime: `${endDate.value}T${endTime.value}:00`,
  status: 'OPEN',
  teamId: userStore.user.teamId || 0,
  shopId: null,
  active: true,
  taskEmployees: null
})

watch(() => props.modelValue, (newValue) => {
  dialogVisible.value = newValue
})

watch(() => dialogVisible.value, (newValue) => {
  emit('update:modelValue', newValue)
  
  // Reset form when dialog is closed
  if (!newValue) {
    handleReset()
  }
})

watch(() => userStore.user.teamId, (newValue) => {
  if (newValue) {
    taskData.value.teamId = newValue
  }
})

// Watch selected shop and update shopId
watch(() => selectedShop.value, (newValue) => {
  if (newValue) {
    taskData.value.shopId = newValue
  } else {
    taskData.value.shopId = null
  }
})

const onSubmit = async () => {
  // Ensure dates are up to date
  updateStartDateTime()
  updateEndDateTime()
  
  emit('submit', taskData.value)
}

const handleReset = () => {
  // Reset date/time values
  startDate.value = new Date().toISOString().split('T')[0]
  startTime.value = '08:00'
  endDate.value = new Date().toISOString().split('T')[0]
  endTime.value = '17:00'
  
  // Reset shop selection
  selectedShop.value = null
  
  // Reset task data
  taskData.value = {    
    title: '',
    description: '',
    workerCount: 1,
    startDateTime: `${startDate.value}T${startTime.value}:00`,
    endDateTime: `${endDate.value}T${endTime.value}:00`,
    status: 'OPEN',
    teamId: userStore.user.teamId || 0,
    shopId: null,
    active: true,
    taskEmployees: null
  }
}

const onHide = () => {
  emit('update:modelValue', false)
} 
</script> 