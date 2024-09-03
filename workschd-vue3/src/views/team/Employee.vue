<template>
  <q-page padding>
    <q-form @submit="handleSubmit" @reset="handleReset">
      <q-input v-model="employee.phone_number" label="Phone Number" />
      <q-checkbox v-model="employee.is_manager" label="Is Manager?" />
      <q-select
          v-model="employee.account_id"
          label="Account"
          :options="accounts"
          option-value="id"
          option-label="username"
          required
      />
      <q-select
          v-model="employee.branch_id"
          label="Branch"
          :options="branches"
          option-value="id"
          option-label="name"
          required
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
  name: 'EmployeePage',
  components: {
    AgGridVue
  },
  setup() {
    const employee = ref({
      phone_number: '',
      is_manager: false,
      account_id: null,
      branch_id: null
    });

    const accounts = ref([
      { id: 1, username: 'user1' },
      { id: 2, username: 'user2' }
      // 추가 계정 데이터를 여기에 추가하세요.
    ]);

    const branches = ref([
      { id: 1, name: 'Branch 1' },
      { id: 2, name: 'Branch 2' }
      // 추가 지점 데이터를 여기에 추가하세요.
    ]);

    const rowData = ref([]);
    const columnDefs = ref([
      { headerName: 'Phone Number', field: 'phone_number' },
      { headerName: 'Is Manager', field: 'is_manager' },
      { headerName: 'Account', field: 'account_id' },
      { headerName: 'Branch', field: 'branch_id' }
    ]);

    const onGridReady = async () => {
      const response = await fetch('/api/employees');
      const data = await response.json();
      rowData.value = data;
    };

    const handleSubmit = async () => {
      await fetch('/api/employees', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(employee.value)
      });

      await onGridReady();
      handleReset();
    };

    const handleReset = () => {
      employee.value = {
        phone_number: '',
        is_manager: false,
        account_id: null,
        branch_id: null
      };
    };

    return {
      employee,
      accounts,
      branches,
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