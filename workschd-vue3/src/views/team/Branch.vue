<template>
  <q-page padding>
    <q-form @submit="handleSubmit" @reset="handleReset">
      <q-input v-model="branch.name" label="Branch Name" required />
      <q-input v-model="branch.region" label="Region" required />
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
  name: 'BranchPage',
  components: {
    AgGridVue
  },
  setup() {
    const branch = ref({
      name: '',
      region: ''
    });

    const rowData = ref([]);
    const columnDefs = ref([
      { headerName: 'Branch Name', field: 'name' },
      { headerName: 'Region', field: 'region' }
    ]);

    const onGridReady = async () => {
      const response = await fetch('/api/branches');
      const data = await response.json();
      rowData.value = data;
    };

    const handleSubmit = async () => {
      await fetch('/api/branches', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(branch.value)
      });

      await onGridReady();
      handleReset();
    };

    const handleReset = () => {
      branch.value = {
        name: '',
        region: ''
      };
    };

    return {
      branch,
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