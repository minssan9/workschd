<template>
  <q-dialog v-model="dialogVisible" @hide="onHide" maximized>
    <q-card style="width: 800px; max-width: 90vw">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">{{ t('team.dialog.registerNewTeam', 'Register New Team') }}</div>
        <q-space />
        <q-btn icon="close" flat round dense v-close-popup />
      </q-card-section>

      <q-card-section>
        <q-form @submit="onSubmit" class="q-gutter-md">
          <div class="row q-col-gutter-md">
            <!-- First column -->
            <div class="col-12 col-md-6">
              <q-input
                v-model="teamData.name"
                :label="t('team.dialog.teamName', 'Team Name')"
                required
                :rules="[val => !!val || t('validation.required', 'Field is required')]"
              />
              
              <q-input
                v-model="teamData.location"
                :label="t('team.dialog.location', 'Location')"
                required
                :rules="[val => !!val || t('validation.required', 'Field is required')]"
                class="q-mt-md"
              />
              
              <q-select
                v-model="teamData.department"
                :label="t('team.dialog.department', 'Department')"
                :options="departments"
                option-value="id"
                option-label="name"
                required
                :rules="[val => !!val || t('validation.required', 'Field is required')]"
                class="q-mt-md"
              />
            </div>
            
            <!-- Second column -->
            <div class="col-12 col-md-6">
              <q-select
                v-model="teamData.manager"
                :label="t('team.dialog.manager', 'Team Manager')"
                :options="managers"
                option-value="id"
                option-label="name"
                required
                :rules="[val => !!val || t('validation.required', 'Field is required')]"
              />
              
              <q-input
                v-model="teamData.description"
                :label="t('team.dialog.description', 'Description')"
                type="textarea"
                rows="3"
                class="q-mt-md"
              />
              
              <q-toggle
                v-model="teamData.isActive"
                :label="t('team.dialog.isActive', 'Active Team')"
                color="primary"
                class="q-mt-md"
              />
            </div>
          </div>
          
          <!-- Team Members Section -->
          <q-separator class="q-my-md" />
          
          <div class="text-subtitle1 q-mb-sm">{{ t('team.dialog.initialMembers', 'Initial Team Members') }}</div>
          
          <div class="row q-col-gutter-md">
            <div class="col-12">
              <q-select
                v-model="selectedMembers"
                :label="t('team.dialog.selectMembers', 'Select Members')"
                :options="availableMembers"
                option-value="id"
                option-label="name"
                multiple
                use-chips
                stack-label
              />
            </div>
          </div>
          
          <!-- Schedule Configuration Section -->
          <q-separator class="q-my-md" />
          
          <div class="text-subtitle1 q-mb-sm">{{ t('team.dialog.scheduleConfig', 'Schedule Configuration') }}</div>
          
          <div class="row q-col-gutter-md">
            <div class="col-12 col-md-6">
              <q-select
                v-model="teamData.workDays"
                :label="t('team.dialog.workDays', 'Work Days')"
                :options="daysOfWeek"
                option-value="value"
                option-label="label"
                multiple
                use-chips
              />
            </div>
            
            <div class="col-12 col-md-6">
              <q-input
                v-model.number="teamData.minStaffPerDay"
                :label="t('team.dialog.minStaffPerDay', 'Minimum Staff Per Day')"
                type="number"
                min="1"
              />
            </div>
          </div>
          
          <!-- Action Buttons -->
          <div class="row justify-end q-mt-lg">
            <q-btn 
              :label="t('common.cancel', 'Cancel')" 
              color="negative" 
              v-close-popup 
              class="q-mr-sm" 
            />
            <q-btn 
              :label="t('common.reset', 'Reset')" 
              type="reset" 
              color="warning" 
              @click="handleReset" 
              class="q-mr-sm" 
            />
            <q-btn 
              :label="t('common.submit', 'Submit')" 
              type="submit" 
              color="primary" 
            />
          </div>
        </q-form>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { useQuasar } from 'quasar'
import { Team } from '@/interface/team'
import { DayConfig } from '@/interface/schedule'

const { t } = useI18n()
const $q = useQuasar()

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'team-registered'])

// Computed property for dialog visibility
const dialogVisible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// Form data
const teamData = ref({
  name: '',
  location: '',
  department: null,
  manager: null,
  description: '',
  isActive: true,
  workDays: [],
  minStaffPerDay: 1
})

const selectedMembers = ref([])

// Mock data for dropdowns
const departments = ref([
  { id: 1, name: 'Sales' },
  { id: 2, name: 'Marketing' },
  { id: 3, name: 'Operations' },
  { id: 4, name: 'Human Resources' }
])

const managers = ref([
  { id: 1, name: 'John Doe' },
  { id: 2, name: 'Jane Smith' },
  { id: 3, name: 'Robert Johnson' }
])

const availableMembers = ref([
  { id: 1, name: 'Alice Brown' },
  { id: 2, name: 'Bob Wilson' },
  { id: 3, name: 'Carol Martinez' },
  { id: 4, name: 'David Lee' },
  { id: 5, name: 'Eva Chen' },
  { id: 6, name: 'Frank Rodriguez' }
])

const daysOfWeek: DayConfig[] = [
  { value: 'MONDAY', label: 'Monday' },
  { value: 'TUESDAY', label: 'Tuesday' },
  { value: 'WEDNESDAY', label: 'Wednesday' },
  { value: 'THURSDAY', label: 'Thursday' },
  { value: 'FRIDAY', label: 'Friday' },
  { value: 'SATURDAY', label: 'Saturday' },
  { value: 'SUNDAY', label: 'Sunday' }
]

// Methods
const onSubmit = async () => {
  try {
    // Simulate API call
    const newTeam: Team = {
      id: Math.floor(Math.random() * 1000),
      name: teamData.value.name,
      location: teamData.value.location,
      memberCount: selectedMembers.value.length,
      createdAt: new Date().toISOString(),
      joinRequests: []
    }
    
    // Emit the new team to parent component
    emit('team-registered', newTeam)
    
    // Close dialog and reset form
    dialogVisible.value = false
    handleReset()
    
    // Show success notification
    $q.notify({
      type: 'positive',
      message: t('team.dialog.successMessage', 'Team registered successfully')
    })
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('team.dialog.errorMessage', 'Failed to register team')
    })
  }
}

const handleReset = () => {
  teamData.value = {
    name: '',
    location: '',
    department: null,
    manager: null,
    description: '',
    isActive: true,
    workDays: [],
    minStaffPerDay: 1
  }
  selectedMembers.value = []
}

const onHide = () => {
  handleReset()
}
</script>

<style scoped>
/* Add any custom styles here */
</style> 