<template>
  <q-page padding>
    <div class="row items-center justify-between q-mb-md">
      <h5 class="q-mt-none q-mb-none">{{ t('attendance.title', 'Attendance Records') }}</h5>
      <div class="row q-gutter-sm">
        <q-btn 
          :label="showForm ? t('attendance.hideForm', 'Hide Form') : t('attendance.showForm', 'Show Form')" 
          color="primary" 
          outline
          size="sm"
          @click="showForm = !showForm" 
        />
        <q-btn 
          :label="t('attendance.refresh', 'Refresh')" 
          color="secondary" 
          outline
          size="sm"
          @click="loadAttendanceData" 
          icon="refresh"
        />
      </div>
    </div>
    
    <q-slide-transition>
      <div v-if="showForm" class="q-mb-md">
        <q-card flat bordered>
          <q-card-section>
            <q-form @submit="handleSubmit" @reset="handleReset" class="row q-col-gutter-md">
              <div class="col-12 col-md-3">
                <q-input
                  v-model="attendance.actualStartTime"
                  :label="t('attendance.label.actualStartTime', 'Actual Start Time')"
                  type="datetime-local"
                  filled
                  dense
                />
              </div>
              <div class="col-12 col-md-3">
                <q-input
                  v-model="attendance.actualEndTime"
                  :label="t('attendance.label.actualEndTime', 'Actual End Time')"
                  type="datetime-local"
                  filled
                  dense
                />
              </div>
              <div class="col-12 col-md-2">
                <q-input
                  v-model="attendance.calculatedDailyWage"
                  :label="t('attendance.label.calculatedDailyWage', 'Calculated Daily Wage')"
                  type="number"
                  filled
                  dense
                />
              </div>
              <div class="col-12 col-md-4">
                <div class="row justify-end q-mt-sm">
                  <q-btn :label="t('common.button.reset', 'Reset')" type="reset" color="secondary" flat dense class="q-mr-sm" />
                  <q-btn :label="t('common.button.submit', 'Submit')" type="submit" color="primary" dense />
                </div>
              </div>
            </q-form>
          </q-card-section>
        </q-card>
      </div>
    </q-slide-transition>

    <div class="q-mt-md attendance-grid">
      <q-card flat bordered>
        <q-card-section class="q-pa-none">
          <GridDefault
            :rowData="rowData"
            :columnDefs="columnDefs"
            class="ag-theme-alpine"
            domLayout="autoHeight"
          />
        </q-card-section>
      </q-card>
    </div>
    
    <!-- Empty state when no records -->
    <div v-if="rowData.length === 0" class="text-center q-pa-lg text-grey-7">
      <q-icon name="event_busy" size="48px" />
      <div class="text-h6 q-mt-sm">{{ t('attendance.noRecords', 'No attendance records found') }}</div>
      <div class="q-mt-sm">{{ t('attendance.clickShowForm', 'Click "Show Form" to record attendance') }}</div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, defineProps } from 'vue';
import { useQuasar } from 'quasar';
import { useI18n } from 'vue-i18n';
import GridDefault from '@/components/grid/GridDefault.vue';
import apiAttendance, { AttendanceDTO } from '@/api/modules/api-attendance';

const props = defineProps({
  jobId: {
    type: Number,
    required: true
  },
  branchId: {
    type: Number,
    required: true
  },
  taskId: {
    type: Number,
    required: true
  },
  startTime: {
    type: String,
    default: ''
  },
  endTime: {
    type: String,
    default: ''
  },
  dailyWage: {
    type: Number,
    default: 0
  }
});

const { t } = useI18n();
const $q = useQuasar();
const showForm = ref(false);

const attendance = ref<AttendanceDTO>({
  branchId: props.branchId,
  taskId: props.taskId,
  calculatedDailyWage: props.dailyWage,
  employeeId: 0, // This should come from user store in a real app
  attendanceDate: new Date().toISOString().split('T')[0],
  dayOfWeek: new Date().toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase(),
  startTime: props.startTime,
  endTime: props.endTime,
  actualStartTime: new Date().toISOString().slice(0, 16),
  actualEndTime: new Date().toISOString().slice(0, 16)
});

const rowData = ref([]);
const columnDefs = ref([
  { field: 'employeeId', headerName: t('attendance.grid.employee', 'Employee') },
  { field: 'actualStartTime', headerName: t('attendance.grid.startTime', 'Start Time') },
  { field: 'actualEndTime', headerName: t('attendance.grid.endTime', 'End Time') },
  { field: 'calculatedDailyWage', headerName: t('attendance.grid.wage', 'Daily Wage') },
  { field: 'status', headerName: t('attendance.grid.status', 'Status') }
]);

const handleSubmit = async () => {
  try {
    await apiAttendance.create(attendance.value);
    $q.notify({
      type: 'positive',
      message: t('attendance.notification.success', 'Attendance recorded successfully')
    });
    await loadAttendanceData();
    showForm.value = false;
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('attendance.notification.error', 'Failed to record attendance')
    });
  }
};

const loadAttendanceData = async () => {
  try {
    const response = await apiAttendance.getByTaskId(props.taskId);
    rowData.value = response.data;
  } catch (error) {
    console.error('Failed to load attendance data', error);
    // For demo purposes, add some sample data
    rowData.value = [
      {
        employeeId: 1,
        actualStartTime: '2023-05-15T09:05:00',
        actualEndTime: '2023-05-15T17:10:00',
        calculatedDailyWage: 120,
        status: 'COMPLETED'
      }
    ];
  }
};

const handleReset = () => {
  attendance.value = {
    branchId: props.branchId,
    taskId: props.taskId,
    calculatedDailyWage: props.dailyWage,
    employeeId: 0,
    attendanceDate: new Date().toISOString().split('T')[0],
    dayOfWeek: new Date().toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase(),
    startTime: props.startTime,
    endTime: props.endTime,
    actualStartTime: new Date().toISOString().slice(0, 16),
    actualEndTime: new Date().toISOString().slice(0, 16)
  };
};

// Watch for changes in props to update the attendance form
watch(() => props.branchId, (newVal) => {
  attendance.value.branchId = newVal;
});

watch(() => props.taskId, (newVal) => {
  attendance.value.taskId = newVal;
  loadAttendanceData();
});

watch(() => props.dailyWage, (newVal) => {
  attendance.value.calculatedDailyWage = newVal;
});

onMounted(() => {
  loadAttendanceData();
});
</script>

<style scoped>
.attendance-grid {
  margin-bottom: 16px;
}

.ag-theme-alpine {
  --ag-header-height: 36px;
  --ag-row-height: 36px;
  --ag-font-size: 13px;
  --ag-header-foreground-color: #5c5c5c;
  --ag-header-background-color: #f5f5f5;
  --ag-odd-row-background-color: #fcfcfc;
}

.q-card {
  box-shadow: none;
  border-radius: 8px;
}
</style>