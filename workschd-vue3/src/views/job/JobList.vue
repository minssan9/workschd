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



    <q-form @submit="handleSubmit" @reset="handleReset">
      <q-select
          v-model="task.branch_id"
          label="Branch"
          :options="branches"
          option-value="id"
          option-label="name"
          required
      />
      <q-select
          v-model="task.store_id"
          label="Store"
          :options="stores"
          option-value="id"
          option-label="name"
      />
      <q-input v-model="task.additional_info" label="Additional Info" />
      <q-input v-model="task.task_datetime" label="Task DateTime" type="datetime-local" />
      <q-input v-model="task.start_time" label="Start Time" type="time" />
      <q-input v-model="task.end_time" label="End Time" type="time" />
      <q-input v-model="task.daily_wage" label="Daily Wage" type="number" />
      <div class="q-mt-md">
        <q-btn label="Submit" type="submit" color="primary" />
        <q-btn label="Reset" type="reset" color="secondary" />
      </div>
    </q-form>

    <div class="q-mt-lg">
      <ag-grid-vue
          class="ag-theme-alpine"
          style="width: 100%; height: 400px;"
          :rowData="rowData"
          :columnDefs="columnDefs"
          @grid-ready="onGridReady"
      ></ag-grid-vue>
    </div>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { apiTask } from '@/api/modules/api-task';
import { apiSchedule } from '@/api/modules/api-schedule';

const tasks = ref([]);
const loading = ref(false);

const fetchTasks = async () => {
  try {
    loading.value = true;
    const response = await apiTask.queryTasks({});
    tasks.value = response.data;
  } catch (error) {
    console.error('Error fetching tasks:', error);
  } finally {
    loading.value = false;
  }
};

const createTask = async (taskData: any) => {
  try {
    await apiTask.createTask(taskData);
    await fetchTasks(); // Refresh the list
  } catch (error) {
    console.error('Error creating task:', error);
  }
};

onMounted(() => {
  fetchTasks();
});
</script>


