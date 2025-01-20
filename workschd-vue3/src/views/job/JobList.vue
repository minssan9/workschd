<template>
  <q-page padding>
    <h5>Job List of Today</h5>

    <q-list bordered separator>
      <q-item v-for="job in jobs" :key="job.id" clickable v-ripple>
        <q-item-section>
          <q-item-label>{{ job.place }}</q-item-label>
          <q-item-label caption>
            Slots: {{ job.slots }} |
            Time: {{ job.startTime }} - {{ job.endTime }}
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-list>

    <h5 class="q-mt-lg">Register New Job</h5>

    <q-form @submit="onSubmit" class="q-gutter-md">
      <q-select
        v-model="newJob.branch_id"
        label="Branch"
        :options="branches"
        option-value="id"
        option-label="name"
        required
      />
      <q-select
        v-model="newJob.store_id"
        label="Store"
        :options="stores"
        option-value="id"
        option-label="name"
        required
      />
      <q-input
        v-model="newJob.additional_info"
        label="Additional Info"
      />
      <q-input
        v-model="newJob.task_datetime"
        label="Task DateTime"
        type="datetime-local"
        required
      />
      <q-input
        v-model="newJob.start_time"
        label="Start Time"
        type="time"
        required
      />
      <q-input
        v-model="newJob.end_time"
        label="End Time"
        type="time"
        required
      />
      <q-input
        v-model.number="newJob.daily_wage"
        label="Daily Wage"
        type="number"
        required
      />
      <div class="q-mt-md">
        <q-btn label="Submit" type="submit" color="primary" class="q-mr-sm" />
        <q-btn label="Reset" type="reset" color="secondary" @click="handleReset" />
      </div>
    </q-form>

    <div class="q-mt-lg">
      <ag-grid-vue
        class="ag-theme-alpine"
        style="width: 100%; height: 400px;"
        :rowData="rowData"
        :columnDefs="columnDefs"
        @grid-ready="onGridReady"
      />
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { AgGridVue } from 'ag-grid-vue3'
import { useQuasar } from 'quasar'

const $q = useQuasar()

interface Job {
  id: number
  place: string
  slots: number
  startTime: string
  endTime: string
}

interface NewJob {
  branch_id: number | null
  store_id: number | null
  additional_info: string
  task_datetime: string
  start_time: string
  end_time: string
  daily_wage: number
}

const jobs = ref<Job[]>([
  { id: 1, place: 'Office A', slots: 5, startTime: '09:00', endTime: '17:00' },
  { id: 2, place: 'Store B', slots: 3, startTime: '10:00', endTime: '18:00' },
])

const newJob = ref<NewJob>({
  branch_id: null,
  store_id: null,
  additional_info: '',
  task_datetime: '',
  start_time: '',
  end_time: '',
  daily_wage: 0
})

const branches = ref([
  { id: 1, name: 'Branch 1' },
  { id: 2, name: 'Branch 2' }
])

const stores = ref([
  { id: 1, name: 'Store 1' },
  { id: 2, name: 'Store 2' }
])

const rowData = ref([])
const columnDefs = ref([
  { headerName: 'Branch', field: 'branch_id' },
  { headerName: 'Store', field: 'store_id' },
  { headerName: 'Additional Info', field: 'additional_info' },
  { headerName: 'Task DateTime', field: 'task_datetime' },
  { headerName: 'Start Time', field: 'start_time' },
  { headerName: 'End Time', field: 'end_time' },
  { headerName: 'Daily Wage', field: 'daily_wage' }
])

const onGridReady = async () => {
  try {
    const response = await fetch('/api/tasks')
    const data = await response.json()
    rowData.value = data
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to fetch tasks'
    })
  }
}

const onSubmit = async () => {
  try {
    await fetch('/api/tasks', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newJob.value)
    })

    await onGridReady()
    handleReset()
    
    $q.notify({
      type: 'positive',
      message: 'Job registered successfully'
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to register job'
    })
  }
}

const handleReset = () => {
  newJob.value = {
    branch_id: null,
    store_id: null,
    additional_info: '',
    task_datetime: '',
    start_time: '',
    end_time: '',
    daily_wage: 0
  }
}
</script>

<style scoped>
.q-page {
  max-width: 1200px;
  margin: auto;
}
</style>


