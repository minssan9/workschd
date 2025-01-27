<template>
  <q-page padding>
    <div class="row justify-between items-center q-mb-md">
      <div class="text-h6">{{ t('team.workplace.title', 'Workplace Management') }}</div>
      <q-btn 
        color="primary"
        :label="t('team.workplace.addStore', 'Add Store')"
        @click="showDialog = true"
        icon="add"
      />
    </div>

    <GridDefault
      style="width: 100%; height: 100%"
      :columnDefs="columnDefs"
      :rowData="rowData"
      @grid-ready="onGridReady"
      class="ag-theme-alpine-dark"
    />

    <StoreFormDialog
      v-model="showDialog"
      :branches="branches"
      @store-added="fetchStores"
    />
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { useQuasar } from 'quasar'
import StoreFormDialog from '@/components/dialogs/StoreFormDialog.vue'

const { t } = useI18n()
const $q = useQuasar()

interface Branch {
  id: number
  name: string
}

const showDialog = ref(false)
const rowData = ref([])
const branches = ref<Branch[]>([
  { id: 1, name: 'Branch 1' },
  { id: 2, name: 'Branch 2' }
])

const columnDefs = ref([
  { headerName: t('team.workplace.grid.storeName', 'Store Name'), field: 'name' },
  { headerName: t('team.workplace.grid.address', 'Address'), field: 'address' },
  { headerName: t('team.workplace.grid.region', 'Region'), field: 'region' },
  { headerName: t('team.workplace.grid.branch', 'Branch'), field: 'branch_id' }
])

const fetchStores = async () => {
  try {
    const response = await fetch('/api/stores')
    const data = await response.json()
    rowData.value = data
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('team.workplace.fetchError', 'Failed to fetch stores')
    })
  }
}

const onGridReady = () => {
  fetchStores()
}

onMounted(() => {
  fetchStores()
})
</script>

<style scoped>
.q-page {
  max-width: 1200px;
  margin: auto;
}
</style> 