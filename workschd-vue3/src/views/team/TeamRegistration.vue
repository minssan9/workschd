<template>
  <q-page class="q-pa-md">
    <h1>Create New Team</h1>
    
    <q-card class="q-mt-md">
      <q-card-section>
        <q-form @submit.prevent="handleCreateTeam" class="q-gutter-md">
          <q-input
            v-model="teamForm.name"
            label="Team Name"
            :rules="[val => !!val || 'Team name is required']"
          />
          
          <q-input
            v-model="teamForm.region"
            label="Region"
            :rules="[val => !!val || 'Region is required']"
          />
          
          <q-select
            v-model="teamForm.scheduleType"
            label="Schedule Type"
            :options="scheduleTypes"
            :rules="[val => !!val || 'Schedule type is required']"
          />
          
          <q-btn 
            label="Create Team" 
            type="submit" 
            color="primary"
            :loading="loading"
          />
        </q-form>
      </q-card-section>
    </q-card>

    <q-card v-if="inviteHash" class="q-mt-md">
      <q-card-section>
        <h2>Team Invitation Link</h2>
        <p>Share this link with team members:</p>
        <q-input
          v-model="inviteUrl"
          readonly
          stack-label
        >
          <template v-slot:append>
            <q-btn
              flat
              round
              icon="content_copy"
              @click="copyInviteUrl"
            />
          </template>
        </q-input>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { apiTeam } from '@/api/modules/api-team';
import { useQuasar } from 'quasar';

const $q = useQuasar();
const loading = ref(false);
const inviteHash = ref('');

const teamForm = ref({
  name: '',
  region: '',
  scheduleType: null
});

const scheduleTypes = [
  'WEEKLY',
  'BI_WEEKLY',
  'MONTHLY'
];

const inviteUrl = computed(() => {
  if (!inviteHash.value) return '';
  return `${window.location.origin}/team/join/${inviteHash.value}`;
});

const handleCreateTeam = async () => {
  try {
    loading.value = true;
    const response = await apiTeam.createTeam(teamForm.value);
    inviteHash.value = response.data.invitationHash;
    
    $q.notify({
      type: 'positive',
      message: 'Team created successfully'
    });
  } catch (error) {
    console.error('Error creating team:', error);
    $q.notify({
      type: 'negative',
      message: 'Failed to create team'
    });
  } finally {
    loading.value = false;
  }
};

const copyInviteUrl = () => {
  navigator.clipboard.writeText(inviteUrl.value)
    .then(() => {
      $q.notify({
        type: 'positive',
        message: 'Invitation link copied to clipboard'
      });
    })
    .catch(() => {
      $q.notify({
        type: 'negative',
        message: 'Failed to copy invitation link'
      });
    });
};
</script>

<style scoped>
.q-page {
  max-width: 800px;
  margin: 0 auto;
}
</style>