<template>
  <q-dialog 
    v-model="isOpen" 
    :maximized="dialogMode === 'view'" 
    @hide="handleDialogHide"
    :transition-show="transitionShow" 
    :transition-hide="transitionHide" 
  >
    <q-card :class="{
      'task-details-dialog': dialogMode === 'view',
      'dialog-card': dialogMode === 'add',
      'approval-dialog': dialogMode === 'approval'
    }">
      <!-- Header -->
      <q-card-section class="header-section q-pb-none">
        <div class="row items-center justify-between">
          <div class="text-h5 text-weight-bold">
            <template v-if="dialogMode === 'view'">{{ task?.title }}</template>
            <template v-else-if="dialogMode === 'edit'">{{ t('task.edit', 'Edit Task') }}</template>
            <template v-else-if="dialogMode === 'add'">{{ t('task.register', 'Register New Task') }}</template>
            <template v-else-if="dialogMode === 'approval'">{{ t('task.approvalRequests', 'Approval Requests') }}</template>
            <template v-else-if="dialogMode === 'join'">{{ t('task.joinRequest', '작업 참여 신청') }}</template>
            <template v-else-if="dialogMode === 'cancel'">{{ t('task.cancelTask', '작업 취소') }}</template>
          </div>
          <q-btn icon="close" flat round dense v-close-popup class="close-btn" />
        </div>
      </q-card-section>

      <!-- Main Content -->
      <template v-if="dialogMode !== 'approval' && dialogMode !== 'join' && dialogMode !== 'cancel'">
        <q-card-section class="content-section q-pt-sm">
          <q-form @submit.prevent="handleFormSubmit" class="form-container">
            <div class="form-grid q-col-gutter-md">
              <!-- Basic Info Card -->
              <q-card flat bordered class="form-card">
                <q-card-section class="q-pa-sm">
                  <div class="text-subtitle1 q-mb-sm">{{ t('events.basicInfo', 'Basic Information') }}</div>
                  <div class="q-gutter-y-sm">
                    <q-input
                      v-model="currentForm.title"
                      :label="t('task.title', 'Title')"
                      :disable="dialogMode === 'view'"
                      outlined
                      dense
                      required
                      :rules="[val => !!val || t('validation.required', 'Field is required')]"
                    />
                    <q-select 
                      v-model="currentForm.shopId" 
                      :options="shopOptions" 
                      :label="t('task.shop', 'Shop')" 
                      :disable="dialogMode === 'view'"
                      outlined
                      dense
                      emit-value 
                      map-options 
                      :rules="[val => val !== null || t('validation.required', 'Field is required')]" 
                    />
                    <q-select 
                      v-model="currentForm.status" 
                      :options="statusOptions" 
                      :label="t('task.status', 'Status')" 
                      :disable="dialogMode === 'view'"
                      outlined
                      dense
                      required 
                      :rules="[val => !!val || t('validation.required', 'Field is required')]" 
                    />
                    <q-toggle 
                      v-model="currentForm.active" 
                      :label="t('task.active', 'Active')" 
                      :disable="dialogMode === 'view'" 
                      color="primary"
                      dense
                    />
                  </div>
                </q-card-section>
              </q-card>

              <!-- Time & Assignment Card -->
              <q-card flat bordered class="form-card">
                <q-card-section class="q-pa-sm">
                  <div class="text-subtitle1 q-mb-sm">{{ t('events.timeAndAssignment', 'Time & Assignment') }}</div>
                  <div class="q-gutter-y-sm">
                    <q-input 
                      v-model="currentForm.startDateTime" 
                      type="datetime-local" 
                      :label="t('task.startDateTime', 'Start Date & Time')" 
                      :disable="dialogMode === 'view'"
                      outlined
                      dense
                      required 
                      :rules="[val => !!val || t('validation.required', 'Field is required')]" 
                    />
                    <q-input 
                      v-model="currentForm.endDateTime" 
                      type="datetime-local" 
                      :label="t('task.endDateTime', 'End Date & Time')" 
                      :disable="dialogMode === 'view'"
                      outlined
                      dense
                      required 
                      :rules="[val => !!val || t('validation.required', 'Field is required')]" 
                    />
                    <q-input 
                      v-model.number="currentForm.workerCount" 
                      type="number" 
                      min="1" 
                      :label="t('task.workerCount', 'Worker Count')" 
                      :disable="dialogMode === 'view'"
                      outlined
                      dense
                      required 
                      :rules="[
                        val => !!val || t('validation.required', 'Field is required'),
                        val => val > 0 || t('validation.min', 'Must be greater than 0')
                      ]" 
                    />
                  </div>
                </q-card-section>
              </q-card>

              <!-- Description Card -->
              <q-card flat bordered class="form-card">
                <q-card-section class="q-pa-sm">
                  <div class="text-subtitle1 q-mb-sm">{{ t('events.description', 'Description') }}</div>
                  <q-input
                    v-model="currentForm.description"
                    type="textarea"
                    :label="t('task.description', 'Description')"
                    :disable="dialogMode === 'view'"
                    outlined
                    dense
                    rows="3"
                    autogrow
                  />
                </q-card-section>
              </q-card>
            </div>

            <!-- Map Section -->
            <div class="map-section q-mt-md">
              <q-card flat bordered class="form-card">
                <q-card-section class="q-pa-sm">
                  <div class="text-subtitle1 q-mb-sm">{{ t('task.location', 'Location') }}</div>
                  <div ref="mapContainer" style="height: 200px;" class="map-container"></div>
                </q-card-section>
              </q-card>
            </div>

            <!-- Action Buttons -->
            <div class="action-buttons row justify-end q-mt-lg q-gutter-sm">
              <template v-if="dialogMode === 'view'">
                <q-btn 
                  v-if="showJoinBtn" 
                  color="primary" 
                  :label="t('task.joinRequest', '참여 신청')" 
                  @click="showJoinRequestDialog" 
                  unelevated
                />
                <q-btn 
                  v-if="showEditBtn" 
                  color="secondary" 
                  :label="t('task.edit', '편집')" 
                  @click="dialogMode = 'edit'" 
                  unelevated
                />
                <q-btn 
                  v-if="showApprovalBtn" 
                  color="info" 
                  :label="t('task.approveRequests', '승인 요청')" 
                  @click="dialogMode = 'approval'" 
                  unelevated
                />
                <q-btn 
                  v-if="showCancelBtn" 
                  color="negative" 
                  :label="t('task.cancel', '작업 취소')" 
                  @click="showCancelDialog" 
                  unelevated
                />
              </template>
              <template v-else>
                <q-btn 
                  flat 
                  :label="t('common.cancel', 'Cancel')" 
                  color="grey-7" 
                  @click="handleCancel" 
                />
                <q-btn 
                  v-if="dialogMode === 'edit'" 
                  :label="t('common.save', 'Save')" 
                  color="primary" 
                  type="submit" 
                  :loading="isSubmitting" 
                  unelevated
                />
                <q-btn 
                  v-else-if="dialogMode === 'add'" 
                  :label="t('common.submit', 'Submit')" 
                  color="primary" 
                  type="submit" 
                  :loading="isSubmitting" 
                  unelevated
                />
              </template>
            </div>
          </q-form>
        </q-card-section>

        <!-- Task Employees Grid -->
        <q-card-section v-if="dialogMode === 'view' && task?.id" class="employees-section">
          <div class="row justify-between items-center q-mb-lg">
            <div class="text-h6">{{ t('events.taskEmployees', 'Task Employees') }}</div>
            <q-btn 
              v-if="!isWorker"
              color="primary" 
              :label="t('task.addEmployee', 'Add Employee')"
              @click="showAddEmployeeDialog = true" 
              unelevated
              rounded
            />
          </div>
          <TaskEmployeeGrid 
            :task-id="task.id"
            @employee-selected="handleEmployeeSelected"
            class="employee-grid"
          />
        </q-card-section>
      </template>

      <!-- Approval Mode -->
      <template v-else-if="dialogMode === 'approval'">
        <q-card-section class="approval-section">
          <div v-if="!requests || requests.length === 0" class="text-center q-pa-xl text-grey-6">
            <q-icon name="inbox" size="48px" class="q-mb-md" />
            <div class="text-h6">No pending requests</div>
          </div>
          <div v-else>
            <q-list separator class="request-list">
              <q-item v-for="request in requests" :key="request.id" class="request-item">
                <q-item-section>
                  <q-item-label class="text-subtitle1">{{ request.workerName }}</q-item-label>
                  <q-item-label caption>
                    <q-icon name="event" size="xs" class="q-mr-xs" />
                    {{ formatDateTime(request.requestDate) }}
                  </q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-btn 
                    color="positive" 
                    label="Approve" 
                    @click="approveRequest(request)" 
                    unelevated
                    rounded
                  />
                </q-item-section>
              </q-item>
            </q-list>
          </div>
        </q-card-section>
        <q-card-actions align="right" class="q-pa-md">
          <q-btn flat label="Back" color="grey-7" @click="dialogMode = 'view'" />
        </q-card-actions>
      </template>

      <!-- Join Request Mode -->
      <template v-else-if="dialogMode === 'join'">
        <q-card-section class="join-section text-center">
          <q-icon name="how_to_reg" size="64px" color="primary" class="q-mb-md" />
          <div class="text-h6 q-mb-md">작업 참여 신청</div>
          <p class="text-subtitle1">다음 작업에 참여 신청을 하시겠습니까?</p>
          <p class="text-h6 text-primary q-mb-sm">{{ task?.title }}</p>
          <p class="text-subtitle1">
            {{ formatDateRange(task?.startDateTime, task?.endDateTime) }}
            <br>
            {{ formatTimeRange(task?.startDateTime, task?.endDateTime) }}
          </p>
        </q-card-section>
        <q-card-actions align="center" class="q-pa-md">
          <q-btn flat label="취소" color="grey-7" @click="dialogMode = 'view'" />
          <q-btn 
            color="primary" 
            label="신청하기" 
            @click="submitJoinRequest" 
            :loading="isSubmitting" 
            unelevated
          />
        </q-card-actions>
      </template>

      <!-- Cancel Task Mode -->
      <template v-else-if="dialogMode === 'cancel'">
        <q-card-section class="cancel-section text-center">
          <q-icon name="warning" size="64px" color="negative" class="q-mb-md" />
          <div class="text-h6 q-mb-md">작업 취소</div>
          <p class="text-subtitle1">다음 작업을 취소하시겠습니까?</p>
          <p class="text-h6 text-negative q-mb-sm">{{ task?.title }}</p>
          <p class="text-subtitle1">
            {{ formatDateRange(task?.startDateTime, task?.endDateTime) }}
            <br>
            {{ formatTimeRange(task?.startDateTime, task?.endDateTime) }}
          </p>
        </q-card-section>
        <q-card-actions align="center" class="q-pa-md">
          <q-btn flat label="돌아가기" color="grey-7" @click="dialogMode = 'view'" />
          <q-btn 
            color="negative" 
            label="취소하기" 
            @click="submitCancelTask" 
            :loading="isSubmitting" 
            unelevated
          />
        </q-card-actions>
      </template>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick, onBeforeUnmount } from 'vue';
import { useI18n } from 'vue-i18n';
import { useQuasar, date } from 'quasar';
import { useUserStore } from '@/stores/modules/store_user';
import { Task, Shop, TaskStatus, JoinRequest, getTaskStatusLabel, getTaskStatusColor } from '@/types';
import TaskEmployeeGrid from '../grid/TaskEmployeeGrid.vue'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'

type DialogMode = 'view' | 'edit' | 'add' | 'approval' | 'join' | 'cancel';

const props = defineProps<{
  modelValue: boolean;
  task: Task | null;
  shops?: Shop[];
  requests?: JoinRequest[];
  isSubmitting?: boolean;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void;
  (e: 'submit', task: Task): void;
  (e: 'add', task: Task): void;
  (e: 'join-request', task: Task): void;
  (e: 'cancel', task: Task): void;
  (e: 'approve', request: JoinRequest): void;
  (e: 'reset'): void;
}>();

const $q = useQuasar();
const { t } = useI18n();
const userStore = useUserStore();

const dialogMode = ref<DialogMode>('view');
const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
});
const isSubmitting = computed(() => props.isSubmitting || false);

// Transition effects based on dialog mode
const transitionShow = computed(() => dialogMode.value === 'view' ? 'slide-up' : 'fade');
const transitionHide = computed(() => dialogMode.value === 'view' ? 'slide-down' : 'fade');

// Task form for edit mode
const form = ref<Task>({
  title: '',
  description: '',
  workerCount: 1,
  startDateTime: '',
  endDateTime: '',
  status: TaskStatus.SCHEDULED,
  teamId: null,
  shopId: null,
  active: true
});

// For Add Task Dialog
const addForm = ref<Task>({
  title: '',
  description: '',
  workerCount: 1,
  startDateTime: '',
  endDateTime: '',
status: TaskStatus.SCHEDULED,
  teamId: userStore.user?.teamId || 0,
  shopId: null,
  active: true,
  taskEmployees: null
});

// Split date and time for easier input in add mode
const startDate = ref(new Date().toISOString().split('T')[0]);
const startTime = ref('08:00');
const endDate = ref(new Date().toISOString().split('T')[0]);
const endTime = ref('17:00');

// Shop selection for add mode
const selectedShop = ref(null);

// Combine date and time into ISO strings for add mode
const updateStartDateTime = () => {
  addForm.value.startDateTime = `${startDate.value}T${startTime.value}:00`;
};

const updateEndDateTime = () => {
  addForm.value.endDateTime = `${endDate.value}T${endTime.value}:00`;
};

// TaskStatus options for the dropdown
const statusOptions = [
  { label: getTaskStatusLabel(TaskStatus.SCHEDULED), value: TaskStatus.SCHEDULED },
  { label: getTaskStatusLabel(TaskStatus.IN_PROGRESS), value: TaskStatus.IN_PROGRESS },
  { label: getTaskStatusLabel(TaskStatus.COMPLETED), value: TaskStatus.COMPLETED },
  { label: getTaskStatusLabel(TaskStatus.CANCELLED), value: TaskStatus.CANCELLED }
];

// Shop options
const shopOptions = computed(() => {
  if (!props.shops || props.shops.length === 0) return [];
  
  return props.shops.map(shop => ({
    label: shop.name,
    value: shop.id
  }));
});

// Permissions for task actions
const canRequestToJoin = computed(() => {
  if (!props.task) return false;
  
  // Check if task is active and user is logged in
  return (
    (props.task.status === TaskStatus.SCHEDULED || 
     props.task.status === TaskStatus.IN_PROGRESS) && 
    !!userStore.user?.accountId &&
    (props.task.taskEmployees?.length || 0) < props.task.workerCount
  );
});

const canManageTask = computed(() => {
  if (!props.task) return false;
  
  // Allow editing by team managers
  return userStore.isManager;
});

const canCancelTask = computed(() => {
  if (!props.task) return false;
  
  // Only allow cancellation by team managers for scheduled tasks
  return (
    props.task.status === TaskStatus.SCHEDULED && 
    userStore.isManager
  );
});

const hasApprovalRequests = computed(() => {
  return props.requests && props.requests.length > 0;
});

// Requests accessor
const requests = computed(() => props.requests || []);

// Helper function to format date range
function formatDateRange(start?: string, end?: string): string {
  if (!start || !end) return '-';
  
  const startDate = date.formatDate(start, 'YYYY.MM.DD');
  const endDate = date.formatDate(end, 'YYYY.MM.DD');
  
  if (startDate === endDate) {
    return startDate;
  }
  return `${startDate} - ${endDate}`;
}

// Helper function to format time range
function formatTimeRange(start?: string, end?: string): string {
  if (!start || !end) return '-';
  
  const startTime = date.formatDate(start, 'HH:mm');
  const endTime = date.formatDate(end, 'HH:mm');
  
  return `${startTime} - ${endTime}`;
}

// Helper function to format date time
function formatDateTime(dateTimeString?: string): string {
  if (!dateTimeString) return 'N/A';
  const date = new Date(dateTimeString);
  return date.toLocaleString();
}

// Watch for task changes to update the form
watch(() => props.task, (newTask) => {
  if (newTask) {
    form.value = { ...newTask };
    
    // Adjust datetime format for HTML datetime-local input
    if (form.value.startDateTime) {
      form.value.startDateTime = form.value.startDateTime.slice(0, 16);
    }
    if (form.value.endDateTime) {
      form.value.endDateTime = form.value.endDateTime.slice(0, 16);
    }
  }
}, { immediate: true });

// Reset edit mode when dialog closes
watch(() => isOpen.value, (newValue) => {
  if (!newValue) {
    dialogMode.value = 'view';
  }
});

// Watch selected shop and update shopId for add mode
watch(() => selectedShop.value, (newValue) => {
  if (newValue) {
    addForm.value.shopId = newValue.id;
  } else {
    addForm.value.shopId = null;
  }
});

// Form submission for edit mode
const onEditSubmit = () => {
  try {
    emit('submit', form.value);
    dialogMode.value = 'view';
  } catch (error) {
    $q.notify({
      type: 'negative',
      message: t('task.updateError', 'Failed to update task')
    });
  }
};

// Form submission for add mode
const onAddSubmit = () => {
  try {
    // Ensure dates are up to date
    updateStartDateTime();
    updateEndDateTime();
    
    emit('add', addForm.value);
    // Reset will happen when dialog closes
  } catch (error) {
    $q.notify({ type: 'negative', message: t('task.addError', 'Failed to add task') });
  }
};

// Reset form for add mode
const handleReset = () => {
  // Reset date/time values
  startDate.value = new Date().toISOString().split('T')[0];
  startTime.value = '08:00';
  endDate.value = new Date().toISOString().split('T')[0];
  endTime.value = '17:00';
  
  // Reset shop selection
  selectedShop.value = null;
  
  // Reset task data
  addForm.value = {    
    title: '',
    description: '',
    workerCount: 1,
    startDateTime: `${startDate.value}T${startTime.value}:00`,
    endDateTime: `${endDate.value}T${endTime.value}:00`,
    status: TaskStatus.SCHEDULED,
    teamId: userStore.user?.teamId || 0,
    shopId: null,
    active: true,
    taskEmployees: null
  };
};

// Show join request dialog
const showJoinRequestDialog = () => {
  dialogMode.value = 'join';
};

// Show cancel dialog
const showCancelDialog = () => {
  dialogMode.value = 'cancel';
};

// Submit join request
const submitJoinRequest = () => {
  if (props.task) {
    emit('join-request', props.task);
    dialogMode.value = 'view';
  }
};

// Submit cancel task
const submitCancelTask = () => {
  if (props.task) {
    emit('cancel', props.task);
    dialogMode.value = 'view';
  }
};

// Approve request
const approveRequest = (request: JoinRequest) => {
  emit('approve', request);
};

// Button visibility computed properties
const showJoinBtn = computed(() => dialogMode.value === 'view' && canRequestToJoin.value)
const showEditBtn = computed(() => dialogMode.value === 'view' && canManageTask.value)
const showApprovalBtn = computed(() => dialogMode.value === 'view' && canManageTask.value && hasApprovalRequests.value)
const showCancelBtn = computed(() => dialogMode.value === 'view' && canCancelTask.value)

// View items for display
const viewItems = computed(() => [
  { label: t('task.status', '상태'), value: '', type: 'chip' },
  { label: t('task.shop', '매장'), value: props.task?.shopName || 'N/A' },
  { label: t('task.team', '팀'), value: props.task?.teamName || 'N/A' },
  { label: t('task.date', '일자'), value: formatDateRange(props.task?.startDateTime, props.task?.endDateTime) },
  { label: t('task.time', '시간'), value: formatTimeRange(props.task?.startDateTime, props.task?.endDateTime) },
  { label: t('task.workerCount', '필요 인원'), value: `${props.task?.workerCount} ${t('task.people', '명')}` },
  { label: t('task.currentWorkers', '현재 인원'), value: `${props.task?.taskEmployees?.length || 0} ${t('task.people', '명')}` }
])

// Override dialogMode to 'add' when no task is provided
watch(() => props.modelValue, (isOpen) => {
  if (isOpen && !props.task?.id) {
    dialogMode.value = 'add';
  }
}, { immediate: true });

const showAddEmployeeDialog = ref(false)
const isWorker = false // Replace with actual logic if needed
function handleEmployeeSelected(employee: any) {
  // Implement employee selection logic or emit event
}

const currentForm = computed<Task>({
  get() {
    if (dialogMode.value === 'add') return addForm.value
    return form.value
  },
  set(val: Task) {
    if (dialogMode.value === 'add') addForm.value = val
    else form.value = val
  }
})

function handleFormSubmit() {
  if (dialogMode.value === 'add') {
    updateStartDateTime();
    updateEndDateTime();
    emit('add', addForm.value);
  } else if (dialogMode.value === 'edit') {
    emit('submit', form.value);
    dialogMode.value = 'view';
  }
}

function handleCancel() {
  if (dialogMode.value === 'edit') dialogMode.value = 'view'
  else isOpen.value = false
}

const map = ref<any>(null)
const marker = ref<any>(null)
const mapContainer = ref<HTMLElement | null>(null)

// Initialize map when shop changes
watch(() => currentForm.value.shopId, async (newShopId) => {
  // if (!newShopId) {
  //   if (map.value) {
  //     map.value.remove()
  //     map.value = null
  //     marker.value = null
  //   }
  //   return
  // }
  if (map.value) {
    map.value.remove()
    map.value = null
    marker.value = null
  }
  
  const shop = props.shops?.find(s => s.id === newShopId)
  if (!shop) return

  const lat = shop.latitude ?? 37.5665 // Default to Seoul if no coordinates
  const lng = shop.longitude ?? 126.9780

  await nextTick()
  
  try {
    if (!mapContainer.value) return

    if (!map.value) {
      const newMap = L.map(mapContainer.value, {
        center: [lat, lng],
        zoom: 15,
        layers: [
          L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 19
          })
        ]
      })
      map.value = newMap
    } else {
      map.value.setView([lat, lng], 15)
    }
    
    if (marker.value) {
      marker.value.setLatLng([lat, lng])
    } else if (map.value) {
      const newMarker = L.marker([lat, lng])
      newMarker.addTo(map.value)
      marker.value = newMarker
    }

    // Add popup with shop info
    if (marker.value) {
      marker.value.bindPopup(`
        <b>${shop.name}</b><br>
        ${shop.region || ''}
      `).openPopup()
    }

    // Trigger a resize event after a short delay to ensure proper rendering
    setTimeout(() => { map.value?.invalidateSize() }, 100)
  } catch (error) {
    console.error('Error initializing map:', error)
  }
})

// Initialize map when dialog opens
watch(() => isOpen.value, async (newValue) => {
  if (newValue && currentForm.value.shopId) {
    await nextTick()
    if (map.value) {
      setTimeout(() => { map.value?.invalidateSize() }, 100)
    }
  }
})

// Add cleanup
onBeforeUnmount(() => {
  if (map.value) {
    map.value.remove()
    map.value = null
    marker.value = null
  }
})

// Add these near other refs
const hasUnsavedChanges = ref(false)

// Update handleDialogHide function
function handleDialogHide() {
  // Only show confirmation if in edit/add mode and has unsaved changes
  if ((dialogMode.value === 'edit' || dialogMode.value === 'add') && hasUnsavedChanges.value) {
    $q.dialog({
      title: t('common.confirm', 'Confirm'),
      message: t('common.unsavedChanges', 'You have unsaved changes. Are you sure you want to close?'),
      cancel: true,
      persistent: true
    }).onOk(() => {
      hasUnsavedChanges.value = false
      isOpen.value = false
      if (dialogMode.value === 'edit') {
        dialogMode.value = 'view'
      }
      emit('reset')
    })
    return
  }
  
  // For other modes, just close
  isOpen.value = false
  if (dialogMode.value === 'edit') {
    dialogMode.value = 'view'
  }
  emit('reset')
}

// Watch for form changes
watch(() => currentForm.value, () => {
  if (dialogMode.value === 'edit' || dialogMode.value === 'add') {
    hasUnsavedChanges.value = true
  }
}, { deep: true })

// Reset unsaved changes flag when dialog closes
watch(() => isOpen.value, (newValue) => {
  if (!newValue) {
    hasUnsavedChanges.value = false
    dialogMode.value = 'view'
  }
})
</script>

<style lang="scss" scoped>
.task-details-dialog {
  width: 100%;
  max-width: 1200px;
  height: 100%;
  max-height: 90vh;
  border-radius: 16px;
  background: #ffffff;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  overflow: hidden;

  .header-section {
    background: linear-gradient(135deg, var(--q-primary) 0%, darken($primary, 15%) 100%);
    color: white;
    padding: 16px;

    .close-btn {
      color: white;
    }
  }

  .content-section {
    padding: 16px;
    height: calc(100% - 64px);
    overflow-y: auto;
  }

  .form-card {
    border-radius: 8px;
    border: 1px solid rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;

    &:hover {
      border-color: var(--q-primary);
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    }
  }

  .map-container {
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  }

  .employee-grid {
    border-radius: 12px;
    border: 1px solid rgba(0, 0, 0, 0.08);
    overflow: hidden;
  }
}

.dialog-card {
  width: 900px;
  max-width: 95vw;
  border-radius: 16px;
  background: #ffffff;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.approval-dialog {
  width: 600px;
  max-width: 95vw;
  border-radius: 16px;
  background: #ffffff;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);

  .request-list {
    border-radius: 12px;
    border: 1px solid rgba(0, 0, 0, 0.08);
    
    .request-item {
      padding: 16px;
      transition: all 0.3s ease;

      &:hover {
        background: rgba(var(--q-primary), 0.05);
      }
    }
  }
}

.q-field {
  &--outlined {
    .q-field__control {
      border-radius: 8px;
      height: 44px;
    }
  }

  &--focused {
    .q-field__control {
      box-shadow: 0 0 0 2px rgba(var(--q-primary), 0.2);
    }
  }
}

.q-btn {
  &:not(.q-btn--round) {
    border-radius: 8px;
    padding: 8px 16px;
  }
}

@media (min-width: 768px) {
  .form-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 16px;
  }
}

// Dark mode support
.body--dark {
  .task-details-dialog,
  .dialog-card,
  .approval-dialog {
    background: #1d1d1d;

    .form-card {
      border-color: rgba(255, 255, 255, 0.1);
      background: #2d2d2d;

      &:hover {
        border-color: var(--q-primary);
      }
    }

    .request-list {
      border-color: rgba(255, 255, 255, 0.1);
      background: #2d2d2d;
    }
  }
}
</style> 