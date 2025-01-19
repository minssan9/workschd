<template>
  <q-page padding>
    <div class="row q-col-gutter-md">
      <!-- Team Members List -->
      <div class="col-12">
        <q-card>
          <q-card-section>
            <div class="text-h6">Team Members</div>
            <q-btn
              color="primary"
              label="Add Member"
              @click="showAddMemberDialog = true"
              class="q-mt-sm"
            />
          </q-card-section>

          <q-card-section>
            <ag-grid-vue
              class="ag-theme-alpine"
              style="width: 100%; height: 500px;"
              :columnDefs="columnDefs"
              :rowData="members"
              :defaultColDef="defaultColDef"
              @grid-ready="onGridReady"
            />
          </q-card-section>
        </q-card>
      </div>
    </div>

    <!-- Add Member Dialog -->
    <q-dialog v-model="showAddMemberDialog">
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">Add New Member</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit="handleAddMember">
            <q-input
              v-model="newMember.email"
              label="Email"
              type="email"
              :rules="[val => !!val || 'Email is required']"
            />
            <q-select
              v-model="newMember.role"
              :options="roleOptions"
              label="Role"
              :rules="[val => !!val || 'Role is required']"
            />
            <div class="row justify-end q-mt-md">
              <q-btn label="Cancel" color="negative" v-close-popup />
              <q-btn label="Add" type="submit" color="primary" class="q-ml-sm" />
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useQuasar } from 'quasar';
import { AgGridVue } from 'ag-grid-vue3';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';

const $q = useQuasar();
const showAddMemberDialog = ref(false);
const members = ref([]);

const newMember = ref({
  email: '',
  role: null
});

const roleOptions = [
  'ADMIN',
  'MANAGER',
  'EMPLOYEE'
];

const columnDefs = ref([
  { field: 'name', headerName: 'Name', sortable: true, filter: true },
  { field: 'email', headerName: 'Email', sortable: true, filter: true },
  { field: 'role', headerName: 'Role', sortable: true, filter: true },
  { field: 'joinDate', headerName: 'Join Date', sortable: true, filter: true },
  {
    headerName: 'Actions',
    cellRenderer: (params: any) => {
      return `
        <button class="q-btn q-btn-item non-selectable no-outline q-btn--flat q-btn--rectangle text-primary q-btn--actionable q-focusable q-hoverable">
          Edit
        </button>
        <button class="q-btn q-btn-item non-selectable no-outline q-btn--flat q-btn--rectangle text-negative q-btn--actionable q-focusable q-hoverable">
          Remove
        </button>
      `;
    }
  }
]);

const defaultColDef = {
  flex: 1,
  minWidth: 100,
  resizable: true
};

const loadMembers = async () => {
  try {
    // TODO: Implement API call to get team members
    members.value = [
      { name: 'John Doe', email: 'john@example.com', role: 'ADMIN', joinDate: '2024-01-01' },
      { name: 'Jane Smith', email: 'jane@example.com', role: 'MANAGER', joinDate: '2024-01-02' }
    ];
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: 'Failed to load team members'
    });
  }
};

const handleAddMember = async () => {
  try {
    // TODO: Implement API call to add team member
    await loadMembers();
    showAddMemberDialog.value = false;
    $q.notify({
      color: 'positive',
      message: 'Member added successfully'
    });
  } catch (error) {
    $q.notify({
      color: 'negative',
      message: 'Failed to add member'
    });
  }
};

onMounted(() => {
  loadMembers();
});
</script> 