<template>
  <q-dialog v-model="dialogVisible" @hide="onHide">
    <q-card style="width: 80vw; max-width: 800px">
      <q-card-section class="bg-primary text-white">
        <div class="text-h6">Approve Join Requests</div>
      </q-card-section>

      <q-card-section v-if="requests.length === 0" class="text-center q-pa-lg">
        <q-icon name="info" size="48px" color="grey-7" />
        <p class="text-grey-7 q-mt-md">No pending requests to approve</p>
      </q-card-section>

      <q-card-section v-else class="q-pa-md">
        <q-list separator>
          <q-item v-for="request in requests" :key="request.id" class="q-py-md">
            <q-item-section avatar>
              <q-avatar color="primary" text-color="white">
                {{ getInitials(request.workerName) }}
              </q-avatar>
            </q-item-section>
            
            <q-item-section>
              <q-item-label class="text-weight-medium">{{ request.workerName }}</q-item-label>
              <q-item-label caption>Request ID: #{{ request.id }}</q-item-label>
            </q-item-section>
            
            <q-item-section side>
              <q-btn
                label="Approve"
                color="positive"
                unelevated
                @click="handleApproveRequest(request)"
              />
            </q-item-section>
          </q-item>
        </q-list>
      </q-card-section>
      
      <q-card-actions align="right" class="bg-grey-1">
        <q-btn flat label="Close" color="primary" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue'

interface JoinRequest {
  id: number
  workerName: string
}

const props = defineProps<{
  modelValue: boolean
  requests: JoinRequest[]
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'approve', request: JoinRequest): void
}>()

const dialogVisible = ref(props.modelValue)

watch(() => props.modelValue, (newValue) => {
  dialogVisible.value = newValue
})

watch(() => dialogVisible.value, (newValue) => {
  emit('update:modelValue', newValue)
})

const handleApproveRequest = (request: JoinRequest) => {
  emit('approve', request)
}

const onHide = () => {
  emit('update:modelValue', false)
}

// Helper function to get initials from name
const getInitials = (name: string): string => {
  if (!name) return '?'
  
  return name
    .split(' ')
    .map(part => part.charAt(0).toUpperCase())
    .slice(0, 2)
    .join('')
}
</script>

<style scoped>
.q-card {
  border-radius: 8px;
}

.q-item {
  border-radius: 4px;
  transition: background-color 0.3s;
}

.q-item:hover {
  background-color: #f5f5f5;
}
</style> 