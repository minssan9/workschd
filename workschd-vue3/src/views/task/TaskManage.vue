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
                    <div class="detail-value">{{ selectedTask.branch_id }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.shop', 'Shop') }}</div>
                    <div class="detail-value">{{ selectedTask.shop_id }}</div>
                  </div>
                </div>
                <div class="col-12">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.additionalInfo', 'Additional Info') }}</div>
                    <div class="detail-value">{{ selectedTask.additional_info || 'N/A' }}</div>
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
                    <div class="detail-value">{{ formatDate(selectedTask.task_datetime) }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.startTime', 'Start Time') }}</div>
                    <div class="detail-value">{{ selectedTask.start_time }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.endTime', 'End Time') }}</div>
                    <div class="detail-value">{{ selectedTask.end_time }}</div>
                  </div>
                </div>
                <div class="col-12 col-sm-6">
                  <div class="detail-item">
                    <div class="detail-label">{{ t('events.dailyWage', 'Daily Wage') }}</div>
                    <div class="detail-value">{{ formatCurrency(selectedTask.daily_wage) }}</div>
                  </div>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
      
      <!-- Attendance Records Section -->
      <div class="q-mt-md">
        <TaskAttendance 
          v-if="selectedTask" 
          :task-id="selectedTask.id" 
          :branch-id="selectedTask.branch_id"
          :start-time="selectedTask.start_time"
          :end-time="selectedTask.end_time"
          :daily-wage="selectedTask.daily_wage"
        />
      </div>
    </div>

    <!-- Replace the attendance form dialog with the new component -->
    <AttendanceFormDialog
      v-model="showAttendanceForm"
      :initial-data="attendanceForm"
      :task-id="selectedTask?.id"
      :branch-id="selectedTask?.branch_id"
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
import TaskAttendance from '@/views/task/TaskAttendance.vue'
import AttendanceFormDialog from '@/views/task/dialog/AttendanceFormDialog.vue'
import AddTaskDialog from '@/views/task/dialog/AddTaskDialog.vue'
import ApprovalDialog from '@/views/task/dialog/ApprovalTaskDialog.vue'
import apiAttendance from '@/api/modules/api-attendance'
import apiTask, { Task, JoinRequest, AttendanceForm, Shop } from '@/api/modules/api-task'
import { useUserStore } from '@/stores/modules/store_user'

const $q = useQuasar()
const { t } = useI18n()
const userStore = useUserStore()

// Check if user is a worker
const isWorker = computed(() => userStore.isWorker)

const tasks = ref<Task[]>([])
const newTask = ref<Task>({
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

const shops = ref<Shop[]>([])

const rowData = ref([])
const columnDefs = ref([
  { 
    field: 'branch_id', 
    headerName: 'Branch',
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
          calculatedDailyWage: selectedTask.value.daily_wage
        };
      }
    }
  },
  { field: 'shop_id', headerName: 'Shop' },
  { field: 'additional_info', headerName: 'Additional Info' },
  { field: 'task_datetime', headerName: 'Task DateTime' },
  { field: 'start_time', headerName: 'Start Time' },
  { field: 'end_time', headerName: 'End Time' },
  { field: 'daily_wage', headerName: 'Daily Wage' }
])

const showAddDialog = ref(false)
const showAttendanceForm = ref(false)
const approvalDialogVisible = ref(false)
const selectedTaskRequests = ref<JoinRequest[]>([])
const selectedTask = ref<Task | null>(null)
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
        calculatedDailyWage: selectedTask.value.daily_wage
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
    await apiAttendance.create({
      branchId: selectedTask.value.branch_id,
      taskId: selectedTask.value.id,
      actualStartTime: formData.actualStartTime,
      actualEndTime: formData.actualEndTime,
      calculatedDailyWage: formData.calculatedDailyWage,
      employeeId: userStore.user.accountId,
      attendanceDate: new Date().toISOString().split('T')[0],
      dayOfWeek: new Date().toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase(),
      startTime: selectedTask.value.start_time,
      endTime: selectedTask.value.end_time
    });
    
    activeTab.value = 'attendance';
    
    $q.notify({type: 'positive', message: t('attendance.notification.success', 'Attendance recorded successfully')});
  } catch (error) {
    $q.notify({type: 'negative', message: t('attendance.notification.error', 'Failed to record attendance')});
  }
};

const handleApproveRequest = async (request: JoinRequest) => {
  try {
    await apiTask.approveJoinRequest(request.id)
    
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
    branch_id: null,
    shop_id: null,
    additional_info: '',
    task_datetime: '',
    start_time: '',
    end_time: '',
    daily_wage: 0
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

const fetchBranchesAndShops = async () => {
  try {
    const shopsResponse = await apiTask.getActiveShopsByTeamId(userStore.user.teamId)
    
    shops.value = shopsResponse.data
  } catch (error) {
    $q.notify({type: 'negative', message: 'Failed to fetch branches and shops'})
  }
}

onMounted(async () => {
  await Promise.all([loadGridData(), fetchBranchesAndShops()])
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


