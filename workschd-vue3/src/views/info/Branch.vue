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

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useQuasar } from 'quasar';
import apiBranch, { BranchDTO } from '@/api/modules/api-branch';

const $q = useQuasar();

const branch = ref<BranchDTO>({
  name: '',
  region: '',
  address: ''
});

const rowData = ref([]);

const loadBranches = async () => {
  try {
    const response = await apiBranch.getList();
    rowData.value = response.data;
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: 'Failed to load branches'
    });
  }
};

const handleSubmit = async () => {
  try {
    await apiBranch.create(branch.value);
    $q.notify({
      color: 'positive',
      message: 'Branch created successfully'
    });
    await loadBranches();
    handleReset();
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: 'Failed to create branch'
    });
  }
};

onMounted(() => {
  loadBranches();
});

// ... rest of your existing template and style
</script>

<style scoped>
.q-page {
  max-width: 800px;
  margin: auto;
}
</style>