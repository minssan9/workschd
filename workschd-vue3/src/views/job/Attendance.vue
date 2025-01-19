<template>
  <q-page padding>
    <q-form @submit="handleSubmit" @reset="handleReset">
      <q-select
          v-model="attendance.branchId"
          label="Branch"
          :options="branches"
          option-value="id"
          option-label="name"
          required
      />
      <q-select
          v-model="attendance.taskId"
          label="Task"
          :options="tasks"
          option-value="id"
          option-label="id"
          required
      />
      <q-input
          v-model="attendance.actualStartTime"
          label="Actual Start Time"
          type="datetime-local"
      />
      <q-input
          v-model="attendance.actualEndTime"
          label="Actual End Time"
          type="datetime-local"
      />
      <q-input
          v-model="attendance.calculatedDailyWage"
          label="Calculated Daily Wage"
          type="number"
      />
      <div class="q-mt-md">
        <q-btn label="Submit" type="submit" color="primary" />
        <q-btn label="Reset" type="reset" color="secondary" />
      </div>
    </q-form>

    <div class="q-mt-lg">
      <ag-grid-vue
          class="ag-theme-alpine"
          style="width: 100%; height: 400px;"
          :rowData="rowData"
          :columnDefs="columnDefs"
          @grid-ready="onGridReady"
      ></ag-grid-vue>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useQuasar } from 'quasar';
import apiAttendance, { AttendanceDTO } from '@/api/modules/api-attendance';
import apiBranch from '@/api/modules/api-branch';
import apiTask from '@/api/modules/api-task';

const $q = useQuasar();

const attendance = ref<AttendanceDTO>({
  branchId: 0,
  taskId: 0,
  calculatedDailyWage: 0,
  employeeId: 0, // This should be set from logged in user
  attendanceDate: new Date().toISOString().split('T')[0],
  dayOfWeek: new Date().toLocaleDateString('en-US', { weekday: 'long' }).toUpperCase(),
  startTime: '',
  endTime: ''
});

const branches = ref([]);
const tasks = ref([]);
const rowData = ref([]);

const loadBranches = async () => {
  try {
    const response = await apiBranch.getList();
    branches.value = response.data;
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: 'Failed to load branches'
    });
  }
};

const loadTasks = async () => {
  try {
    const response = await apiTask.getList();
    tasks.value = response.data;
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: 'Failed to load tasks'
    });
  }
};

const handleSubmit = async () => {
  try {
    await apiAttendance.create(attendance.value);
    $q.notify({
      color: 'positive',
      message: 'Attendance recorded successfully'
    });
    await loadAttendanceData();
    handleReset();
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: 'Failed to record attendance'
    });
  }
};

const loadAttendanceData = async () => {
  try {
    const response = await apiAttendance.getByEmployeeId(attendance.value.employeeId);
    rowData.value = response.data;
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: 'Failed to load attendance data'
    });
  }
};

onMounted(() => {
  loadBranches();
  loadTasks();
  loadAttendanceData();
});

// ... rest of your existing template and style
</script>

<style scoped>
.q-page {
  max-width: 800px;
  margin: auto;
}
</style>