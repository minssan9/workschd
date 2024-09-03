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
import { AgGridVue } from 'ag-grid-vue3'


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

    const task = ref({
      branch_id: null,
      store_id: null,
      additional_info: '',
      task_datetime: '',
      start_time: '',
      end_time: '',
      daily_wage: 0
    });

    const branches = ref([
      { id: 1, name: 'Branch 1' },
      { id: 2, name: 'Branch 2' }
      // 추가 지점 데이터를 여기에 추가하세요.
    ]);

    const stores = ref([
      { id: 1, name: 'Store 1' },
      { id: 2, name: 'Store 2' }
      // 추가 매장 데이터를 여기에 추가하세요.
    ]);

    const rowData = ref([]);
    const columnDefs = ref([
      { headerName: 'Branch', field: 'branch_id' },
      { headerName: 'Store', field: 'store_id' },
      { headerName: 'Additional Info', field: 'additional_info' },
      { headerName: 'Task DateTime', field: 'task_datetime' },
      { headerName: 'Start Time', field: 'start_time' },
      { headerName: 'End Time', field: 'end_time' },
      { headerName: 'Daily Wage', field: 'daily_wage' }
    ]);

    const onGridReady = async () => {
      const response = await fetch('/api/tasks');
      const data = await response.json();
      rowData.value = data;
    };

    const handleSubmit = async () => {
      await fetch('/api/tasks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(task.value)
      });

      await onGridReady();
      handleReset();
    };

    const handleReset = () => {
      task.value = {
        branch_id: null,
        store_id: null,
        additional_info: '',
        task_datetime: '',
        start_time: '',
        end_time: '',
        daily_wage: 0
      };
    };

    return {
      jobs,
      newJob,
      onSubmit
    }
    return {
      task,
      branches,
      stores,
      rowData,
      columnDefs,
      onGridReady,
      handleSubmit,
      handleReset
    };
  }
}
</script>


