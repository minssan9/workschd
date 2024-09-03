<template>
  <q-page padding>
    <q-form @submit="handleSubmit" @reset="handleReset">
      <q-select
          v-model="attendance.branch_id"
          label="Branch"
          :options="branches"
          option-value="id"
          option-label="name"
          required
      />
      <q-select
          v-model="attendance.task_id"
          label="Task"
          :options="tasks"
          option-value="id"
          option-label="id"
          required
      />
      <q-input
          v-model="attendance.actual_start_time"
          label="Actual Start Time"
          type="datetime-local"
      />
      <q-input
          v-model="attendance.actual_end_time"
          label="Actual End Time"
          type="datetime-local"
      />
      <q-input
          v-model="attendance.calculated_daily_wage"
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

<script>
import { ref } from 'vue';
import { AgGridVue } from 'ag-grid-vue3';

export default {
  name: 'AttendancePage',
  components: {
    AgGridVue
  },
  setup() {
    const attendance = ref({
      branch_id: null,
      task_id: null,
      actual_start_time: '',
      actual_end_time: '',
      calculated_daily_wage: 0
    });

    const branches = ref([
      { id: 1, name: 'Branch 1' },
      { id: 2, name: 'Branch 2' }
      // 추가 지점 데이터를 여기에 추가하세요.
    ]);

    const tasks = ref([
      { id: 1 },
      { id: 2 }
      // 추가 업무 데이터를 여기에 추가하세요.
    ]);

    const rowData = ref([]);
    const columnDefs = ref([
      { headerName: 'Branch', field: 'branch_id' },
      { headerName: 'Task', field: 'task_id' },
      { headerName: 'Actual Start Time', field: 'actual_start_time' },
      { headerName: 'Actual End Time', field: 'actual_end_time' },
      { headerName: 'Calculated Daily Wage', field: 'calculated_daily_wage' }
    ]);

    const onGridReady = async () => {
      const response = await fetch('/api/attendance');
      const data = await response.json();
      rowData.value = data;
    };

    const handleSubmit = async () => {
      await fetch('/api/attendance', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(attendance.value)
      });

      await onGridReady();
      handleReset();
    };

    const handleReset = () => {
      attendance.value = {
        branch_id: null,
        task_id: null,
        actual_start_time: '',
        actual_end_time: '',
        calculated_daily_wage: 0
      };
    };

    return {
      attendance,
      branches,
      tasks,
      rowData,
      columnDefs,
      onGridReady,
      handleSubmit,
      handleReset
    };
  }
};
</script>

<style scoped>
.q-page {
  max-width: 800px;
  margin: auto;
}
</style>