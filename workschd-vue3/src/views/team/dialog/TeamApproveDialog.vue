<template>
  <q-dialog v-model="isOpen">
    <q-card style="min-width: 350px">
      <q-card-section>
        <div class="text-h6">Approve Join Requests</div>
      </q-card-section>

      <q-card-section class="q-pt-none">
        <q-list dense>
          <q-item v-for="request in selectedTeam?.joinRequests" :key="request.id">
            <q-item-section>
              <q-item-label>{{ request.name }}</q-item-label>
              <q-item-label caption>{{ request.location }}</q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-btn
                label="Approve"
                color="positive"
                dense
                @click="handleApprove(request)"
              />
            </q-item-section>
          </q-item>
        </q-list>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="Close" color="primary" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { useQuasar } from 'quasar'

const $q = useQuasar()
const emit = defineEmits(['request-approved'])
const isOpen = defineModel('modelValue')

interface JoinRequest {
  id: number
  name: string
  location: string
}

interface Team {
  id: number
  joinRequests: JoinRequest[]
}

const props = defineProps<{
  selectedTeam: Team | null
}>()

const handleApprove = async (request: JoinRequest) => {
  try {
    if (!props.selectedTeam) return

    await fetch(`/api/teams/${props.selectedTeam.id}/approve`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(request)
    })

    emit('request-approved', { teamId: props.selectedTeam.id, request })
    $q.notify({ type: 'positive', message: 'Request approved successfully' })
  } catch (error) {
    $q.notify({ type: 'negative', message: 'Failed to approve request' })
  }
}
</script> 