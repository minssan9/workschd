<template>
  <q-page padding class="page-container">
    <div class="row justify-between items-center q-mb-md">
      <h5 class="q-my-none">Task List of Today</h5>
      <q-btn color="primary" label="Add New Task" @click="showAddDialog = true" />
    </div>

    <div class="content-section q-pa-md q-mb-md">
      <GridDefault
        :rowData="rowData"
        :columnDefs="columnDefs"
        :gridOptions="gridOptions"
        @grid-ready="loadGridData"
      />
    </div>

    <!-- Task Details Section -->
    <div v-if="selectedTask" class="content-section q-pa-md q-mt-md">
      <div class="row justify-between items-center q-mb-md">
        <h5 class="q-my-none">{{ t('events.taskDetails', 'Task Details') }}</h5>
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

      <!-- Task Details Cards -->
      <div class="row q-col-gutter-md q-py-md">
        <!-- Basic Info Card -->
        <div class="col-12 col-md-4">
          <q-card class="detail-card">
            <q-card-section>
              <div class="text-h6">{{ t('events.basicInfo', 'Basic Information') }}</div>
              <q-separator class="q-my-sm" />
              
              <div class="row q-col-gutter-sm"> 
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.title', 'Title') }}</div>
                    <div class="detail-value">{{ selectedTask.title }}</div>
                  </div>
                </div>
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.shop', 'Shop') }}</div>
                    <div class="detail-value">{{ selectedTask.shopId }}</div>
                  </div>
                </div>
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.status', 'Status') }}</div>
                    <div class="detail-value">{{ selectedTask.status }}</div>
                  </div>
                </div>
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.active', 'Active') }}</div>
                    <div class="detail-value">{{ selectedTask.active ? 'Yes' : 'No' }}</div>
                  </div>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
        
        <!-- Time & Assignment Card -->
        <div class="col-12 col-md-4">
          <q-card class="detail-card">
            <q-card-section>
              <div class="text-h6">{{ t('events.timeAndAssignment', 'Time & Assignment') }}</div>
              <q-separator class="q-my-sm" />
              
              <div class="row q-col-gutter-sm">
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.startDateTime', 'Start Date & Time') }}</div>
                    <div class="detail-value">{{ formatDateTime(selectedTask.startDateTime) }}</div>
                  </div>
                </div>
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.endDateTime', 'End Date & Time') }}</div>
                    <div class="detail-value">{{ formatDateTime(selectedTask.endDateTime) }}</div>
                  </div>
                </div>
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.workerCount', 'Worker Count') }}</div>
                    <div class="detail-value">{{ selectedTask.workerCount }}</div>
                  </div>
                </div>
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.teamId', 'Team ID') }}</div>
                    <div class="detail-value">{{ selectedTask.teamId || 'N/A' }}</div>
                  </div>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
        
        <!-- Description Card -->
        <div class="col-12 col-md-4">
          <q-card class="detail-card">
            <q-card-section>
              <div class="text-h6">{{ t('events.description', 'Description') }}</div>
              <q-separator class="q-my-sm" />
              
              <div class="detail-item">
                <div class="detail-value description">{{ selectedTask.description || 'N/A' }}</div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
      
      <!-- Task Employees Section -->
      <div class="q-mt-md">
        <div class="row justify-between items-center q-mb-md">
          <h6 class="q-my-none">{{ t('events.taskEmployees', 'Task Employees') }}</h6>
          <q-btn 
            color="primary" 
            label="Add Employee" 
            size="sm"
            @click="showAddEmployeeDialog = true" 
            v-if="!isWorker"
          />
        </div>
        
        <TaskEmployeeGrid 
          v-if="selectedTask && selectedTask.id" 
          :task-id="selectedTask.id"
          @employee-selected="handleEmployeeSelected"
        />
      </div>
    </div>

    <!-- Replace the attendance form dialog with the new component -->
    <AttendanceFormDialog
      v-model="showAttendanceForm"
      :initial-data="attendanceForm"
      :task-id="selectedTask?.id"
      :team-id="selectedTask?.teamId"
      @submit="submitAttendance"
    />

    <!-- Add Task Dialog -->
    <AddTaskDialog
      v-model="showAddDialog" 
      :shops="shops"
      @submit="handleTaskSubmit"
    />

    <!-- Approval Dialog -->
    <ApprovalDialog
      v-model="approvalDialogVisible"
      :requests="selectedTaskRequests"
      @approve="handleApproveRequest"
    />
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useQuasar } from 'quasar'
import { useI18n } from 'vue-i18n'
import GridDefault from '@/components/grid/GridDefault.vue'
import TaskEmployeeGrid from '@/views/task/grid/TaskEmployeeGrid.vue'
import AttendanceFormDialog from '@/views/task/dialog/AttendanceFormDialog.vue'
import AddTaskDialog from '@/views/task/dialog/AddTaskDialog.vue'
import ApprovalDialog from '@/views/task/dialog/ApprovalTaskDialog.vue'

import { useUserStore } from '@/stores/modules/store_user'

import apiAttendance from '@/api/modules/api-attendance'
import apiTask, { Task, TaskEmployee } from '@/api/modules/api-task'
import apiTeamShop from '@/api/modules/api-team-shop'

// Define interfaces that were removed from api-task.ts
interface AttendanceForm {
  actualStartTime: string
  actualEndTime: string
  calculatedDailyWage: number
}

interface CreateAttendanceParams {
  taskId: number | undefined
  actualStartTime: string
  actualEndTime: string
  calculatedDailyWage: number
  employeeId: string | null
  attendanceDate: string
  dayOfWeek: string
  startDateTime: string
  endDateTime: string
  teamId?: number | null
}

interface Shop {
  id?: number
  name: string
  teamId?: number
  region?: string
  active?: boolean
}

const $q = useQuasar()
const { t } = useI18n()
const userStore = useUserStore()

// Check if user is a worker
const isWorker = computed(() => userStore.isWorker)

const tasks = ref<Task[]>([])
const newTask = ref<Task>({
  title: '',
  description: '',
  workerCount: 1,
  startDateTime: new Date().toISOString().split('T')[0] + 'T08:00:00',
  endDateTime: new Date().toISOString().split('T')[0] + 'T17:00:00',
  status: 'OPEN',
  teamId: userStore.user.teamId || 0,
  shopId: null,
  active: true
})

const attendanceForm = ref<AttendanceForm>({
  actualStartTime: '',
  actualEndTime: '',
  calculatedDailyWage: 0
})

const shops = ref<Shop[]>([])

const rowData = ref([])
const columnDefs = ref([
  { 
    field: 'task_id', 
    headerName: 'Task',
    cellRenderer: (params) => {
      return `<div class="clickable-cell">${params.value}</div>`;
    },
    onCellClicked: (params) => {
      selectedTask.value = params.data;
      activeTab.value = 'details';
      
      // Pre-fill attendance form
      if (selectedTask.value) {
        const now = new Date().toISOString().slice(0, 16);
        attendanceForm.value = {
          actualStartTime: now,
          actualEndTime: now,
          calculatedDailyWage: 0 // Default to 0 since daily_wage no longer exists
        };
      }
    }
  },
  { field: 'shopId', headerName: 'Shop' }, 
  { field: 'title', headerName: 'Title' },
  { field: 'description', headerName: 'Description' },
  { field: 'workerCount', headerName: 'Worker Count' },
  { field: 'startDateTime', headerName: 'Start Date & Time' },
  { field: 'endDateTime', headerName: 'End Date & Time' },
  { field: 'status', headerName: 'Status' },
  { field: 'active', headerName: 'Active' }
])

const showAddDialog = ref(false)
const showAttendanceForm = ref(false)
const showAddEmployeeDialog = ref(false)
const approvalDialogVisible = ref(false)
const selectedTaskRequests = ref<TaskEmployee[]>([])
const selectedTask = ref<Task | null>(null)
const selectedEmployee = ref<TaskEmployee | null>(null)
const activeTab = ref('details')

// Grid options with row click handler
const gridOptions = ref({
  onRowClicked: (params) => {
    console.log('Row clicked via gridOptions:', params);
    selectedTask.value = params.data;
    activeTab.value = 'details';
    
    // Pre-fill attendance form with task data
    if (selectedTask.value) {
      const now = new Date().toISOString().slice(0, 16);
      attendanceForm.value = {
        actualStartTime: now,
        actualEndTime: now,
        calculatedDailyWage: 0 // Default to 0 since daily_wage no longer exists
      };
    }
  },
  // Make rows look clickable
  rowStyle: { cursor: 'pointer' }
})

const onSubmit = async () => {
  try {
    await apiTask.createTask(newTask.value)
    await loadGridData()
    handleReset()
    showAddDialog.value = false
    
    $q.notify({type: 'positive', message: 'Task registered successfully'})
  } catch (error) {
    $q.notify({type: 'negative', message: 'Failed to register task'})
  }
}

const submitAttendance = async (formData: AttendanceForm) => {
  if (!selectedTask.value) return;
  
  try {
    const params: CreateAttendanceParams = {
      taskId: selectedTask.value.id,
      actualStartTime: formData.actualStartTime,
      actualEndTime: formData.actualEndTime,
      calculatedDailyWage: formData.calculatedDailyWage,
      employeeId: userStore.user.accountId,
      attendanceDate: new Date().toISOString().split('T')[0],
      dayOfWeek: new Date().toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase(),
      startDateTime: selectedTask.value.startDateTime,
      endDateTime: selectedTask.value.endDateTime,
      teamId: selectedTask.value.teamId
    };
    
    await apiAttendance.create(params);
    
    activeTab.value = 'attendance';
    
    $q.notify({type: 'positive', message: t('attendance.notification.success', 'Attendance recorded successfully')});
  } catch (error) {
    $q.notify({type: 'negative', message: t('attendance.notification.error', 'Failed to record attendance')});
  }
};

const handleEmployeeSelected = (employee: TaskEmployee) => {
  selectedEmployee.value = employee;
};

const handleApproveRequest = async (request: TaskEmployee) => {
  try {
    await apiTask.approveJoinRequest(request)
    
    $q.notify({type: 'positive', message: 'Request approved successfully'})
    
    selectedTaskRequests.value = selectedTaskRequests.value.filter(r => r.id !== request.id)
    
    if (selectedTaskRequests.value.length === 0) {
      approvalDialogVisible.value = false
    }
  } catch (error) {
    $q.notify({type: 'negative', message: 'Failed to approve request'})
  }
}

const handleReset = () => {
  newTask.value = {
    title: '',
    description: '',
    workerCount: 1,
    startDateTime: new Date().toISOString().split('T')[0] + 'T08:00:00',
    endDateTime: new Date().toISOString().split('T')[0] + 'T17:00:00',
    status: 'OPEN',
    teamId: userStore.user.teamId || 0,
    shopId: null,
    active: true
  }
}

const loadGridData = async () => {
  try {
    const response = await apiTask.fetchTasks()
    rowData.value = response.data
  } catch (error) {
    $q.notify({type: 'negative', message: 'Failed to fetch tasks'})
  }
}

const formatDateTime = (dateTimeString: string) => {
  if (!dateTimeString) return 'N/A';
  const date = new Date(dateTimeString);
  return date.toLocaleString();
};

const handleTaskSubmit = async (newTaskData: Task) => {
  try {
    await apiTask.createTask(newTaskData)
    await loadGridData()
    showAddDialog.value = false
    
    $q.notify({type: 'positive', message: 'Task registered successfully'})
  } catch (error) {
    $q.notify({type: 'negative', message: 'Failed to register task'})
  }
}

const fetchShops = async () => {
  try {
    const shopsResponse = await apiTeamShop.getShopsByTeamId(userStore.user.teamId)
    
    shops.value = shopsResponse.data
  } catch (error) {
    $q.notify({type: 'negative', message: 'Failed to fetch branches and shops'})
  }
}

onMounted(async () => {
  await Promise.all([loadGridData(), fetchShops()])
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

.description {
  white-space: pre-line;
  max-height: 150px;
  overflow-y: auto;
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


