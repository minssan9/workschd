<template>
  <q-dialog
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
  >
    <q-card style="min-width: 350px; max-width: 90vw;">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">{{ task?.title }}</div>
        <q-space />
        <q-btn icon="close" flat round dense v-close-popup />
      </q-card-section>

      <q-card-section v-if="task">
        <div v-if="task.description" class="q-mb-md">
          <div class="text-subtitle2">설명</div>
          <p class="q-ma-none">{{ task.description }}</p>
        </div>
        
        <div class="q-mb-md">
          <div class="text-subtitle2">작업 기간</div>
          <div class="row items-center q-mt-xs">
            <q-icon name="event" size="xs" class="q-mr-xs" />
            <span>{{ formatDateRange(task.startDateTime, task.endDateTime) }}</span>
          </div>
          <div class="row items-center q-mt-xs">
            <q-icon name="accountWorkHour" size="xs" class="q-mr-xs" />
            <span>{{ formatTimeRange(task.startDateTime, task.endDateTime) }}</span>
          </div>
        </div>
        
        <div class="q-mb-md">
          <div class="text-subtitle2">작업 인원</div>
          <div class="row items-center q-mt-xs">
            <q-icon name="group" size="xs" class="q-mr-xs" />
            <span>{{ task.taskEmployees?.length || 0 }}명 / {{ task.workerCount }}명</span>
          </div>
        </div>
        
        <div class="q-mb-md">
          <div class="text-subtitle2">상태</div>
          <q-chip
            :color="getStatusColor(task.status)"
            text-color="white"
            dense
          >
            {{ getStatusLabel(task.status) }}
          </q-chip>
        </div>
      </q-card-section>
      
      <q-card-actions align="right">
        <q-btn
          v-if="canRequestToJoin(task)"
          color="primary" 
          label="참여 신청" 
          @click="$emit('join-request', task)"
        />
        <q-btn
          v-if="canCancel(task)"
          color="negative"
          label="취소하기"
          @click="$emit('cancel', task)"
        />
        <q-btn flat label="닫기" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { formatDateRange, formatTimeRange } from '@/utils/dateUtils'
import { getStatusColor, getStatusLabel } from '@/utils/taskUtils'
import type { Task } from '@/types/task'

interface Props {
  modelValue: boolean
  task: Task | null
}

defineProps<Props>()
defineEmits<{
  (e: 'update:modelValue', value: boolean): void
  (e: 'join-request', task: Task): void
  (e: 'cancel', task: Task): void
}>()

function canRequestToJoin(task: Task): boolean {
  return task.status === 'scheduled' && 
         (!task.taskEmployees || task.taskEmployees.length < task.workerCount)
}

function canCancel(task: Task): boolean {
  return task.status === 'scheduled'
}
</script> 