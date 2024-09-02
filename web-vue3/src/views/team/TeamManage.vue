<template>
  <q-page class="q-pa-md">
    <h1>Team Registration System</h1>

    <!-- Worker Registration Form -->
    <q-card v-if="role === 'worker'" class="q-mb-md">
      <q-card-section>
        <h2>Worker Registration</h2>
        <q-form @submit="onWorkerSubmit" class="q-gutter-md">
          <q-input v-model="teamId" label="Team ID" required />
          <q-input v-model="name" label="Your Name" required />
          <q-input v-model="location" label="Your Location" required />
          <q-select
              v-model="preferredPlaces"
              label="Preferred Places"
              multiple
              :options="placeOptions"
              use-chips
              stack-label
              :max-values="3"
          />
          <q-btn label="Submit" type="submit" color="primary" />
        </q-form>
      </q-card-section>
    </q-card>

    <!-- Manager Approval Section -->
    <q-card v-if="role === 'manager'" class="q-mb-md">
      <q-card-section>
        <h2>Manager Approval</h2>
        <q-list bordered separator>
          <q-item v-for="request in requests" :key="request.id">
            <q-item-section>
              <q-item-label>{{ request.name }}</q-item-label>
              <q-item-label caption>
                Team ID: {{ request.teamId }}, Location: {{ request.location }}
              </q-item-label>
              <q-item-label caption>
                Preferred Places: {{ request.preferredPlaces.join(', ') }}
              </q-item-label>
            </q-item-section>
            <q-item-section side>
              <q-btn label="Approve" color="positive" @click="approveRequest(request.id)" />
            </q-item-section>
          </q-item>
        </q-list>
      </q-card-section>
    </q-card>

    <!-- Role Selection -->
    <q-btn-toggle
        v-model="role"
        :options="[
        {label: 'Worker', value: 'worker'},
        {label: 'Manager', value: 'manager'}
      ]"
        class="q-mt-lg"
    />
  </q-page>
</template>

<script>
// - manager
// request list
// approve
import { ref, onMounted } from 'vue'

export default {
  name: 'TeamManage',
  setup() {
    const role = ref('worker')
    const teamId = ref('')
    const name = ref('')
    const location = ref('')
    const preferredPlaces = ref([])
    const placeOptions = ['Place 1', 'Place 2', 'Place 3', 'Place 4', 'Place 5']
    const requests = ref([])

    onMounted(() => {
      // Simulating fetched data for manager
      requests.value = [
        { id: 1, teamId: 'T001', name: 'John Doe', location: 'New York', preferredPlaces: ['Place 1', 'Place 2'] },
        { id: 2, teamId: 'T002', name: 'Jane Smith', location: 'Los Angeles', preferredPlaces: ['Place 3', 'Place 4'] },
      ]
    })

    const onWorkerSubmit = () => {
      console.log('Worker registration submitted:', {
        teamId: teamId.value,
        name: name.value,
        location: location.value,
        preferredPlaces: preferredPlaces.value
      })
      // Here you would typically send this data to your backend
    }

    const approveRequest = (id) => {
      console.log('Request approved:', id)
      // Here you would typically send an approval request to your backend
      // and then remove the request from the list
      requests.value = requests.value.filter(request => request.id !== id)
    }

    return {
      role,
      teamId,
      name,
      location,
      preferredPlaces,
      placeOptions,
      requests,
      onWorkerSubmit,
      approveRequest
    }
  }
}
</script>