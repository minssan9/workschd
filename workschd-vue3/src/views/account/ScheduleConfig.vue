<template>
  <q-page padding>
    <h2>Schedule Configuration</h2>

    <!-- 요일별 최소 근무 인원 설정 -->
    <div>
      <h3>Minimum Staff Per Day</h3>
      <q-input v-for="day in daysOfWeek" :key="day.value"
               v-model="minStaffPerDay[day.value]"
               :label="`Minimum staff for ${day.label}`"
               type="number"
               class="q-mb-md"
      />
    </div>

    <!-- 월별 최대 휴무일수 설정 -->
    <div>
      <h3>Maximum Off Days Per Month</h3>
      <q-input v-for="month in months" :key="month.value"
               v-model="maxOffDaysPerMonth[month.value]"
               :label="`Max off days for ${month.label}`"
               type="number"
               class="q-mb-md"
      />
    </div>

    <!-- 직원별 선호 요일 및 지점 설정 -->
    <div>
      <h3>Employee Preferences</h3>
      <q-table :rows="employees" :columns="employeeColumns" row-key="id">
        <template v-slot:body-cell-preferredDay="props">
          <q-select v-model="props.row.preferredDay" :options="daysOfWeek" option-value="value" option-label="label"/>
        </template>
        <template v-slot:body-cell-preferredBranch="props">
          <q-select v-model="props.row.preferredBranch" :options="branches" option-value="id" option-label="name"/>
        </template>
      </q-table>
    </div>

    <!-- 배정 실행 버튼 -->
    <q-btn @click="runScheduling" label="Run Scheduling" color="primary" class="q-mt-md"/>

    <!-- 배정 결과 출력 -->
    <div v-if="schedulingResult.length > 0" class="q-mt-lg">
      <h3>Scheduling Results</h3>
      <ag-grid-vue
          class="ag-theme-alpine"
          style="width: 100%; height: 400px;"
          :rowData="schedulingResult"
          :columnDefs="resultColumns"
      ></ag-grid-vue>
    </div>
  </q-page>
</template>

<script>
import { ref } from 'vue';
import { AgGridVue } from 'ag-grid-vue3';

export default {
  name: 'ScheduleConfigurationPage',
  components: {
    AgGridVue
  },
  setup() {
    const daysOfWeek = [
      { value: 'MONDAY', label: 'Monday' },
      { value: 'TUESDAY', label: 'Tuesday' },
      { value: 'WEDNESDAY', label: 'Wednesday' },
      { value: 'THURSDAY', label: 'Thursday' },
      { value: 'FRIDAY', label: 'Friday' },
      { value: 'SATURDAY', label: 'Saturday' },
      { value: 'SUNDAY', label: 'Sunday' }
    ];

    const months = [
      { value: 1, label: 'January' },
      { value: 2, label: 'February' },
      // 3월부터 12월까지 추가
      { value: 12, label: 'December' }
    ];

    const branches = ref([
      { id: 1, name: 'Downtown Branch' },
      { id: 2, name: 'Uptown Branch' },
      { id: 3, name: 'Suburban Branch' }
    ]);

    const employees = ref([
      { id: 1, name: 'John Doe', preferredDay: '', preferredBranch: null },
      { id: 2, name: 'Jane Smith', preferredDay: '', preferredBranch: null }
      // 추가 직원 정보를 여기에 추가
    ]);

    const minStaffPerDay = ref({
      MONDAY: 1,
      TUESDAY: 1,
      WEDNESDAY: 1,
      THURSDAY: 1,
      FRIDAY: 1,
      SATURDAY: 1,
      SUNDAY: 1
    });

    const maxOffDaysPerMonth = ref({
      1: 4, // January
      2: 4, // February
      // 3월부터 12월까지 추가
      12: 4 // December
    });

    const employeeColumns = [
      { name: 'name', required: true, label: 'Name', align: 'left', field: 'name' },
      { name: 'preferredDay', label: 'Preferred Day', align: 'left', field: 'preferredDay' },
      { name: 'preferredBranch', label: 'Preferred Branch', align: 'left', field: 'preferredBranch' }
    ];

    const schedulingResult = ref([]);
    const resultColumns = ref([
      { headerName: 'Employee', field: 'employeeName' },
      { headerName: 'Date', field: 'date' },
      { headerName: 'Branch', field: 'branch' }
    ]);

    const runScheduling = () => {
      // 여기에 배정 로직을 구현 (예: Spring Backend와 통신하여 배정 결과를 가져옴)
      schedulingResult.value = [
        { employeeName: 'John Doe', date: '2024-09-10', branch: 'Downtown Branch' },
        { employeeName: 'Jane Smith', date: '2024-09-11', branch: 'Uptown Branch' }
        // 배정된 추가 결과를 여기에 추가
      ];
    };

      return {
        daysOfWeek,
        months,
        branches,
        employees,
        minStaffPerDay,
        maxOffDaysPerMonth,
        employeeColumns,
        schedulingResult,
        resultColumns,
        runScheduling
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