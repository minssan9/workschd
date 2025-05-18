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

    <!-- Calendar View Section -->
    <div class="content-section q-pa-md q-mb-md">
      <div class="row items-center q-mb-sm">
        <q-btn-toggle
          v-model="calendarView"
          :options="[
            { label: 'Weekly', value: 'week' },
            { label: 'Monthly', value: 'month' }
          ]"
          color="primary"
          dense
          unelevated
          class="q-mr-md"
        />
        <q-btn color="secondary" label="Auto-Schedule" @click="autoSchedule" flat dense />
        <q-btn color="secondary" label="Shift Templates" @click="showTemplateDialog = true" flat dense class="q-ml-sm" />
      </div>
      <q-banner v-if="hasConflict" class="bg-red-2 text-red-10 q-mb-md">
        <q-icon name="warning" color="red" class="q-mr-sm" />
        Conflict detected: Overlapping shifts or availability violation.
      </q-banner>
      <div class="calendar-container q-mb-md">
        <!-- Stub: Calendar grid, replace with real calendar later -->
        <div class="row q-col-gutter-sm">
          <div v-for="day in calendarDays" :key="day" class="col">
            <q-card flat bordered class="q-pa-sm bg-grey-1">
              <div class="text-caption text-grey-7">{{ day }}</div>
              <div v-for="task in getTasksForDay(day)" :key="task.id" class="q-mt-xs">
                <q-chip
                  color="primary"
                  text-color="white"
                  class="q-mb-xs drag-task"
                  draggable
                  @dragstart="onDragStart(task, day)"
                  @drop.prevent="onDrop(task, day)"
                >
                  {{ task.title }}
                </q-chip>
              </div>
            </q-card>
          </div>
        </div>
      </div>
    </div>

    <!-- Shift Template Dialog (stub) -->
    <q-dialog v-model="showTemplateDialog">
      <q-card style="min-width:300px">
        <q-card-section>
          <div class="text-h6">Select Shift Template</div>
          <q-list>
            <q-item v-for="template in shiftTemplates" :key="template.id" clickable @click="applyTemplate(template)">
              <q-item-section>{{ template.name }}</q-item-section>
            </q-item>
          </q-list>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="Close" v-close-popup />
        </q-card-actions>
      </q-card>
    </q-dialog>
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

// All properties optional for stub/mock compatibility
interface CreateAttendanceParams {
  taskId?: number;
  actualStartTime?: string;
  actualEndTime?: string;
  calculatedDailyWage?: number;
  employeeId?: string | null;
  attendanceDate?: string;
  dayOfWeek?: string;
  startDateTime?: string;
  endDateTime?: string;
  teamId?: number | null;
  branchId?: number | null;
  startTime?: string;
  endTime?: string;
}

// Make id optional for Shop to match sample/mock data
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
    // Add all required fields for CreateAttendanceParams
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
      teamId: selectedTask.value.teamId,
      branchId: null, // stub
      startTime: formData.actualStartTime,
      endTime: formData.actualEndTime
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
    // Fallback: if response.data is not an array, use empty array
    const response = await apiTask.fetchTasks()
    rowData.value = Array.isArray(response.data) ? response.data : []
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

const calendarView = ref<'week' | 'month'>('week')
const showTemplateDialog = ref(false)
const hasConflict = ref(false)
const shiftTemplates = ref([
  { id: 1, name: 'Morning Shift (8am-12pm)' },
  { id: 2, name: 'Afternoon Shift (1pm-5pm)' },
  { id: 3, name: 'Full Day (8am-5pm)' }
])
const calendarDays = computed(() => calendarView.value === 'week'
  ? ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  : Array.from({ length: 30 }, (_, i) => `Day ${i + 1}`)
)
function getTasksForDay(day: string) {
  // Stub: filter tasks by day (replace with real logic)
  return tasks.value.filter((_, idx) => idx % 7 === calendarDays.value.indexOf(day))
}
function onDragStart(task: any, day: string) {
  // Stub: handle drag start
}
function onDrop(task: any, day: string) {
  // Stub: handle drop, set hasConflict if needed
  hasConflict.value = Math.random() > 0.7 // Randomly simulate conflict
}
function autoSchedule() {
  // Stub: auto-scheduling logic
  $q.notify({ type: 'info', message: 'Auto-scheduling (stub)' })
}
function applyTemplate(template: any) {
  // Stub: apply shift template
  $q.notify({ type: 'info', message: `Applied template: ${template.name}` })
  showTemplateDialog.value = false
}

onMounted(async () => {
  loadGridData()
  fetchShops()
  fetchTasks()
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


