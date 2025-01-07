<template>
  <q-page class="q-pa-md">
    <h1>Team Management</h1>

    <!-- Worker Registration Form -->
    <q-card v-if="role === 'worker'" class="q-mb-md">
      <q-card-section>
        <h2>Join Team</h2>
        <q-form @submit.prevent="handleJoinTeam" class="q-gutter-md">
          <q-input 
            v-model="joinForm.teamId" 
            label="Team ID" 
            :rules="[val => !!val || 'Team ID is required']"
          />
          <q-input 
            v-model="joinForm.name" 
            label="Your Name"
            :rules="[val => !!val || 'Name is required']"
          />
          <q-input 
            v-model="joinForm.location" 
            label="Your Location"
            :rules="[val => !!val || 'Location is required']"
          />
          <q-select
            v-model="joinForm.preferredPlaces"
            label="Preferred Places"
            multiple
            :options="placeOptions"
            use-chips
            :rules="[
              val => val.length > 0 || 'Select at least one place',
              val => val.length <= 3 || 'Maximum 3 places allowed'
            ]"
          />
          <q-btn 
            label="Submit" 
            type="submit" 
            color="primary"
            :loading="loading"
          />
        </q-form>
      </q-card-section>
    </q-card>

    <!-- Manager Approval Section -->
    <q-card v-if="role === 'manager'" class="q-mb-md">
      <q-card-section>
        <h2>Pending Requests</h2>
        <q-list bordered separator>
          <q-item v-for="request in pendingRequests" :key="request.id">
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
              <q-btn 
                label="Approve" 
                color="positive" 
                @click="handleApproveRequest(request.id)"
                :loading="approvingId === request.id"
              />
            </q-item-section>
          </q-item>
        </q-list>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { apiTeam } from '@/api/modules/api-team';
import { useQuasar } from 'quasar';

const $q = useQuasar();
const role = ref('worker'); // This should come from your auth store
const loading = ref(false);
const approvingId = ref<number | null>(null);

// Form data
const joinForm = ref({
  teamId: '',
  name: '',
  location: '',
  preferredPlaces: []
});

const pendingRequests = ref([]);
const placeOptions = [
  'Downtown Office',
  'Uptown Branch',
  'West Side Store',
  'East Side Store',
  'Central Hub'
];

// Fetch pending requests for managers
const fetchPendingRequests = async () => {
  try {
    const response = await apiTeam.queryTeams({ status: 'pending' });
    pendingRequests.value = response.data;
  } catch (error) {
    console.error('Error fetching pending requests:', error);
    $q.notify({
      type: 'negative',
      message: 'Failed to load pending requests'
    });
  }
};

// Handle team join request
const handleJoinTeam = async () => {
  try {
    loading.value = true;
    await apiTeam.joinTeam(parseInt(joinForm.value.teamId), joinForm.value.name);
    
    $q.notify({
      type: 'positive',
      message: 'Join request submitted successfully'
    });
    
    // Reset form
    joinForm.value = {
      teamId: '',
      name: '',
      location: '',
      preferredPlaces: []
    };
  } catch (error) {
    console.error('Error submitting join request:', error);
    $q.notify({
      type: 'negative',
      message: 'Failed to submit join request'
    });
  } finally {
    loading.value = false;
  }
};

// Handle request approval
const handleApproveRequest = async (requestId: number) => {
  try {
    approvingId.value = requestId;
    await apiTeam.updateTeam(requestId, { status: 'approved' });
    
    // Remove approved request from list
    pendingRequests.value = pendingRequests.value.filter(req => req.id !== requestId);
    
    $q.notify({
      type: 'positive',
      message: 'Request approved successfully'
    });
  } catch (error) {
    console.error('Error approving request:', error);
    $q.notify({
      type: 'negative',
      message: 'Failed to approve request'
    });
  } finally {
    approvingId.value = null;
  }
};

// Load initial data
onMounted(() => {
  if (role.value === 'manager') {
    fetchPendingRequests();
  }
});
</script>

<style scoped>
.q-page {
  max-width: 800px;
  margin: 0 auto;
}
</style>