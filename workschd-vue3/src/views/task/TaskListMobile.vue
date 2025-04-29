<template>
  <q-page class="q-pa-md">
    <!-- Search and Filter Bar -->
    <div class="q-mb-md">
      <q-input v-model="searchQuery" outlined dense placeholder="작업 검색" class="q-mb-sm">
        <template v-slot:append>
          <q-icon name="search" />
        </template>
      </q-input>
      
      <div class="row q-col-gutter-sm">
        <div class="col-12 text-subtitle1 q-mb-xs">상태 필터</div>
        <div class="col-auto">
          <q-chip
            clickable
            :color="statusFilter === '' ? 'primary' : 'grey-4'"
            :text-color="statusFilter === '' ? 'white' : 'black'"
            @click="statusFilter = ''"
            dense
          >
            전체
          </q-chip>
        </div>
        <div v-for="status in statusOptions" :key="status.value" class="col-auto">
          <q-chip
            clickable
            :color="statusFilter === status.value ? 'primary' : 'grey-4'"
            :text-color="statusFilter === status.value ? 'white' : 'black'"
            @click="statusFilter = status.value"
            dense
          >
            {{ status.label }}
          </q-chip>
        </div>
      </div>
    </div>

    <!-- Tasks List -->
    <div v-if="isLoading" class="column items-center justify-center" style="height: 200px">
      <q-spinner color="primary" size="3em" />
      <div class="q-mt-sm">작업 목록을 불러오는 중...</div>
    </div>
    
    <div v-else-if="filteredTasks.length === 0" class="column items-center justify-center q-pa-lg">
      <q-icon name="work_off" size="3em" color="grey-5" />
      <div class="q-mt-sm text-subtitle1">표시할 작업이 없습니다</div>
    </div>
    
    <div v-else>
      <q-list bordered separator>
        <q-item 
          v-for="task in filteredTasks" 
          :key="task.id" 
          clickable 
          v-ripple
          @click="showTaskDetails(task)"
          class="q-py-md"
        >
          <q-item-section>
            <q-item-label class="text-subtitle1 text-weight-medium">{{ task.title }}</q-item-label>
            <q-item-label caption lines="2">{{ task.description }}</q-item-label>
            <div class="row items-center q-mt-xs">
              <q-icon name="event" size="xs" class="q-mr-xs" />
              <span class="text-caption">{{ formatDateRange(task.startDateTime, task.endDateTime) }}</span>
            </div>
            <div class="row items-center q-mt-xs">
              <q-icon name="accountWorkHour" size="xs" class="q-mr-xs" />
              <span class="text-caption">{{ formatTimeRange(task.startDateTime, task.endDateTime) }}</span>
            </div>
            <div class="row items-center q-mt-xs">
              <q-icon name="group" size="xs" class="q-mr-xs" />
              <span class="text-caption">현재 {{ task.taskEmployees?.length || 0 }}명 / 총 {{ task.workerCount }}명</span>
            </div>
          </q-item-section>
          
          <q-item-section side>
            <q-chip
              :color="getStatusColor(task.status)"
              text-color="white"
              dense
            >
              {{ getStatusLabel(task.status) }}
            </q-chip>
            
            <q-btn
              v-if="canRequestToJoin(task)"
              color="primary" 
              label="참여 신청" 
              size="sm" 
              flat
              class="q-mt-sm"
              @click.stop="confirmJoinRequest(task)"
            />
            
            <q-chip
              v-else-if="getTaskRequestStatus(task.id)"
              :color="getRequestStatusColor(getTaskRequestStatus(task.id))"
              text-color="white"
              dense
              class="q-mt-sm"
            >
              {{ getRequestStatusLabel(getTaskRequestStatus(task.id)) }}
            </q-chip>
          </q-item-section>
        </q-item>
      </q-list>
      
      <!-- Pagination controls -->
      <div class="row justify-center q-mt-md">
        <q-pagination
          v-model="currentPage"
          :max="totalPages"
          direction-links
          boundary-links
          :max-pages="3"
          color="primary"
        />
      </div>
    </div>

    <!-- Task Details Dialog -->
    <TaskDetailsDialog
      v-model="showDetailsDialog"
      :task="selectedTask"
      @join-request="confirmJoinRequest"
      @cancel="confirmCancel"
    />

    <!-- Join Request Confirmation Dialog -->
    <JoinRequestDialog
      v-model="showJoinRequestDialog"
      :task="selectedTask"
      :is-submitting="isSubmitting"
      @submit="submitJoinRequest"
    />

    <!-- Cancel Task Dialog -->
    <CancelTaskDialog
      v-model="showCancelDialog"
      :task="selectedTask"
      :is-submitting="isSubmitting"
      @submit="submitCancel"
    />
  </q-page>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useQuasar, date } from 'quasar'
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/stores/modules/store_user'
import taskApi from '@/api/modules/api-task'
import type { Task, TaskEmployee } from '@/api/modules/api-task'
import TaskDetailsDialog from '@/views/task/dialog/TaskDetailsDialog.vue'
import JoinRequestDialog from '@/views/task/dialog/JoinRequestDialog.vue'
import CancelTaskDialog from '@/views/task/dialog/CancelTaskDialog.vue'

const $q = useQuasar()
const userStore = useUserStore()
const { user } = storeToRefs(userStore)

// Pagination state
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const totalItems = ref(0)

// Query parameters for API calls
const queryParams = ref({
  page: 0,
  size: 10,
  search: undefined as string | undefined,
  status: undefined as string | undefined,
  accountId: undefined as number | undefined
})

// Tasks state
const tasks = ref<Task[]>([])
const isLoading = ref(false)
const searchQuery = ref('')
const statusFilter = ref('')
const userRequests = ref<any[]>([])

// Dialog state
const showDetailsDialog = ref(false)
const showJoinRequestDialog = ref(false)
const selectedTask = ref<Task | null>(null)
const isSubmitting = ref(false)
const showCancelDialog = ref(false)

// Status options for filtering
const statusOptions = [
  { label: '예정됨', value: 'SCHEDULED' },
  { label: '진행중', value: 'IN_PROGRESS' },
  { label: '완료됨', value: 'COMPLETED' },
  { label: '취소됨', value: 'CANCELLED' }
]

// Load tasks and user's task requests
onMounted(async () => {
  updateQueryParams()
  await loadTasks()
  if (user.value?.accountId) {
    await loadUserTaskRequests()
  }
})

// Watch for filter/search changes to update tasks
watch([currentPage, searchQuery, statusFilter], async () => {
  updateQueryParams()
  await loadTasks()
})

// Update query parameters based on current filters and pagination
function updateQueryParams() {
  queryParams.value = {
    page: currentPage.value - 1,
    size: pageSize.value,
    search: searchQuery.value || undefined,
    status: statusFilter.value || undefined,
    accountId: user.value?.accountId ? Number(user.value.accountId) : undefined
  }
}

// Load tasks with pagination and filtering
async function loadTasks() {
  isLoading.value = true
  try {
    const response = await taskApi.fetchTasksForWorker(queryParams.value)
    
    // Use type assertion for response.data
    const data = response.data as any
    
    if (data?.content) {
      // Paginated response
      tasks.value = data.content
      totalItems.value = data.totalElements || 0
      totalPages.value = data.totalPages || 1
    } else {
      // Direct array response
      tasks.value = Array.isArray(data) ? data : []
      totalPages.value = 1
    }
  } catch (error) {
    console.error('Error loading tasks:', error)
    $q.notify({ type: 'negative', message: '작업 목록을 불러오는데 실패했습니다.' })
    tasks.value = []
  } finally {
    isLoading.value = false
  }
}

// Load user's task requests
async function loadUserTaskRequests() {
  try {
    const response = await taskApi.fetchTasksForWorker(queryParams.value)
    
    // Use type assertion for response.data
    const data = response.data as any
    
    if (data?.content) {
      userRequests.value = data.content
    } else {
      userRequests.value = Array.isArray(data) ? data : []
    }
  } catch (error) {
    console.error('Error loading user task requests:', error)
  }
}

// Show task details
function showTaskDetails(task: Task) {
  selectedTask.value = task
  showDetailsDialog.value = true
}

// Confirm join request
function confirmJoinRequest(task: Task) {
  selectedTask.value = task
  showJoinRequestDialog.value = true
}

// Submit join request
async function submitJoinRequest() {
  if (!selectedTask.value || !user.value?.accountId) return

  isSubmitting.value = true
  try {
    const requestData: Partial<TaskEmployee> = {
      taskId: selectedTask.value.id!,
      accountId: Number(user.value.accountId),
      status: 'PENDING'
    }

    await taskApi.createTaskEmployeeRequest(requestData)
    $q.notify({ type: 'positive', message: '작업 참여 신청이 완료되었습니다.' })
    showJoinRequestDialog.value = false
    
    // Reload user's task requests to update UI
    if (user.value?.accountId) {
      await loadUserTaskRequests()
    }
  } catch (error) {
    console.error('Error submitting join request:', error)
    $q.notify({ type: 'negative', message: '작업 참여 신청에 실패했습니다.' })
  } finally {
    isSubmitting.value = false
  }
}

// Check if user can request to join a task
function canRequestToJoin(task: Task | null): boolean {
  if (!task || !task.id || !user.value?.accountId) return false

  // User can't request if task is not scheduled or in progress
  if (task.status == 'COMPLETED' || task.status == 'CANCELLED') return false

  // User can't request if already at capacity
  if (task.taskEmployees && task.taskEmployees.length >= task.workerCount) return false

  // User can't request if already has a request for this task
  return !getTaskRequestStatus(task.id)
}

// Get request status for a task if it exists
function getTaskRequestStatus(taskId: number | undefined): string | null {
  if (!taskId) return null
  const request = userRequests.value.find(req => req.taskId === taskId)
  return request ? request.status : null
}

// Helper function to format date range
function formatDateRange(start?: string, end?: string): string {
  if (!start || !end) return '-'
  
  const startDate = date.formatDate(start, 'YYYY.MM.DD')
  const endDate = date.formatDate(end, 'YYYY.MM.DD')
  
  if (startDate === endDate) {
    return startDate
  }
  return `${startDate} - ${endDate}`
}

// Helper function to format time range
function formatTimeRange(start?: string, end?: string): string {
  if (!start || !end) return '-'
  
  const startTime = date.formatDate(start, 'HH:mm')
  const endTime = date.formatDate(end, 'HH:mm')
  
  return `${startTime} - ${endTime}`
}

// Helper functions for status display
function getStatusLabel(status: string): string {
  const statusMap: Record<string, string> = {
    'SCHEDULED': '예정됨',
    'IN_PROGRESS': '진행중',
    'COMPLETED': '완료됨',
    'CANCELLED': '취소됨'
  }
  return statusMap[status] || status
}

function getStatusColor(status: string): string {
  const colorMap: Record<string, string> = {
    'SCHEDULED': 'blue',
    'IN_PROGRESS': 'green',
    'COMPLETED': 'purple',
    'CANCELLED': 'grey'
  }
  return colorMap[status] || 'grey'
}

function getRequestStatusLabel(status: string | null): string {
  if (!status) return ''
  
  const statusMap: Record<string, string> = {
    'PENDING': '승인 대기중',
    'APPROVED': '승인됨',
    'REJECTED': '거절됨',
    'ACTIVE': '참여 중',
    'INACTIVE': '참여 종료'
  }
  return statusMap[status] || status
}

function getRequestStatusColor(status: string | null): string {
  if (!status) return 'grey'
  
  const colorMap: Record<string, string> = {
    'PENDING': 'orange',
    'APPROVED': 'green',
    'REJECTED': 'red',
    'ACTIVE': 'teal',
    'INACTIVE': 'grey'
  }
  return colorMap[status] || 'grey'
}

// Computed property for filtered tasks
const filteredTasks = computed(() => {
  return tasks.value
})

const confirmCancel = (task: Task) => {
  showDetailsDialog.value = false
  showCancelDialog.value = true
}

const submitCancel = async () => {
  if (!selectedTask.value) return
  
  isSubmitting.value = true
  try {
    await taskApi.cancelTask(selectedTask.value.id)
    showCancelDialog.value = false
    await fetchTasks()
  } catch (error) {
    console.error('Failed to cancel task:', error)
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style scoped>
/* Responsive adjustments */
@media (max-width: 599px) {
  .q-page {
    padding: 12px !important;
  }
}
</style> 