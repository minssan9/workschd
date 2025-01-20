<template>
  <q-page padding>
    <h5>Jobs of Today</h5>

    <q-list bordered separator>
      <q-item v-for="job in jobs" :key="job.id">
        <q-item-section>
          <q-item-label>{{ job.place }}</q-item-label>
          <q-item-label caption>
            Slots: {{ job.slots }} |
            Time: {{ job.startTime }} - {{ job.endTime }}
          </q-item-label>
        </q-item-section>

        <q-item-section side v-if="userRole === 'worker'">
          <q-btn
              :label="job.joined ? 'Cancel Join' : 'Join'"
              :color="job.joined ? 'negative' : 'primary'"
              @click="toggleJoin(job)"
              :disabled="job.joinRequestStatus === 'pending'"
          />
        </q-item-section>

        <q-item-section side v-if="userRole === 'manager' && job.joinRequests.length > 0">
          <q-btn
              label="Approve Joins"
              color="positive"
              @click="showApprovalDialog(job)"
          />
        </q-item-section>
      </q-item>
    </q-list>

    <q-dialog v-model="approvalDialog">
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">Approve Join Requests</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-list dense>
            <q-item v-for="request in selectedJob?.joinRequests" :key="request.id">
              <q-item-section>
                <q-item-label>{{ request.workerName }}</q-item-label>
              </q-item-section>
              <q-item-section side>
                <q-btn
                    label="Approve"
                    color="positive"
                    dense
                    @click="approveJoinRequest(request)"
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
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useUserStore } from '@/stores/modules/store_user'

interface JoinRequest {
  id: number
  workerId: number
  workerName: string
}

interface Job {
  id: number
  place: string
  slots: number
  startTime: string
  endTime: string
  joined: boolean
  joinRequestStatus: string | null
  joinRequests: JoinRequest[]
}

const userStore = useUserStore()
const userRole = ref('worker') // This should come from userStore in production

const jobs = ref<Job[]>([
  {
    id: 1,
    place: 'Office A',
    slots: 5,
    startTime: '09:00',
    endTime: '17:00',
    joined: false,
    joinRequestStatus: null,
    joinRequests: [
      { id: 1, workerId: 101, workerName: 'John Doe' },
      { id: 2, workerId: 102, workerName: 'Jane Smith' },
    ]
  },
  {
    id: 2,
    place: 'Store B',
    slots: 3,
    startTime: '10:00',
    endTime: '18:00',
    joined: false,
    joinRequestStatus: null,
    joinRequests: []
  },
])

const approvalDialog = ref(false)
const selectedJob = ref<Job | null>(null)

const toggleJoin = (job: Job) => {
  if (job.joined) {
    job.joined = false
    job.joinRequestStatus = null
  } else {
    job.joinRequestStatus = 'pending'
    // Here you would make an API call to request joining
  }
}

const showApprovalDialog = (job: Job) => {
  selectedJob.value = job
  approvalDialog.value = true
}

const approveJoinRequest = (request: JoinRequest) => {
  if (!selectedJob.value) return

  // Here you would make an API call to approve the request
  console.log(`Approved join request for worker: ${request.workerName}`)
  
  // Remove the request from the list after approval
  const index = selectedJob.value.joinRequests.findIndex(r => r.id === request.id)
  if (index !== -1) {
    selectedJob.value.joinRequests.splice(index, 1)
  }
}
</script>