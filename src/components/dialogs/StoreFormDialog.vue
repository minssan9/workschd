<template>
  <q-dialog v-model="isOpen" persistent>
    <q-card style="min-width: 350px">
      <q-card-section class="row items-center">
        <div class="text-h6">{{ t('team.workplace.addStore', 'Add Store') }}</div>
        <q-space />
        <q-btn icon="close" flat round dense v-close-popup />
      </q-card-section>

      <q-card-section>
        <q-form @submit.prevent="handleSubmit" @reset="handleReset">
          <q-input 
            v-model="storeForm.name" 
            :label="t('team.workplace.storeName', 'Store Name')" 
            required 
            class="q-mb-md"
          />
          <q-input 
            v-model="storeForm.address" 
            :label="t('team.workplace.address', 'Address')" 
            class="q-mb-md"
          />
          <q-input 
            v-model="storeForm.region" 
            :label="t('team.workplace.region', 'Region')" 
            class="q-mb-md"
          />
          <q-select
            v-model="storeForm.branch_id"
            :label="t('team.workplace.branch', 'Branch')"
            :options="branches"
            option-value="id"
            option-label="name"
            class="q-mb-lg"
          />
          <div class="row justify-end">
            <q-btn :label="t('common.reset', 'Reset')" type="reset" color="secondary" flat class="q-mr-sm" />
            <q-btn :label="t('common.submit', 'Submit')" type="submit" color="primary" />
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits } from 'vue'
import { useI18n } from 'vue-i18n'
import { useQuasar } from 'quasar'

const { t } = useI18n()
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

const props = defineProps<{
  modelValue: boolean
  branches: Branch[]
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'store-added'): void
}>()

const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const storeForm = ref<Store>({
  name: '',
  address: '',
  region: '',
  branch_id: null
})

const handleSubmit = async () => {
  try {
    await fetch('/api/stores', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(storeForm.value)
    })
    
    handleReset()
    isOpen.value = false
    emit('store-added')
    
    $q.notify({
      type: 'positive',
      message: t('team.workplace.storeAddedSuccess', 'Store added successfully')
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('team.workplace.storeAddError', 'Failed to add store')
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
</script> 