<template>
  <q-page padding class="page-container">  
    <!-- Header Section -->
    <div class="row justify-between items-center q-mb-md">
      <div class="col-12 col-sm-auto">
        <h5 class="q-my-none">근태 관리</h5>
      </div>
      <div class="col-12 col-sm-auto q-mt-sm q-mt-sm-none">
        <q-btn-group flat>
          <q-btn 
            v-if="isAdmin"
            color="primary" 
            icon="add"
            label="근태 등록" 
            @click="showManageDialog = true"
            class="q-mr-sm"
          />
          <q-btn 
            color="secondary" 
            icon="file_download"
            label="엑셀 다운로드" 
            @click="downloadExcel"
          />
        </q-btn-group>
      </div>
    </div>

    <!-- Filter Section -->
    <div class="row q-col-gutter-md q-mb-md">
      <div class="col-12 col-sm-4">
        <q-input
          v-model="filters.search"
          outlined
          dense
          placeholder="이름으로 검색"
          clearable
          @update:model-value="onFilterChange"
        >
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </div>
      <div class="col-12 col-sm-4">
        <q-select
          v-model="filters.status"
          :options="statusOptions"
          outlined
          dense
          emit-value
          map-options
          placeholder="상태 필터"
          clearable
          @update:model-value="onFilterChange"
        />
      </div>
      <div class="col-12 col-sm-4">
        <q-input
          v-model="filters.date"
          outlined
          dense
          placeholder="날짜 선택"
          mask="####-##-##"
          clearable
          @update:model-value="onFilterChange"
        >
          <template v-slot:append>
            <q-icon name="event" class="cursor-pointer">
              <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                <q-date v-model="filters.date" mask="YYYY-MM-DD" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
      </div>
    </div>

    <!-- Content Section -->
    <div class="content-section q-pa-md q-mb-md">
      <div v-if="loading" class="text-center q-pa-lg">
        <q-spinner color="primary" size="3em" />
        <div class="q-mt-sm">근태 기록을 불러오는 중...</div>
      </div>

      <div v-else-if="filteredRecords.length === 0" class="text-center q-pa-lg">
        <q-icon name="event_busy" size="4em" color="grey-6" />
        <div class="text-h6 q-mt-md text-grey-8">근태 기록 없음</div>
        <p class="text-grey-7">해당 작업에 대한 근태 기록이 없습니다.</p>
      </div>

      <div v-else>
        <!-- Desktop View -->
        <q-table
          :rows="filteredRecords"
          :columns="columns"
          row-key="id"
          :pagination="pagination"
          :loading="loading"
          class="q-mt-md desktop-only"
          :filter="filters.search"
          @request="onRequest"
        >
          <template v-slot:body-cell-status="props">
            <q-td :props="props">
              <q-chip :color="getStatusColor(props.value)" text-color="white">
                {{ getStatusLabel(props.value) }}
              </q-chip>
            </q-td>
          </template>
          
          <template v-slot:body-cell-actions="props">
            <q-td :props="props">
              <q-btn-group flat>
                <q-btn flat round color="primary" icon="visibility" @click="viewDetails(props.row)">
                  <q-tooltip>상세 보기</q-tooltip>
                </q-btn>
                <q-btn v-if="isAdmin" flat round color="warning" icon="edit" @click="editRecord(props.row)">
                  <q-tooltip>수정</q-tooltip>
                </q-btn>
                <q-btn v-if="isAdmin" flat round color="negative" icon="delete" @click="confirmDelete(props.row)">
                  <q-tooltip>삭제</q-tooltip>
                </q-btn>
              </q-btn-group>
            </q-td>
          </template>

          <template v-slot:bottom="props">
            <div class="row items-center justify-between full-width">
              <div class="col-auto">
                <span class="text-body2">
                  총 {{ totalRecords }} 건
                </span>
              </div>
              <div class="col-auto">
                <q-pagination
                  v-model="pagination.page"
                  :max="Math.ceil(totalRecords / pagination.rowsPerPage)"
                  :max-pages="5"
                  boundary-links
                  direction-links
                  @update:model-value="onRequest"
                />
              </div>
            </div>
          </template>
        </q-table>

        <!-- Mobile View -->
        <div class="mobile-only">
          <q-list separator>
            <q-item v-for="record in filteredRecords" :key="record.id" class="q-py-md">
              <q-item-section>
                <q-item-label class="text-subtitle1">{{ record.employeeName }}</q-item-label>
                <q-item-label caption>
                  <div class="row q-gutter-x-md">
                    <div>
                      <q-icon name="schedule" size="xs" />
                      {{ formatTime(record.actualStartTime) }} - {{ formatTime(record.actualEndTime) }}
                    </div>
                    <div>
                      <q-icon name="payments" size="xs" />
                      {{ formatWage(record.calculatedDailyWage) }}
                    </div>
                  </div>
                </q-item-label>
                <q-item-label>
                  <q-chip :color="getStatusColor(record.status)" text-color="white" dense>
                    {{ getStatusLabel(record.status) }}
                  </q-chip>
                </q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn-group vertical flat>
                  <q-btn flat round color="primary" icon="visibility" @click="viewDetails(record)" />
                  <q-btn v-if="isAdmin" flat round color="warning" icon="edit" @click="editRecord(record)" />
                  <q-btn v-if="isAdmin" flat round color="negative" icon="delete" @click="confirmDelete(record)" />
                </q-btn-group>
              </q-item-section>
            </q-item>
          </q-list>
          
          <!-- Mobile Pagination -->
          <div class="row justify-center q-mt-md">
            <q-pagination
              v-model="pagination.page"
              :max="Math.ceil(totalRecords / pagination.rowsPerPage)"
              :max-pages="3"
              boundary-links
              direction-links
              @update:model-value="onRequest"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Manage Attendance Dialog -->
    <q-dialog v-model="showManageDialog" persistent>
      <q-card style="min-width: 350px">
        <q-card-section class="row items-center">
          <div class="text-h6">근태 등록</div>
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-form @submit="onSubmitAttendance" class="q-gutter-md">
            <q-select
              v-model="newRecord.employeeId"
              :options="employeeOptions"
              label="직원 *"
              outlined
              dense
              emit-value
              map-options
              :rules="[val => !!val || '직원을 선택해주세요']"
            />

            <div class="row q-col-gutter-sm">
              <div class="col-12 col-sm-6">
                <q-input
                  v-model="newRecord.actualStartTime"
                  label="시작 시간 *"
                  outlined
                  dense
                  mask="##:##"
                  :rules="[val => !!val || '시작 시간을 입력해주세요']"
                >
                  <template v-slot:append>
                    <q-icon name="access_time" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-time v-model="newRecord.actualStartTime" mask="HH:mm" format24h />
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
              <div class="col-12 col-sm-6">
                <q-input
                  v-model="newRecord.actualEndTime"
                  label="종료 시간 *"
                  outlined
                  dense
                  mask="##:##"
                  :rules="[val => !!val || '종료 시간을 입력해주세요']"
                >
                  <template v-slot:append>
                    <q-icon name="access_time" class="cursor-pointer">
                      <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                        <q-time v-model="newRecord.actualEndTime" mask="HH:mm" format24h />
                      </q-popup-proxy>
                    </q-icon>
                  </template>
                </q-input>
              </div>
            </div>

            <q-select
              v-model="newRecord.status"
              :options="statusOptions"
              label="상태 *"
              outlined
              dense
              emit-value
              map-options
              :rules="[val => !!val || '상태를 선택해주세요']"
            />

            <q-input
              v-model.number="newRecord.calculatedDailyWage"
              label="일당"
              type="number"
              outlined
              dense
              prefix="₩"
              :rules="[
                val => val >= 0 || '0 이상의 금액을 입력해주세요',
              ]"
            />

            <q-input
              v-model="newRecord.remarks"
              label="비고"
              type="textarea"
              outlined
              dense
            />

            <div class="row justify-end q-gutter-sm">
              <q-btn label="취소" color="grey" v-close-popup />
              <q-btn label="저장" type="submit" color="primary" :loading="submitting" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>

    <!-- Delete Confirmation Dialog -->
    <q-dialog v-model="showDeleteDialog" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="warning" color="warning" text-color="white" />
          <span class="q-ml-sm">정말 이 근태 기록을 삭제하시겠습니까?</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="취소" color="primary" v-close-popup />
          <q-btn flat label="삭제" color="negative" @click="deleteRecord" :loading="submitting" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useQuasar, date } from 'quasar'
import { useUserStore } from '@/stores/modules/store_user'
import apiAttendance from '@/api/modules/api-attendance'
import { AttendanceRecord } from '@/api/modules/api-task'

const $q = useQuasar()
const userStore = useUserStore()

// Check if user is an admin
const isAdmin = computed(() => userStore.isAdmin)

const props = defineProps<{
  taskId: number
  branchId: number
  startTime: string
  endTime: string
  dailyWage: number
}>()

// State
const loading = ref(true)
const submitting = ref(false)
const showManageDialog = ref(false)
const showDeleteDialog = ref(false)
const attendanceRecords = ref<AttendanceRecord[]>([])
const selectedRecord = ref<AttendanceRecord | null>(null)
const totalRecords = ref(0)

// Filters
const filters = ref({
  search: '',
  status: null as string | null,
  date: null as string | null
})

// Pagination
const pagination = ref({
  page: 1,
  rowsPerPage: 10
})

// Status options
const statusOptions = [
  { label: '정상', value: 'COMPLETED' },
  { label: '대기', value: 'PENDING' },
  { label: '결근', value: 'ABSENT' },
  { label: '지각', value: 'LATE' },
  { label: '조퇴', value: 'EARLY_LEAVE' }
]

// Mock employee options (replace with API call)
const employeeOptions = [
  { label: '홍길동', value: 1 },
  { label: '김철수', value: 2 },
  { label: '이영희', value: 3 }
]

// Table columns
const columns = [
  { 
    name: 'employeeName', 
    align: 'left' as const, 
    label: '직원', 
    field: 'employeeName', 
    sortable: true 
  },
  { 
    name: 'actualStartTime', 
    align: 'left' as const, 
    label: '시작 시간', 
    field: 'actualStartTime', 
    sortable: true,
    format: (val: string) => formatTime(val)
  },
  { 
    name: 'actualEndTime', 
    align: 'left' as const, 
    label: '종료 시간', 
    field: 'actualEndTime', 
    sortable: true,
    format: (val: string) => formatTime(val)
  },
  { 
    name: 'calculatedDailyWage', 
    align: 'right' as const, 
    label: '일당', 
    field: 'calculatedDailyWage', 
    sortable: true,
    format: (val: number) => formatWage(val)
  },
  { 
    name: 'status', 
    align: 'left' as const, 
    label: '상태', 
    field: 'status', 
    sortable: true 
  },
  { 
    name: 'actions', 
    align: 'center' as const, 
    label: '관리', 
    field: 'actions' 
  }
]

// New record form
const newRecord = ref({
  employeeId: null as number | null,
  actualStartTime: '',
  actualEndTime: '',
  calculatedDailyWage: props.dailyWage,
  status: 'PENDING',
  remarks: ''
})

// Load attendance records
const loadAttendanceRecords = async () => {
  loading.value = true
  
  try {
    const response = await apiAttendance.getAttendanceByTaskId({
      taskId: props.taskId,
      page: pagination.value.page - 1,
      size: pagination.value.rowsPerPage,
      ...filters.value
    })
    
    attendanceRecords.value = response.data.content
    totalRecords.value = response.data.totalElements
    loading.value = false
  } catch (error) {
    console.error('Failed to load attendance records:', error)
    $q.notify({
      type: 'negative',
      message: '근태 기록을 불러오는데 실패했습니다.'
    })
    loading.value = false
  }
}

// Filter change handler
const onFilterChange = () => {
  pagination.value.page = 1
  loadAttendanceRecords()
}

// Request handler for pagination
const onRequest = () => {
  loadAttendanceRecords()
}

// Get color for status chip
const getStatusColor = (status: string): string => {
  const statusColors = {
    'COMPLETED': 'positive',
    'PENDING': 'warning',
    'ABSENT': 'negative',
    'LATE': 'orange',
    'EARLY_LEAVE': 'deep-orange'
  }
  
  return statusColors[status as keyof typeof statusColors] || 'grey'
}

// Get label for status
const getStatusLabel = (status: string): string => {
  const statusLabels = {
    'COMPLETED': '정상',
    'PENDING': '대기',
    'ABSENT': '결근',
    'LATE': '지각',
    'EARLY_LEAVE': '조퇴'
  }
  
  return statusLabels[status as keyof typeof statusLabels] || status
}

// Format time
const formatTime = (time: string): string => {
  return date.formatDate(time, 'HH:mm')
}

// Format wage
const formatWage = (wage: number): string => {
  return new Intl.NumberFormat('ko-KR', {
    style: 'currency',
    currency: 'KRW'
  }).format(wage)
}

// View attendance details
const viewDetails = (record: AttendanceRecord) => {
  $q.dialog({
    title: '근태 상세 정보',
    message: `
      <div class="q-pa-md">
        <p><strong>직원:</strong> ${record.employeeName}</p>
        <p><strong>시작 시간:</strong> ${formatTime(record.actualStartTime)}</p>
        <p><strong>종료 시간:</strong> ${formatTime(record.actualEndTime)}</p>
        <p><strong>일당:</strong> ${formatWage(record.calculatedDailyWage)}</p>
        <p><strong>상태:</strong> ${getStatusLabel(record.status)}</p>
        ${record.remarks ? `<p><strong>비고:</strong> ${record.remarks}</p>` : ''}
      </div>
    `,
    html: true,
    style: 'min-width: 300px'
  })
}

// Edit attendance record
const editRecord = (record: AttendanceRecord) => {
  selectedRecord.value = record
  newRecord.value = {
    employeeId: record.employeeId,
    actualStartTime: formatTime(record.actualStartTime),
    actualEndTime: formatTime(record.actualEndTime),
    calculatedDailyWage: record.calculatedDailyWage,
    status: record.status,
    remarks: record.remarks || ''
  }
  showManageDialog.value = true
}

// Confirm delete
const confirmDelete = (record: AttendanceRecord) => {
  selectedRecord.value = record
  showDeleteDialog.value = true
}

// Delete record
const deleteRecord = async () => {
  if (!selectedRecord.value) return
  
  submitting.value = true
  try {
    await apiAttendance.deleteAttendance(selectedRecord.value.id)
    showDeleteDialog.value = false
    await loadAttendanceRecords()
    $q.notify({
      type: 'positive',
      message: '근태 기록이 삭제되었습니다.'
    })
  } catch (error) {
    console.error('Failed to delete attendance record:', error)
    $q.notify({
      type: 'negative',
      message: '근태 기록 삭제에 실패했습니다.'
    })
  } finally {
    submitting.value = false
  }
}

// Submit attendance record
const onSubmitAttendance = async () => {
  submitting.value = true
  try {
    const data = {
      ...newRecord.value,
      taskId: props.taskId
    }
    
    if (selectedRecord.value) {
      await apiAttendance.updateAttendance(selectedRecord.value.id, data)
    } else {
      await apiAttendance.createAttendance(data)
    }
    
    showManageDialog.value = false
    await loadAttendanceRecords()
    $q.notify({
      type: 'positive',
      message: `근태 기록이 ${selectedRecord.value ? '수정' : '등록'}되었습니다.`
    })
    
    // Reset form
    selectedRecord.value = null
    newRecord.value = {
      employeeId: null,
      actualStartTime: '',
      actualEndTime: '',
      calculatedDailyWage: props.dailyWage,
      status: 'PENDING',
      remarks: ''
    }
  } catch (error) {
    console.error('Failed to submit attendance:', error)
    $q.notify({
      type: 'negative',
      message: `근태 기록 ${selectedRecord.value ? '수정' : '등록'}에 실패했습니다.`
    })
  } finally {
    submitting.value = false
  }
}

// Download Excel
const downloadExcel = () => {
  $q.notify({
    type: 'info',
    message: '엑셀 다운로드 기능은 준비중입니다.'
  })
}

// Computed filtered records
const filteredRecords = computed(() => {
  return attendanceRecords.value
})

onMounted(() => {
  loadAttendanceRecords()
})
</script>

<style lang="scss" scoped>
.desktop-only {
  display: none;
}

.mobile-only {
  display: block;
}

@media (min-width: 1024px) {
  .desktop-only {
    display: block;
  }
  
  .mobile-only {
    display: none;
  }
}

.q-table {
  border-radius: 8px;
}

:deep(.q-table__card) {
  box-shadow: none;
  border: 1px solid #e0e0e0;
}

.page-container {
  max-width: 1200px;
  margin: 0 auto;
}
</style>