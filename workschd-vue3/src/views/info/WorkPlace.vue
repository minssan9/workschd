<template>
  <q-page padding>
    <q-form @submit.prevent="handleSubmit" @reset="handleReset">
      <q-input v-model="storeForm.name" label="Store Name" required />
      <q-input v-model="storeForm.address" label="Address" />
      <q-input v-model="storeForm.region" label="Region" />
      <q-select
        v-model="storeForm.branch_id"
        label="Branch"
        :options="branches"
        option-value="id"
        option-label="name"
      />
      <div class="q-mt-md">
        <q-btn label="Submit" type="submit" color="primary" />
        <q-btn label="Reset" type="reset" color="secondary" class="q-ml-sm" />
      </div>
    </q-form>

    <div class="q-mt-lg">
      <ag-grid-vue
        class="ag-theme-alpine"
        style="width: 100%; height: 400px;"
        :rowData="rowData"
        :columnDefs="columnDefs"
        @grid-ready="onGridReady"
      />
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { AgGridVue } from 'ag-grid-vue3'
import { useQuasar } from 'quasar'

const $q = useQuasar()

interface Store {
  name: string
  address: string
  region: string
  branch_id: number | null
}

interface Branch {
  id: number
  name: string
}

const storeForm = ref<Store>({
  name: '',
  address: '',
  region: '',
  branch_id: null
})

const branches = ref<Branch[]>([
  { id: 1, name: 'Branch 1' },
  { id: 2, name: 'Branch 2' }
])

const rowData = ref([])

const columnDefs = ref([
  { headerName: 'Store Name', field: 'name' },
  { headerName: 'Address', field: 'address' },
  { headerName: 'Region', field: 'region' },
  { headerName: 'Branch', field: 'branch_id' }
])

const fetchStores = async () => {
  try {
    const response = await fetch('/api/stores')
    const data = await response.json()
    rowData.value = data
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to fetch stores'
    })
  }
}

const onGridReady = () => {
  fetchStores()
}

const handleSubmit = async () => {
  try {
    await fetch('/api/stores', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(storeForm.value)
    })
    
    await fetchStores()
    handleReset()
    
    $q.notify({
      type: 'positive',
      message: 'Store added successfully'
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: 'Failed to add store'
    })
  }
}

const handleReset = () => {
  storeForm.value = {
    name: '',
    address: '',
    region: '',
    branch_id: null
  }
}

onMounted(() => {
  fetchStores()
})
</script>

<style scoped>
.q-page {
  max-width: 800px;
  margin: auto;
}
</style>