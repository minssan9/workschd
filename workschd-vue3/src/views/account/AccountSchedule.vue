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

      <div class="col-12">
        <!-- 직원별 선호 요일 및 지점 설정 -->
        <div>
          <h3>Employee Preferences</h3>
          <q-table :rows="employees" :columns="employeeColumns" row-key="id">
            <template v-slot:body-cell-preferredDay="props">
              <q-select v-model="props.row.preferredDay" :options="daysOfWeek" option-value="value" option-label="label"/>
            </template>
            <template v-slot:body-cell-preferredBranch="props">
              <q-select v-model="props.row.preferredBranch" :options="branches" option-value="id" option-label="name"/>
            </template>
          </q-table>
        </div>

        <!-- 배정 실행 버튼 -->
        <q-btn @click="runScheduling" label="Run Scheduling" color="primary" class="q-mt-md"/>

        <!-- 배정 결과 출력 -->
        <div v-if="schedulingResult.length > 0" class="q-mt-lg">
          <h3>Scheduling Results</h3>
          <ag-grid-vue
              class="ag-theme-alpine"
              style="width: 100%; height: 400px;"
              :rowData="schedulingResult"
              :columnDefs="resultColumns"
          ></ag-grid-vue>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useQuasar } from 'quasar';

const $q = useQuasar();


const schedulingResult = ref([]);
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


const employees = ref([
  { id: 1, name: 'John Doe', preferredDay: '', preferredBranch: null },
  { id: 2, name: 'Jane Smith', preferredDay: '', preferredBranch: null }
  // 추가 직원 정보를 여기에 추가
]);
const employeeColumns = [
  { name: 'name', required: true, label: 'Name', align: 'left', field: 'name' },
  { name: 'preferredDay', label: 'Preferred Day', align: 'left', field: 'preferredDay' },
  { name: 'preferredBranch', label: 'Preferred Branch', align: 'left', field: 'preferredBranch' }
];

const runScheduling = () => {
  // 여기에 배정 로직을 구현 (예: Spring Backend와 통신하여 배정 결과를 가져옴)
  schedulingResult.value = [
    { employeeName: 'John Doe', date: '2024-09-10', branch: 'Downtown Branch' },
    { employeeName: 'Jane Smith', date: '2024-09-11', branch: 'Uptown Branch' }
    // 배정된 추가 결과를 여기에 추가
  ];
};

const resultColumns = ref([
  { headerName: 'Employee', field: 'employeeName' },
  { headerName: 'Date', field: 'date' },
  { headerName: 'Branch', field: 'branch' }
]);

loadUserPreferences();
</script> 