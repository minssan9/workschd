<template>
  <q-page padding class="page-container">  
    <div class="row justify-between items-center q-mb-md">
      <h5 class="q-my-none">Attendance Records</h5>
      <div>
        <q-btn 
          v-if="isAdmin"
          color="secondary" 
          label="Manage Attendance" 
          @click="showManageDialog = true"
          class="q-mr-sm"
        />
      </div>
    </div>
 
    <div class="content-section q-pa-md q-mb-md">

      <div v-if="loading" class="text-center q-pa-lg">
        <q-spinner color="primary" size="3em" />
        <div class="q-mt-sm">Loading attendance records...</div>
      </div>

      <div v-else-if="attendanceRecords.length === 0" class="text-center q-pa-lg">
        <q-icon name="event_busy" size="4em" color="grey-6" />
        <div class="text-h6 q-mt-md text-grey-8">No Attendance Records</div>
        <p class="text-grey-7">No attendance has been recorded for this task yet.</p>
      </div>

      <div v-else>
        <q-table
          :rows="attendanceRecords"
          :columns="columns"
          row-key="id"
          :pagination="{ rowsPerPage: 5 }"
          class="q-mt-md"
        >
          <template v-slot:body-cell-status="props">
            <q-td :props="props">
              <q-chip :color="getStatusColor(props.value)" text-color="white">
                {{ props.value }}
              </q-chip>
            </q-td>
          </template>
          
          <template v-slot:body-cell-actions="props">
            <q-td :props="props">
              <q-btn flat round color="primary" icon="visibility" @click="viewDetails(props.row)">
                <q-tooltip>View Details</q-tooltip>
              </q-btn>
              <q-btn v-if="isAdmin" flat round color="warning" icon="edit" @click="editRecord(props.row)">
                <q-tooltip>Edit</q-tooltip>
              </q-btn>
            </q-td>
          </template>
        </q-table>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useQuasar } from 'quasar'
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

const loading = ref(true)
const showManageDialog = ref(false)
const attendanceRecords = ref<AttendanceRecord[]>([])

const columns = [
  { name: 'employeeName', align: 'left' as 'left', label: 'Employee', field: 'employeeName', sortable: true },
  { name: 'actualStartTime', align: 'left' as 'left', label: 'Start Time', field: 'actualStartTime', sortable: true },
  { name: 'actualEndTime', align: 'left' as 'left', label: 'End Time', field: 'actualEndTime', sortable: true },
  { name: 'calculatedDailyWage', align: 'left' as 'left', label: 'Wage', field: 'calculatedDailyWage', sortable: true },
  { name: 'status', align: 'left' as 'left', label: 'Status', field: 'status', sortable: true },
  { name: 'actions', align: 'center' as 'center', label: 'Actions', field: 'actions' }
]

// Load attendance records
const loadAttendanceRecords = async () => {
  loading.value = true
  
  try {
    const response = await apiAttendance.getAttendanceByTaskId(props.taskId)
    attendanceRecords.value = response.data
    loading.value = false
  } catch (error) {
    $q.notify({type: 'negative', message: 'Failed to load attendance records'})
    loading.value = false
  }
}

// Get color for status chip
const getStatusColor = (status: string): string => {
  const statusColors = {
    'COMPLETED': 'positive',
    'PENDING': 'warning',
    'ABSENT': 'negative',
    'LATE': 'orange'
  }
  
  return statusColors[status as keyof typeof statusColors] || 'grey'
}

// View attendance details
const viewDetails = (record: AttendanceRecord) => {
  $q.dialog({
    title: 'Attendance Details',
    message: `
      <div class="q-pa-md">
        <p><strong>Employee:</strong> ${record.employeeName}</p>
        <p><strong>Start Time:</strong> ${record.actualStartTime}</p>
        <p><strong>End Time:</strong> ${record.actualEndTime}</p>
        <p><strong>Wage:</strong> $${record.calculatedDailyWage}</p>
        <p><strong>Status:</strong> ${record.status}</p>
      </div>
    `,
    html: true
  })
}

// Edit attendance record
const editRecord = (record: AttendanceRecord) => {
  // In a real app, this would open an edit dialog
  $q.notify({type: 'info', message: `Editing record for ${record.employeeName}`})
}

onMounted(() => {
  loadAttendanceRecords()
})
</script>

<style scoped>
.q-table {
  border-radius: 8px;
}

:deep(.q-table__card) {
  box-shadow: none;
  border: 1px solid #e0e0e0;
}
</style>