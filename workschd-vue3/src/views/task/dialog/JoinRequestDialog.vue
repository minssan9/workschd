<template>
  <q-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
  >
    <q-card>
      <q-card-section class="row items-center">
        <div class="text-h6">작업 참여 신청</div>
      </q-card-section>

      <q-card-section>
        <p>다음 작업에 참여 신청을 하시겠습니까?</p>
        <p class="text-weight-bold">{{ task?.title }}</p>
        <p>{{ formatDateRange(task?.startDateTime, task?.endDateTime) }} {{ formatTimeRange(task?.startDateTime, task?.endDateTime) }}</p>
      </q-card-section>

      <q-card-actions align="right">
        <q-btn flat label="취소" v-close-popup />
        <q-btn color="primary" label="신청하기" @click="$emit('submit')" :loading="isSubmitting" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { formatDateRange, formatTimeRange } from '@/utils/dateUtils'
import type { Task } from '@/types/task'

interface Props {
  modelValue: boolean
  task: Task | null
  isSubmitting: boolean
}

defineProps<Props>()
defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'submit'): void
}>()
</script> 