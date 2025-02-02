<template>
  <q-page padding>
    <q-form @submit.prevent="handleSubmit" @reset="handleReset">
      <q-input v-model="storeForm.name" :label="t('team.workplace.storeName', 'Store Name')" required />
      <q-input v-model="storeForm.address" :label="t('team.workplace.address', 'Address')" />
      <q-input v-model="storeForm.region" :label="t('team.workplace.region', 'Region')" />
      <q-select
        v-model="storeForm.branch_id"
        :label="t('team.workplace.branch', 'Branch')"
        :options="branches"
        option-value="id"
        option-label="name"
      />
      <div class="q-mt-md">
        <q-btn :label="t('common.submit', 'Submit')" type="submit" color="primary" />
        <q-btn :label="t('common.reset', 'Reset')" type="reset" color="secondary" class="q-ml-sm" />
      </div>
    </q-form>

    <div class="q-mt-lg">
      <GridDefault
          style="width: 100%; height: 100%"
          :columnDefs="columnDefs"
          :rowData="rowData"
          @grid-ready="onGridReady"
          class="ag-theme-alpine-dark"
      />
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { AgGridVue } from 'ag-grid-vue3'
import { useQuasar } from 'quasar'
import { useI18n } from 'vue-i18n'
import { useTeamStore } from '@/stores/modules/teamStore'
import { Store, Branch } from '@/interface/workplace'

const { t } = useI18n()
const $q = useQuasar()

const teamStore = useTeamStore()

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
  { headerName: t('team.workplace.grid.storeName', 'Store Name'), field: 'name' },
  { headerName: t('team.workplace.grid.address', 'Address'), field: 'address' },
  { headerName: t('team.workplace.grid.region', 'Region'), field: 'region' },
  { headerName: t('team.workplace.grid.branch', 'Branch'), field: 'branch_id' }
])

const fetchStores = async () => {
  try {
    const response = await fetch('/stores')
    const data = response
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
    await fetch('/stores', {
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