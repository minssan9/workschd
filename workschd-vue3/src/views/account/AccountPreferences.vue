<template>
  <q-page padding>
    <div class="row q-col-gutter-md">
      <!-- Profile Settings -->
      <div class="col-12 col-md-6">
        <q-card>
          <q-card-section>
            <div class="text-h6">Profile Settings</div>
          </q-card-section>

          <q-card-section>
            <q-form @submit="handleProfileUpdate">
              <q-input
                v-model="profile.name"
                label="Name"
                :rules="[val => !!val || 'Name is required']"
              />
              <q-input
                v-model="profile.email"
                label="Email"
                type="email"
                :rules="[val => !!val || 'Email is required']"
              />
              <q-input
                v-model="profile.phone"
                label="Phone"
                type="tel"
              />
              <q-btn
                label="Update Profile"
                type="submit"
                color="primary"
                class="q-mt-md"
              />
            </q-form>
          </q-card-section>
        </q-card>
      </div>

      <!-- Notification Settings -->
      <div class="col-12 col-md-6">
        <q-card>
          <q-card-section>
            <div class="text-h6">Notification Settings</div>
          </q-card-section>

          <q-card-section>
            <q-list>
              <q-item tag="label">
                <q-item-section>
                  <q-item-label>Email Notifications</q-item-label>
                  <q-item-label caption>Receive notifications via email</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-toggle v-model="notifications.email" />
                </q-item-section>
              </q-item>

              <q-item tag="label">
                <q-item-section>
                  <q-item-label>Push Notifications</q-item-label>
                  <q-item-label caption>Receive push notifications</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-toggle v-model="notifications.push" />
                </q-item-section>
              </q-item>

              <q-item tag="label">
                <q-item-section>
                  <q-item-label>Schedule Updates</q-item-label>
                  <q-item-label caption>Get notified about schedule changes</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-toggle v-model="notifications.schedule" />
                </q-item-section>
              </q-item>
            </q-list>
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useQuasar } from 'quasar';

const $q = useQuasar();

const profile = ref({
  name: '',
  email: '',
  phone: ''
});

const notifications = ref({
  email: true,
  push: true,
  schedule: true
});

const handleProfileUpdate = async () => {
  try {
    // TODO: Implement API call to update profile
    $q.notify({
      color: 'positive',
      message: 'Profile updated successfully'
    });
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: 'Failed to update profile'
    });
  }
};

// Load user preferences on component mount
const loadUserPreferences = async () => {
  try {
    // TODO: Implement API call to get user preferences
    // For now, using mock data
    profile.value = {
      name: 'John Doe',
      email: 'john@example.com',
      phone: '+1234567890'
    };
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: 'Failed to load user preferences'
    });
  }
};

loadUserPreferences();
</script> 