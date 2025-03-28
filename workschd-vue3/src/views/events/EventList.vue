<template>
  <q-page padding class="page-container">
    <div class="row justify-between items-center q-mb-md">
      <h5 class="q-my-none">Job List of Today</h5>
      <q-btn color="primary" label="Add New Job" @click="showAddDialog = true" />
    </div>

    <div class="content-section q-pa-md q-mb-md">
      <GridDefault
        :rowData="rowData"
        :columnDefs="columnDefs"
        :gridOptions="gridOptions"
        @grid-ready="loadGridData"
      />
    </div>

    <!-- Job Details Section -->
    <div v-if="selectedJob" class="content-section q-pa-md q-mt-md">
      <div class="row justify-between items-center q-mb-md">
        <h5 class="q-my-none">{{ t('events.jobDetails', 'Job Details') }}</h5>
        <div>
          <q-btn 
            v-if="isWorker"
            color="primary" 
            :label="t('events.attend', 'Attend')" 
            @click="showAttendanceForm = true"
            class="q-mr-sm"
          /> 
        </div>
      </div>

      <q-separator />

      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-6">
          <q-card class="detail-card">
            <q-card-section>
              <div class="text-h6">{{ t('events.basicInfo', 'Basic Information') }}</div>
              <q-separator class="q-my-sm" />
              
              <div class="row q-col-gutter-md">
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.branch', 'Branch') }}</div>
                    <div class="detail-value">{{ selectedJob.branch_id }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.store', 'Store') }}</div>
                    <div class="detail-value">{{ selectedJob.store_id }}</div>
                  </div>
                </div>
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.additionalInfo', 'Additional Info') }}</div>
                    <div class="detail-value">{{ selectedJob.additional_info || 'N/A' }}</div>
                  </div>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
        
        <div class="col-12 col-md-6">
          <q-card class="detail-card">
            <q-card-section>
              <div class="text-h6">{{ t('events.timeAndWage', 'Time & Wage') }}</div>
              <q-separator class="q-my-sm" />
              
              <div class="row q-col-gutter-md">
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.taskDate', 'Task Date') }}</div>
                    <div class="detail-value">{{ formatDate(selectedJob.task_datetime) }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.startTime', 'Start Time') }}</div>
                    <div class="detail-value">{{ selectedJob.start_time }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.endTime', 'End Time') }}</div>
                    <div class="detail-value">{{ selectedJob.end_time }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.dailyWage', 'Daily Wage') }}</div>
                    <div class="detail-value">{{ formatCurrency(selectedJob.daily_wage) }}</div>
                  </div>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
      
      <!-- Attendance Records Section -->
      <div class="q-mt-md">
        <EventAttendance 
          v-if="selectedJob" 
          :job-id="selectedJob.id" 
          :branch-id="selectedJob.branch_id"
          :task-id="selectedJob.id"
          :start-time="selectedJob.start_time"
          :end-time="selectedJob.end_time"
          :daily-wage="selectedJob.daily_wage"
        />
      </div>
    </div>

    <!-- Replace the attendance form dialog with the new component -->
    <AttendanceFormDialog
      v-model="showAttendanceForm"
      :initial-data="attendanceForm"
      :job-id="selectedJob?.id"
      :branch-id="selectedJob?.branch_id"
      @submit="submitAttendance"
    />

    <!-- Add Job Dialog -->
    <AddJobDialog
      v-model="showAddDialog"
      :branches="branches"
      :stores="stores"
      @submit="handleJobSubmit"
    />

    <!-- Approval Dialog -->
    <ApprovalDialog
      v-model="approvalDialogVisible"
      :requests="selectedJobRequests"
      @approve="handleApproveRequest"
    />
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useQuasar } from 'quasar'
import { useI18n } from 'vue-i18n'
import GridDefault from '@/components/grid/GridDefault.vue'
import EventAttendance from '@/views/events/EventAttendance.vue'
import AttendanceFormDialog from '@/views/events/dialog/AttendanceFormDialog.vue'
import AddJobDialog from '@/views/events/dialog/AddJobDialog.vue'
import ApprovalDialog from '@/views/events/dialog/ApprovalDialog.vue'
import apiAttendance from '@/api/modules/api-attendance'
import { useUserStore } from '@/stores/modules/store_user'

const $q = useQuasar()
const { t } = useI18n()
const userStore = useUserStore()

// Check if user is a worker
const isWorker = computed(() => userStore.isWorker)

interface Job {
  id: number
  branch_id: number
  store_id: number
  additional_info: string
  task_datetime: string
  start_time: string
  end_time: string
  daily_wage: number
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

interface JoinRequest {
  id: number
  workerName: string
}

interface AttendanceForm {
  actualStartTime: string
  actualEndTime: string
  calculatedDailyWage: number
}

const jobs = ref<Job[]>([
  { 
    id: 1, 
    branch_id: 1, 
    store_id: 1, 
    additional_info: 'Regular shift', 
    task_datetime: '2023-05-15T09:00', 
    start_time: '09:00', 
    end_time: '17:00',
    daily_wage: 120
  },
  { 
    id: 2, 
    branch_id: 2, 
    store_id: 2, 
    additional_info: 'Inventory check', 
    task_datetime: '2023-05-16T10:00', 
    start_time: '10:00', 
    end_time: '18:00',
    daily_wage: 130
  },
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

const attendanceForm = ref<AttendanceForm>({
  actualStartTime: '',
  actualEndTime: '',
  calculatedDailyWage: 0
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
  { 
    field: 'branch_id', 
    headerName: 'Branch',
    cellRenderer: (params) => {
      return `<div class="clickable-cell">${params.value}</div>`;
    },
    onCellClicked: (params) => {
      selectedJob.value = params.data;
      activeTab.value = 'details';
      
      // Pre-fill attendance form
      if (selectedJob.value) {
        const now = new Date().toISOString().slice(0, 16);
        attendanceForm.value = {
          actualStartTime: now,
          actualEndTime: now,
          calculatedDailyWage: selectedJob.value.daily_wage
        };
      }
    }
  },
  { field: 'store_id', headerName: 'Store' },
  { field: 'additional_info', headerName: 'Additional Info' },
  { field: 'task_datetime', headerName: 'Task DateTime' },
  { field: 'start_time', headerName: 'Start Time' },
  { field: 'end_time', headerName: 'End Time' },
  { field: 'daily_wage', headerName: 'Daily Wage' }
])

const showAddDialog = ref(false)
const showAttendanceForm = ref(false)
const approvalDialogVisible = ref(false)
const selectedJobRequests = ref<JoinRequest[]>([])
const selectedJob = ref<Job | null>(null)
const activeTab = ref('details')

// Grid options with row click handler
const gridOptions = ref({
  onRowClicked: (params) => {
    console.log('Row clicked via gridOptions:', params);
    selectedJob.value = params.data;
    activeTab.value = 'details';
    
    // Pre-fill attendance form with job data
    if (selectedJob.value) {
      const now = new Date().toISOString().slice(0, 16);
      attendanceForm.value = {
        actualStartTime: now,
        actualEndTime: now,
        calculatedDailyWage: selectedJob.value.daily_wage
      };
    }
  },
  // Make rows look clickable
  rowStyle: { cursor: 'pointer' }
})


const onSubmit = async () => {
  try {
    await fetch('/tasks', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newJob.value)
    })

    await loadGridData()
    handleReset()
    showAddDialog.value = false
    
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

const submitAttendance = async (formData) => {
  if (!selectedJob.value) return;
  
  try {
    await apiAttendance.create({
      branchId: selectedJob.value.branch_id,
      taskId: selectedJob.value.id,
      actualStartTime: formData.actualStartTime,
      actualEndTime: formData.actualEndTime,
      calculatedDailyWage: formData.calculatedDailyWage,
      employeeId: 1, // Assuming current user ID or get from store
      attendanceDate: new Date().toISOString().split('T')[0],
      dayOfWeek: new Date().toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase(),
      startTime: selectedJob.value.start_time,
      endTime: selectedJob.value.end_time
    });
    
    activeTab.value = 'attendance';
    
    $q.notify({
      type: 'positive',
      message: t('attendance.notification.success', 'Attendance recorded successfully')
    });
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('attendance.notification.error', 'Failed to record attendance')
    });
  }
};

const handleApproveRequest = async (request: JoinRequest) => {
  try {
    await fetch(`/requests/${request.id}/approve`, {
      method: 'POST'
    })
    
    $q.notify({
      type: 'positive',
      message: 'Request approved successfully'
    })
    
    selectedJobRequests.value = selectedJobRequests.value.filter(r => r.id !== request.id)
    
    if (selectedJobRequests.value.length === 0) {
      approvalDialogVisible.value = false
    }
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to approve request'
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

const loadGridData = async () => {
  try {
    // For demo purposes, use the jobs array
    rowData.value = jobs.value;
    
    // In a real app, uncomment this:
    /*
    const response = await fetch('/tasks')
    const data = await response.json()
    rowData.value = data
    */
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to fetch tasks'
    })
  }
}

// Helper functions
const formatDate = (dateString: string) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString();
};

const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(amount);
};

const handleJobSubmit = async (newJobData: NewJob) => {
  try {
    await fetch('/tasks', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(newJobData)
    })

    await loadGridData()
    showAddDialog.value = false
    
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

onMounted(() => {
  loadGridData()
})
</script>

<style scoped>
.detail-card {
  height: 100%;
  transition: all 0.3s ease;
}

.detail-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.detail-item {
  margin-bottom: 12px;
}

.detail-label {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 4px;
}

.detail-value {
  font-size: 1rem;
  font-weight: 500;
}

.clickable-cell {
  cursor: pointer;
  color: var(--q-primary);
  text-decoration: underline;
}

/* Make the form elements more visually appealing */
:deep(.q-field) {
  margin-bottom: 16px;
}

:deep(.q-field__label) {
  font-weight: 500;
}

:deep(.q-field--filled .q-field__control) {
  border-radius: 8px;
}

:deep(.q-btn) {
  border-radius: 8px;
  padding: 8px 24px;
}
</style>


