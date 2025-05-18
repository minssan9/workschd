<template>
  <q-dialog v-model="isOpen" :maximized="dialogMode === 'view'" persistent :transition-show="transitionShow" :transition-hide="transitionHide">
    <q-card :class="{'task-details-dialog': dialogMode === 'view', 'dialog-card': dialogMode === 'add', 'approval-dialog': dialogMode === 'approval'}">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">
          <template v-if="dialogMode === 'view'">{{ task?.title }}</template>
          <template v-else-if="dialogMode === 'edit'">{{ t('task.edit', 'Edit Task') }}</template>
          <template v-else-if="dialogMode === 'add'">{{ t('task.register', 'Register New Task') }}</template>
          <template v-else-if="dialogMode === 'approval'">{{ t('task.approvalRequests', 'Approval Requests') }}</template>
          <template v-else-if="dialogMode === 'join'">{{ t('task.joinRequest', '작업 참여 신청') }}</template>
          <template v-else-if="dialogMode === 'cancel'">{{ t('task.cancelTask', '작업 취소') }}</template>
        </div>
        <q-space />
        <q-btn icon="close" flat round dense v-close-popup />
      </q-card-section>

      <!-- View Mode -->
      <template v-if="dialogMode === 'view'">
        <q-card-section class="q-pt-sm">
          <div class="q-mb-md">
            <div class="text-subtitle1 q-mb-xs">{{ t('task.details', '상세 정보') }}</div>
            <div class="row q-col-gutter-md">
              <div class="col-12 col-md-6">
                <q-list dense>
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('task.status', '상태') }}</q-item-label>
                      <q-item-label>
                        <q-chip :color="getTaskStatusColor(task?.status)" text-color="white" dense>
                          {{ getTaskStatusLabel(task?.status) }}
                        </q-chip>
                      </q-item-label>
                    </q-item-section>
                  </q-item>
                  
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('task.shop', '매장') }}</q-item-label>
                      <q-item-label>{{ task?.shopName || 'N/A' }}</q-item-label>
                    </q-item-section>
                  </q-item>
                  
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('task.team', '팀') }}</q-item-label>
                      <q-item-label>{{ task?.teamName || 'N/A' }}</q-item-label>
                    </q-item-section>
                  </q-item>
                  
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('task.date', '일자') }}</q-item-label>
                      <q-item-label>{{ formatDateRange(task?.startDateTime, task?.endDateTime) }}</q-item-label>
                    </q-item-section>
                  </q-item>
                  
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('task.time', '시간') }}</q-item-label>
                      <q-item-label>{{ formatTimeRange(task?.startDateTime, task?.endDateTime) }}</q-item-label>
                    </q-item-section>
                  </q-item>
                  
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('task.workerCount', '필요 인원') }}</q-item-label>
                      <q-item-label>{{ task?.workerCount }} {{ t('task.people', '명') }}</q-item-label>
                    </q-item-section>
                  </q-item>
                  
                  <q-item>
                    <q-item-section>
                      <q-item-label caption>{{ t('task.currentWorkers', '현재 인원') }}</q-item-label>
                      <q-item-label>{{ task?.taskEmployees?.length || 0 }} {{ t('task.people', '명') }}</q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </div>
              
              <div class="col-12 col-md-6">
                <q-card flat bordered>
                  <q-card-section>
                    <div class="text-subtitle2">{{ t('task.description', '세부 설명') }}</div>
                    <p class="q-mt-sm">{{ task?.description || t('task.noDescription', '세부 설명이 없습니다.') }}</p>
                  </q-card-section>
                </q-card>
              </div>
            </div>
          </div>
          
          <div class="q-mt-lg">
            <q-btn 
              v-if="canRequestToJoin" 
              color="primary" 
              :label="t('task.joinRequest', '참여 신청')" 
              @click="showJoinRequestDialog"
              class="q-mr-md"
            />
            <q-btn 
              v-if="canManageTask" 
              color="secondary" 
              :label="t('task.edit', '편집')" 
              @click="dialogMode = 'edit'"
              class="q-mr-md"
            />
            <q-btn 
              v-if="canManageTask && hasApprovalRequests" 
              color="info" 
              :label="t('task.approveRequests', '승인 요청')" 
              @click="dialogMode = 'approval'"
              class="q-mr-md"
            />
            <q-btn 
              v-if="canCancelTask" 
              color="negative" 
              :label="t('task.cancel', '작업 취소')" 
              @click="showCancelDialog"
              class="q-mr-md"
            />
          </div>
        </q-card-section>
      </template>

      <!-- Edit Mode -->
      <template v-else-if="dialogMode === 'edit'">
        <q-card-section>
          <q-form @submit.prevent="onEditSubmit" class="q-gutter-md">
            <q-input
              v-model="form.title"
              :label="t('task.title', 'Title')"
              required
              :rules="[val => !!val || t('validation.required', 'Field is required')]"
            />
            
            <q-input
              v-model="form.description"
              type="textarea"
              :label="t('task.description', 'Description')"
              rows="3"
            />
            
            <div class="row q-col-gutter-md">
              <div class="col-12 col-sm-6">
                <q-input
                  v-model="form.startDateTime"
                  type="datetime-local"
                  :label="t('task.startDateTime', 'Start Date & Time')"
                  required
                  :rules="[val => !!val || t('validation.required', 'Field is required')]"
                />
              </div>
              <div class="col-12 col-sm-6">
                <q-input
                  v-model="form.endDateTime"
                  type="datetime-local"
                  :label="t('task.endDateTime', 'End Date & Time')"
                  required
                  :rules="[val => !!val || t('validation.required', 'Field is required')]"
                />
              </div>
            </div>
            
            <div class="row q-col-gutter-md">
              <div class="col-12 col-sm-6">
                <q-select
                  v-model="form.status"
                  :options="statusOptions"
                  :label="t('task.status', 'Status')"
                  required
                  :rules="[val => !!val || t('validation.required', 'Field is required')]"
                />
              </div>
              <div class="col-12 col-sm-6">
                <q-input
                  v-model.number="form.workerCount"
                  type="number"
                  min="1"
                  :label="t('task.workerCount', 'Worker Count')"
                  required
                  :rules="[
                    val => !!val || t('validation.required', 'Field is required'),
                    val => val > 0 || t('validation.min', 'Must be greater than 0')
                  ]"
                />
              </div>
            </div>
            
            <div class="row q-col-gutter-md">
              <div class="col-12">
                <q-select
                  v-model="form.shopId"
                  :options="shopOptions"
                  :label="t('task.shop', 'Shop')"
                  emit-value
                  map-options
                  :rules="[val => val !== null || t('validation.required', 'Field is required')]"
                />
              </div>
            </div>
            
            <q-toggle
              v-model="form.active"
              :label="t('task.active', 'Active')"
            />
          </q-form>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat :label="t('common.cancel', 'Cancel')" color="grey-7" @click="dialogMode = 'view'" />
          <q-btn flat :label="t('common.save', 'Save')" color="primary" @click="onEditSubmit" />
        </q-card-actions>
      </template>

      <!-- Add Mode -->
      <template v-else-if="dialogMode === 'add'">
        <q-card-section class="dialog-content">
          <q-form @submit="onAddSubmit" class="dialog-form q-gutter-md">
            <div class="row q-col-gutter-md">
              <!-- Title and Shop selection --> 
              <div class="col-12 col-md-6">
                <q-input
                  v-model="addForm.title"
                  label="Title"
                  filled
                  required
                />
              </div>
              <div class="col-12 col-md-6">
                <q-select
                  v-model="selectedShop"
                  label="Store"
                  :options="shops"
                  option-value="id"
                  option-label="name"
                  filled
                  required
                />
              </div>

              <!-- Description -->
              <div class="col-12">
                <q-input
                  v-model="addForm.description"
                  label="Description"
                  filled
                  type="textarea"
                  autogrow
                />
              </div>

              <!-- Worker Count -->
              <div class="col-12 col-md-4">
                <q-input
                  v-model.number="addForm.workerCount"
                  label="Worker Count"
                  filled
                  type="number"
                  required
                />
              </div>

              <!-- Date and Time -->
              <div class="col-12 col-md-4">
                <q-input
                  v-model="startDate"
                  label="Start Date"
                  filled
                  type="date"
                  required
                  @update:model-value="updateStartDateTime"
                />
              </div>
              <div class="col-12 col-md-4">
                <q-input
                  v-model="startTime"
                  label="Start Time"
                  filled
                  type="time"
                  required
                  @update:model-value="updateStartDateTime"
                />
              </div>
              
              <div class="col-12 col-md-4">
                <q-input
                  v-model="endDate"
                  label="End Date"
                  filled
                  type="date"
                  required
                  @update:model-value="updateEndDateTime"
                />
              </div>
              <div class="col-12 col-md-4">
                <q-input
                  v-model="endTime"
                  label="End Time"
                  filled
                  type="time"
                  required
                  @update:model-value="updateEndDateTime"
                />
              </div>

              <!-- Status -->
              <div class="col-12 col-md-4">
                <q-select
                  v-model="addForm.status"
                  label="Status"
                  :options="statusOptions"
                  filled
                  required
                />
              </div>

              <!-- Active -->
              <div class="col-12 col-md-4">
                <q-toggle
                  v-model="addForm.active"
                  label="Active"
                />
              </div>
            </div>

            <div class="row justify-end q-mt-md">
              <q-btn label="Cancel" color="negative" flat v-close-popup class="q-mr-sm" />
              <q-btn label="Reset" type="reset" color="warning" flat @click="handleReset" class="q-mr-sm" />
              <q-btn label="Submit" type="submit" color="primary" unelevated />
            </div>
          </q-form>
        </q-card-section>
      </template>

      <!-- Approval Mode -->
      <template v-else-if="dialogMode === 'approval'">
        <q-card-section>
          <div v-if="!requests || requests.length === 0" class="text-center q-pa-md">
            No pending requests
          </div>
          <div v-else>
            <q-list separator>
              <q-item v-for="request in requests" :key="request.id">
                <q-item-section>
                  <q-item-label>{{ request.workerName }}</q-item-label>
                  <q-item-label caption>Request Date: {{ formatDateTime(request.requestDate) }}</q-item-label>
                </q-item-section>
                <q-item-section side>
                  <q-btn
                    color="positive"
                    label="Approve"
                    size="sm"
                    @click="approveRequest(request)"
                  />
                </q-item-section>
              </q-item>
            </q-list>
          </div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Back" @click="dialogMode = 'view'" />
        </q-card-actions>
      </template>

      <!-- Join Request Mode -->
      <template v-else-if="dialogMode === 'join'">
        <q-card-section>
          <p>다음 작업에 참여 신청을 하시겠습니까?</p>
          <p class="text-weight-bold">{{ task?.title }}</p>
          <p>{{ formatDateRange(task?.startDateTime, task?.endDateTime) }} {{ formatTimeRange(task?.startDateTime, task?.endDateTime) }}</p>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="취소" @click="dialogMode = 'view'" />
          <q-btn color="primary" label="신청하기" @click="submitJoinRequest" :loading="isSubmitting" />
        </q-card-actions>
      </template>

      <!-- Cancel Task Mode -->
      <template v-else-if="dialogMode === 'cancel'">
        <q-card-section>
          <p>다음 작업을 취소하시겠습니까?</p>
          <p class="text-weight-bold">{{ task?.title }}</p>
          <p>{{ formatDateRange(task?.startDateTime, task?.endDateTime) }} {{ formatTimeRange(task?.startDateTime, task?.endDateTime) }}</p>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="취소" @click="dialogMode = 'view'" />
          <q-btn color="negative" label="취소하기" @click="submitCancelTask" :loading="isSubmitting" />
        </q-card-actions>
      </template>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useQuasar, date } from 'quasar';
import { useUserStore } from '@/stores/modules/store_user';
import { Task, Shop, TaskStatus, JoinRequest, getTaskStatusLabel, getTaskStatusColor } from '@/types';

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
    $q.notify({
      type: 'negative',
      message: t('task.addError', 'Failed to add task')
    });
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

// Override dialogMode to 'add' when no task is provided
watch(() => props.modelValue, (isOpen) => {
  if (isOpen && !props.task?.id) {
    dialogMode.value = 'add';
  }
}, { immediate: true });
</script>

<style scoped>
.task-details-dialog {
  width: 100%;
  max-width: 1000px;
  height: 100%;
  max-height: 90vh;
  border-radius: 8px;
}

.dialog-card {
  width: 800px;
  max-width: 90vw;
}

.approval-dialog {
  width: 500px;
  max-width: 90vw;
}

@media (min-width: 768px) {
  .task-details-dialog {
    max-height: 80vh;
    height: auto;
  }
}
</style> 