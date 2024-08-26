<template>
  <q-page padding>
    <h5>Job List of Today</h5>

    <q-list bordered separator>
      <q-item v-for="job in jobs" :key="job.id" clickable v-ripple>
        <q-item-section>
          <q-item-label>{{ job.place }}</q-item-label>
          <q-item-label caption>
            Slots: {{ job.slots }} |
            Time: {{ job.startTime }} - {{ job.endTime }}
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-list>

    <h5 class="q-mt-lg">Register New Job</h5>

    <q-form @submit="onSubmit" class="q-gutter-md">
      <q-input
          filled
          v-model="newJob.place"
          label="Place"
          :rules="[val => !!val || 'Place is required']"
      />

      <q-input
          filled
          type="number"
          v-model="newJob.slots"
          label="Slots"
          :rules="[val => val > 0 || 'Slots must be greater than 0']"
      />

      <q-input
          filled
          type="time"
          v-model="newJob.startTime"
          label="Start Time"
          :rules="[val => !!val || 'Start time is required']"
      />

      <q-input
          filled
          type="time"
          v-model="newJob.endTime"
          label="End Time"
          :rules="[val => !!val || 'End time is required']"
      />

      <q-btn label="Register Job" type="submit" color="primary"/>
    </q-form>
  </q-page>
</template>

<script>
// job list of today
// - job list registering form
//
// manager
// - register jobs
// 1. place
// 2. slots
// 3. start time
// 4. end time

import { ref } from 'vue'

export default {
  name: 'JobList',
  setup() {
    const jobs = ref([
      { id: 1, place: 'Office A', slots: 5, startTime: '09:00', endTime: '17:00' },
      { id: 2, place: 'Store B', slots: 3, startTime: '10:00', endTime: '18:00' },
    ])

    const newJob = ref({
      place: '',
      slots: null,
      startTime: '',
      endTime: ''
    })

    const onSubmit = () => {
      jobs.value.push({
        id: jobs.value.length + 1,
        ...newJob.value
      })
      // Reset form
      newJob.value = {
        place: '',
        slots: null,
        startTime: '',
        endTime: ''
      }
    }

    return {
      jobs,
      newJob,
      onSubmit
    }
  }
}
</script>


