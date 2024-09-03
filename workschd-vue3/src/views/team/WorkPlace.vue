<template>
  <q-page padding>
    <q-form @submit="handleSubmit" @reset="handleReset">
      <q-input v-model="store.name" label="Store Name" required />
      <q-input v-model="store.address" label="Address" />
      <q-input v-model="store.region" label="Region" />
      <q-select
          v-model="store.branch_id"
          label="Branch"
          :options="branches"
          option-value="id"
          option-label="name"
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
  name: 'StorePage',
  components: {
    AgGridVue
  },
  setup() {
    const store = ref({
      name: '',
      address: '',
      region: '',
      branch_id: null
    });

    const branches = ref([
      { id: 1, name: 'Branch 1' },
      { id: 2, name: 'Branch 2' }
      // 추가 지점 데이터를 여기에 추가하세요.
    ]);

    const rowData = ref([]);
    const columnDefs = ref([
      { headerName: 'Store Name', field: 'name' },
      { headerName: 'Address', field: 'address' },
      { headerName: 'Region', field: 'region' },
      { headerName: 'Branch', field: 'branch_id' }
    ]);

    const onGridReady = async () => {
      const response = await fetch('/api/stores');
      const data = await response.json();
      rowData.value = data;
    };

    const handleSubmit = async () => {
      await fetch('/api/stores', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(store.value)
      });

      await onGridReady();
      handleReset();
    };

    const handleReset = () => {
      store.value = {
        name: '',
        address: '',
        region: '',
        branch_id: null
      };
    };

    return {
      store,
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