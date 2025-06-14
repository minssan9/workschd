<template>
  <q-page class="task-list-mobile">
    <!-- Header Section -->
    <div class="header-section q-pa-md q-mb-lg">
      <div class="row items-center justify-between q-mb-md">
        <div class="text-h5 text-weight-bold">Task Management</div>
        <q-btn-toggle
          v-model="viewMode"
          :options="[
            { label: '목록', value: 'list', icon: 'list' },
            { label: '캘린더', value: 'calendar', icon: 'calendar_today' }
          ]"
          color="primary"
          glossy
          unelevated
          rounded
          class="toggle-btn"
        />
      </div>
      
      <div class="filter-section q-mb-lg">
        <q-input 
          v-model="searchQuery" 
          outlined 
          dense 
          placeholder="Search tasks..." 
          class="search-input q-mb-sm"
          bg-color="white"
        >
          <template v-slot:prepend>
            <q-icon name="search" color="primary" />
          </template>
        </q-input>

        <div class="row q-col-gutter-sm">
          <div class="col-12 col-md-auto">
            <q-btn-toggle
              v-model="requestFilter"
              :options="[
                { label: '전체 작업', value: 'all', icon: 'work' },
                { label: '내 신청', value: 'mine', icon: 'person' }
              ]"
              color="secondary"
              rounded
              unelevated
              glossy
              class="full-width"
            />
          </div>
          <div class="col-12 col-md-auto">
            <div class="status-filters row q-col-gutter-x-sm">
              <div v-for="status in statusOptions" :key="status.value" class="col-auto">
                <q-btn
                  :label="status.label"
                  :color="statusFilter === status.value ? 'primary' : 'grey-3'"
                  :text-color="statusFilter === status.value ? 'white' : 'grey-8'"
                  @click="statusFilter = status.value"
                  rounded
                  flat
                  dense
                  class="status-btn"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Calendar View -->
    <div v-if="viewMode === 'calendar'" class="calendar-section q-pa-md">
      <q-card flat bordered class="calendar-card">
        <q-card-section>
          <div class="text-h6 q-mb-md">Weekly Calendar</div>
          <div class="row q-col-gutter-md">
            <div v-for="day in 7" :key="day" class="col">
              <q-card flat bordered class="day-card">
                <q-card-section class="q-pa-sm">
                  <div class="text-subtitle2 text-weight-medium q-mb-sm">Day {{ day }}</div>
                  <div v-for="task in tasksForCalendar(day)" :key="task.id" class="task-chip q-mb-xs">
                    <q-chip
                      dense
                      color="primary"
                      text-color="white"
                      class="full-width"
                    >
                      {{ task.title }}
                    </q-chip>
                  </div>
                </q-card-section>
              </q-card>
            </div>
          </div>
        </q-card-section>
      </q-card>
    </div>

    <!-- List View -->
    <div v-else class="list-section q-px-md">
      <q-list separator class="rounded-borders bg-white">
        <q-item 
          v-for="task in filteredTasksForView" 
          :key="task.id" 
          clickable 
          v-ripple
          @click="showTaskDetails(task)"
          class="task-item q-py-md"
        >
          <q-item-section>
            <div class="row items-center q-mb-sm">
              <div class="col">
                <div class="text-h6 text-weight-medium">{{ task.title }}</div>
              </div>
              <div class="col-auto">
                <q-chip
                  :color="getTaskStatusColor(task.status)"
                  text-color="white"
                  dense
                  class="status-chip"
                >
                  {{ getTaskStatusLabel(task.status) }}
                </q-chip>
              </div>
            </div>

            <q-item-label caption lines="2" class="text-body2 q-mb-sm">
              {{ task.description }}
            </q-item-label>

            <div class="row q-col-gutter-x-md q-mb-sm">
              <div class="col-auto">
                <div class="row items-center text-body2">
                  <q-icon name="event" size="xs" class="q-mr-xs text-primary" />
                  {{ formatDateRange(task.startDateTime, task.endDateTime) }}
                </div>
              </div>
              <div class="col-auto">
                <div class="row items-center text-body2">
                  <q-icon name="schedule" size="xs" class="q-mr-xs text-primary" />
                  {{ formatTimeRange(task.startDateTime, task.endDateTime) }}
                </div>
              </div>
            </div>

            <div class="row q-col-gutter-x-md">
              <div class="col-auto">
                <div class="row items-center text-body2">
                  <q-icon name="store" size="xs" class="q-mr-xs text-primary" />
                  {{ task.shopName || 'N/A' }}
                </div>
              </div>
              <div class="col-auto">
                <div class="row items-center text-body2">
                  <q-icon name="group" size="xs" class="q-mr-xs text-primary" />
                  Team: {{ task.teamName || 'N/A' }}
                </div>
              </div>
              <div class="col-auto">
                <div class="row items-center text-body2">
                  <q-icon name="people" size="xs" class="q-mr-xs text-primary" />
                  {{ task.taskEmployees?.length || 0 }}/{{ task.workerCount }} workers
                </div>
              </div>
            </div>
          </q-item-section>

          <q-item-section side>
            <div class="column items-end">
              <q-btn
                v-if="canRequestToJoin(task)"
                color="primary"
                label="참여 신청"
                rounded
                flat
                dense
                class="q-mb-sm"
                @click.stop="confirmJoinRequest(task)"
              />
              <q-chip
                v-else-if="getTaskRequestStatus(task.id)"
                :color="getRequestStatusColor(getTaskRequestStatus(task.id))"
                text-color="white"
                dense
                class="request-status-chip"
              >
                {{ getRequestStatusLabel(getTaskRequestStatus(task.id)) }}
              </q-chip>
            </div>
          </q-item-section>
        </q-item>
      </q-list>

      <!-- Pagination -->
      <div class="row justify-center q-mt-lg q-mb-md">
        <q-pagination
          v-model="currentPage"
          :max="totalPages"
          direction-links
          boundary-links
          :max-pages="5"
          color="primary"
          rounded
          unelevated
          class="pagination"
        />
      </div>
    </div>

    <!-- Task Dialog -->
    <TaskDialog
      v-model="showTaskDialog"
      :task="selectedTask"
      :is-submitting="isSubmitting"
      :shops="shops"
      @join-request="submitJoinRequest"
      @cancel="submitCancel"
    />
  </q-page>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useQuasar, date } from 'quasar'
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/stores/modules/store_user'
import taskApi from '@/api/modules/api-task'
import apiTeamShop from '@/api/modules/api-team-shop'
import { 
  Task, 
  TaskEmployee,
  JoinRequest,
  Shop
} from '@/types'
import { 
  TaskStatus, 
  RequestStatus,
  getTaskStatusLabel, 
  getTaskStatusColor, 
  getRequestStatusLabel, 
  getRequestStatusColor 
} from '@/types/status'
import TaskDialog from '@/views/task/dialog/TaskDialog.vue'

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
const shops = ref<Shop[]>([])
const isLoading = ref(false)
const isLoadingShops = ref(false)
const searchQuery = ref('')
const statusFilter = ref('')
const userRequests = ref<any[]>([])

// Dialog state
const showTaskDialog = ref(false)
const selectedTask = ref<Task | null>(null)
const isSubmitting = ref(false)

// Status options for filtering
const statusOptions = [
  { label: '예정됨', value: TaskStatus.SCHEDULED },
  { label: '진행중', value: TaskStatus.IN_PROGRESS },
  { label: '완료됨', value: TaskStatus.COMPLETED },
  { label: '취소됨', value: TaskStatus.CANCELLED }
]

// View mode toggle (list/calendar)
const viewMode = ref<'list' | 'calendar'>('list')
// Request filter (all/mine)
const requestFilter = ref<'all' | 'mine'>('all')

// Load tasks and user's task requests
onMounted(async () => {
  updateQueryParams()
  await Promise.all([
    loadTasks(),
    loadShops(),
    user.value?.accountId ? loadUserTaskRequests() : Promise.resolve()
  ])
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

// Load shops for the user's team
async function loadShops() {
  if (!user.value?.teamId) return
  
  isLoadingShops.value = true
  try {
    const response = await apiTeamShop.getShopsByTeamId(user.value.teamId)
    shops.value = response.data
  } catch (error) {
    console.error('Error loading shops:', error)
    $q.notify({ type: 'negative', message: '매장 목록을 불러오는데 실패했습니다.' })
    shops.value = []
  } finally {
    isLoadingShops.value = false
  }
}

// Show task details
function showTaskDetails(task: Task) {
  selectedTask.value = task
  showTaskDialog.value = true
}

// Confirm join request
function confirmJoinRequest(task: Task) {
  selectedTask.value = task
  showTaskDialog.value = true
}

// Submit join request
async function submitJoinRequest(task: Task) {
  if (!task || !task.id || !user.value?.accountId) return

  isSubmitting.value = true
  try {
    const requestData = {
      taskId: task.id,
      accountId: Number(user.value.accountId),
      status: RequestStatus.PENDING
    }

    await taskApi.createTaskEmployeeRequest(requestData)
    $q.notify({ type: 'positive', message: '작업 참여 신청이 완료되었습니다.' })
    showTaskDialog.value = false
    
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
  if (task.status === TaskStatus.COMPLETED || task.status === TaskStatus.CANCELLED) return false

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

// Filtered tasks for current view
const filteredTasksForView = computed(() => {
  if (requestFilter.value === 'mine') {
    // Show only tasks the user has requested
    return tasks.value.filter(task => getTaskRequestStatus(task.id))
  }
  return tasks.value
})

// Calendar stub: return tasks for a given day
function tasksForCalendar(day: number) {
  // Replace with real logic later
  return tasks.value.filter((_, idx) => idx % 7 === (day - 1))
}

// Handle task cancellation
const submitCancel = async (task: Task) => {
  if (!task || !task.id) return
  
  isSubmitting.value = true
  try {
    // For now, just close the dialog - updateTask method doesn't exist in taskApi
    // This would need to be implemented if task cancellation is required
    showTaskDialog.value = false
    $q.notify({ type: 'info', message: '작업 취소 기능은 아직 구현되지 않았습니다.' })
  } catch (error) {
    console.error('Failed to cancel task:', error)
    $q.notify({ type: 'negative', message: '작업 취소에 실패했습니다.' })
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style lang="scss" scoped>
.task-list-mobile {
  background-color: #f5f5f5;
  min-height: 100vh;

  .header-section {
    background: linear-gradient(135deg, var(--q-primary) 0%, darken($primary, 15%) 100%);
    color: white;
    border-radius: 0 0 24px 24px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .toggle-btn {
    background: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);
  }

  .search-input {
    border-radius: 12px;
    .q-field__control {
      height: 44px;
    }
  }

  .status-btn {
    min-width: 100px;
    border-radius: 20px;
    &:hover {
      background: rgba(var(--q-primary), 0.1);
    }
  }

  .task-item {
    border-radius: 12px;
    margin-bottom: 12px;
    transition: all 0.3s ease;
    border: 1px solid rgba(0, 0, 0, 0.05);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    }
  }

  .status-chip {
    font-weight: 500;
    padding: 0 12px;
  }

  .request-status-chip {
    font-weight: 500;
  }

  .calendar-card, .day-card {
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  }

  .pagination {
    .q-btn {
      border-radius: 8px;
    }
  }

  @media (max-width: 599px) {
    .header-section {
      border-radius: 0 0 16px 16px;
    }

    .task-item {
      margin-bottom: 8px;
    }
  }
}
</style> 