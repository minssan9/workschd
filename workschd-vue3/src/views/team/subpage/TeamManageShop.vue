<template>
  <div>
    <div class="row items-center justify-between q-mb-md">
      <h5 class="q-mt-none q-mb-none">{{ t('team.workplace.title', 'Workplace Management') }}</h5>
      <div class="row q-gutter-sm">
        <q-btn 
          :label="t('team.workplace.addStore', 'Add Store')" 
          color="primary" 
          outline
          size="sm"
          @click="openAddStoreDialog" 
          icon="add_business"
        />
        <q-btn 
          :label="t('team.workplace.refresh', 'Refresh')" 
          color="secondary" 
          outline
          size="sm"
          @click="fetchShops" 
          icon="refresh"
        />
      </div>
    </div>
    
    <div class="q-mt-md workplace-grid">
      <q-card flat bordered>
        <q-card-section class="q-pa-none">
          <GridDefault
            :rowData="shops"
            :columnDefs="columnDefs"
            :gridOptions="gridOptions"
            class="ag-theme-alpine"
            domLayout="autoHeight"
          />
        </q-card-section>
      </q-card>
    </div>
    
    <!-- Empty state when no shops -->
    <div v-if="shops.length === 0" class="text-center q-pa-lg text-grey-7">
      <q-icon name="store" size="48px" />
      <div class="text-h6 q-mt-sm">{{ t('team.workplace.noStores', 'No shops found') }}</div>
      <div class="q-mt-sm">{{ t('team.workplace.clickAddStore', 'Click "Add Store" to add a new workplace') }}</div>
    </div>
    
    <!-- Store Dialog (used for both add and edit) -->
    <q-dialog v-model="showStoreDialog" persistent>
      <q-card style="min-width: 400px">
        <q-card-section class="row items-center">
          <div class="text-h6">{{ dialogMode === 'add' ? t('team.workplace.addStore', 'Add Store') : t('team.workplace.editStore', 'Edit Store') }}</div>
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="handleSubmit" @reset="handleReset" class="row q-col-gutter-md">
            <div class="col-12">
              <q-input 
                v-model="storeForm.name" 
                :label="t('team.workplace.storeName', 'Store Name')" 
                filled
                dense
                required 
              />
            </div>
            <div class="col-12">
              <q-select
                v-model="storeForm.branch_id"
                :label="t('team.workplace.branch', 'Branch')"
                :options="branches"
                option-value="id"
                option-label="name"
                filled
                dense
                emit-value
                map-options
              />
            </div>
            <div class="col-12">
              <q-input 
                v-model="storeForm.address" 
                :label="t('team.workplace.address', 'Address')" 
                filled
                dense
              />
            </div>
            <div class="col-12">
              <q-input 
                v-model="storeForm.region" 
                :label="t('team.workplace.region', 'Region')" 
                filled
                dense
              />
            </div>
          </q-form>
        </q-card-section>
        
        <q-card-actions align="right">
          <q-btn :label="t('common.cancel', 'Cancel')" color="secondary" flat v-close-popup />
          <q-btn 
            :label="t('common.submit', 'Submit')" 
            color="primary" 
            @click="handleSubmit" 
            :loading="isSubmitting"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
    
    <!-- Store Details Dialog -->
    <q-dialog v-model="showStoreDetails">
      <q-card style="min-width: 350px">
        <q-card-section class="row items-center">
          <div class="text-h6">{{ t('team.workplace.storeDetails', 'Store Details') }}</div>
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>

        <q-card-section v-if="selectedStore">
          <div class="row q-col-gutter-md">
            <div class="col-12">
              <div class="detail-item">
                <div class="detail-label">{{ t('team.workplace.storeName', 'Store Name') }}</div>
                <div class="detail-value">{{ selectedStore.name }}</div>
              </div>
            </div>
            <div class="col-12">
              <div class="detail-item">
                <div class="detail-label">{{ t('team.workplace.branch', 'Branch') }}</div>
                <div class="detail-value">{{ selectedStore.branch_name }}</div>
              </div>
            </div>
            <div class="col-12">
              <div class="detail-item">
                <div class="detail-label">{{ t('team.workplace.region', 'Region') }}</div>
                <div class="detail-value">{{ selectedStore.region || 'N/A' }}</div>
              </div>
            </div>
            <div class="col-12">
              <div class="detail-item">
                <div class="detail-label">{{ t('team.workplace.address', 'Address') }}</div>
                <div class="detail-value">{{ selectedStore.address || 'N/A' }}</div>
              </div>
            </div>
          </div>
        </q-card-section>
        
        <q-card-actions align="right">
          <q-btn 
            :label="t('team.workplace.edit', 'Edit')" 
            color="primary" 
            @click="editStore" 
            v-close-popup
          />
          <q-btn 
            :label="t('team.workplace.delete', 'Delete')" 
            color="negative" 
            @click="confirmDelete" 
            v-close-popup
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, defineProps } from 'vue'
import { useQuasar } from 'quasar'
import { useI18n } from 'vue-i18n'
import GridDefault from '@/components/grid/GridDefault.vue'

const props = defineProps({
  teamId: {
    type: Number,
    required: true
  }
})

const { t } = useI18n()
const $q = useQuasar()
const showStoreDialog = ref(false)
const showStoreDetails = ref(false)
const selectedStore = ref(null)
const isSubmitting = ref(false)
const dialogMode = ref('add') // 'add' or 'edit'
const editingStoreId = ref(null)

const storeForm = ref({
  name: '',
  address: '',
  region: '',
  branch_id: null,
  team_id: props.teamId
})

const shops = ref([
  { id: 1, name: 'Store A', address: '123 Main St', region: 'North', branch_id: 1, branch_name: 'Branch A', team_id: props.teamId },
  { id: 2, name: 'Store B', address: '456 Oak Ave', region: 'South', branch_id: 2, branch_name: 'Branch B', team_id: props.teamId }
])

const branches = ref([
  { id: 1, name: 'Branch A' },
  { id: 2, name: 'Branch B' },
  { id: 3, name: 'Branch C' }
])

// Grid options with row click handler
const gridOptions = ref({
  onRowClicked: (params) => {
    // Only handle row clicks if the click wasn't on the delete button
    if (!params.event.defaultPrevented) {
      console.log('Row clicked:', params);
      selectedStore.value = params.data;
      showStoreDetails.value = true;
    }
  },
  // Make rows look clickable
  rowStyle: { cursor: 'pointer' }
})

const columnDefs = ref([
  { 
    field: 'name', 
    headerName: t('team.workplace.grid.name', 'Store Name'),
    cellRenderer: (params) => {
      return `<div class="clickable-cell">${params.value}</div>`;
    }
  },
  { field: 'address', headerName: t('team.workplace.grid.address', 'Address') },
  { field: 'region', headerName: t('team.workplace.grid.region', 'Region') },
  { field: 'branch_name', headerName: t('team.workplace.grid.branch', 'Branch') },
  {
    field: 'actions',
    headerName: t('team.workplace.grid.actions', 'Actions'),
    cellRenderer: () => {
      return `<button class="q-btn q-btn-item non-selectable no-outline q-btn--flat q-btn--round q-btn--actionable q-focusable q-hoverable q-btn--dense text-negative">
        <span class="q-focus-helper"></span>
        <span class="q-btn__content text-center">
          <i aria-hidden="true" role="presentation" class="material-icons q-icon">delete</i>
        </span>
      </button>`;
    },
    onCellClicked: (params) => {
      // Prevent the row click event
      params.event.preventDefault();
      handleDelete(params.data.id);
    }
  }
])
 
// Fetch shops from API
const fetchShops = async () => {
  try {
    // In a real app, fetch from API
    // const response = await fetch(`/teams/${props.teamId}/shops`)
    // shops.value = await response.json()
    
    // For demo purposes, we're using the static data
  } catch (error) {
    console.error('Failed to fetch shops', error)
  }
}

const openAddStoreDialog = () => {
  dialogMode.value = 'add';
  editingStoreId.value = null;
  handleReset();
  showStoreDialog.value = true;
}

const handleSubmit = async () => {
  try {
    isSubmitting.value = true;
    
    if (dialogMode.value === 'add') {
      // In a real app, submit to API
      // await fetch(`/teams/${props.teamId}/shops`, {
      //   method: 'POST',
      //   headers: { 'Content-Type': 'application/json' },
      //   body: JSON.stringify(storeForm.value)
      // })
      
      // For demo purposes
      const branchName = branches.value.find(b => b.id === storeForm.value.branch_id)?.name || '';
      shops.value.push({
        id: Date.now(),
        ...storeForm.value,
        branch_name: branchName
      });
      
      $q.notify({
        type: 'positive',
        message: t('team.workplace.storeAdded', 'Store added successfully')
      });
    } else {
      // In a real app, update via API
      // await fetch(`/shops/${editingStoreId.value}`, {
      //   method: 'PUT',
      //   headers: { 'Content-Type': 'application/json' },
      //   body: JSON.stringify(storeForm.value)
      // })
      
      // For demo purposes
      const index = shops.value.findIndex(s => s.id === editingStoreId.value);
      if (index !== -1) {
        const branchName = branches.value.find(b => b.id === storeForm.value.branch_id)?.name || '';
        shops.value[index] = {
          ...shops.value[index],
          ...storeForm.value,
          branch_name: branchName
        };
      }
      
      $q.notify({
        type: 'positive',
        message: t('team.workplace.storeUpdated', 'Store updated successfully')
      });
    }
    
    showStoreDialog.value = false;
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: dialogMode.value === 'add' 
        ? t('team.workplace.addError', 'Failed to add store')
        : t('team.workplace.updateError', 'Failed to update store')
    });
  } finally {
    isSubmitting.value = false;
  }
}

const handleDelete = async (id) => {
  try {
    $q.dialog({
      title: t('team.workplace.confirmDelete', 'Confirm Deletion'),
      message: t('team.workplace.deleteMessage', 'Are you sure you want to delete this store?'),
      cancel: true,
      persistent: true
    }).onOk(async () => {
      // In a real app, delete from API
      // await fetch(`/shops/${id}`, { method: 'DELETE' })
      
      // For demo purposes
      shops.value = shops.value.filter(store => store.id !== id)
      
      $q.notify({
        type: 'positive',
        message: t('team.workplace.storeDeleted', 'Store deleted successfully')
      })
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('team.workplace.deleteError', 'Failed to delete store')
    })
  }
}

const editStore = () => {
  if (!selectedStore.value) return;
  
  dialogMode.value = 'edit';
  editingStoreId.value = selectedStore.value.id;
  
  // Populate the form with selected store data
  storeForm.value = {
    name: selectedStore.value.name,
    address: selectedStore.value.address,
    region: selectedStore.value.region,
    branch_id: selectedStore.value.branch_id,
    team_id: props.teamId
  };
  
  // Show the dialog
  showStoreDialog.value = true;
}

const confirmDelete = () => {
  if (selectedStore.value) {
    handleDelete(selectedStore.value.id);
  }
}

const handleReset = () => {
  storeForm.value = {
    name: '',
    address: '',
    region: '',
    branch_id: null,
    team_id: props.teamId
  }
}

// Watch for changes in teamId
watch(() => props.teamId, (newVal) => {
  storeForm.value.team_id = newVal
  fetchShops()
})

onMounted(() => { 
  fetchShops()
})
</script>

<style scoped>
.workplace-grid {
  margin-bottom: 16px;
}

.ag-theme-alpine {
  --ag-header-height: 36px;
  --ag-row-height: 36px;
  --ag-font-size: 13px;
  --ag-header-foreground-color: #5c5c5c;
  --ag-header-background-color: #f5f5f5;
  --ag-odd-row-background-color: #fcfcfc;
}

.q-card {
  box-shadow: none;
  border-radius: 8px;
}

.clickable-cell {
  cursor: pointer;
  color: var(--q-primary);
  text-decoration: underline;
}

.detail-item {
  margin-bottom: 12px;
}

.detail-label {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 4px;
}

.detail-value {
  font-size: 1rem;
  font-weight: 500;
}
</style>