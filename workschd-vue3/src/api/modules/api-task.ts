import { AxiosResponse } from 'axios'
import request from '@/api/axios-voyagerss'
import { PageResponseDTO } from '@/api/modules/api-common'

// Interfaces for task-related data
// These were consolidated from the previous task.ts file in the interface directory
export interface Task {
  id?: number
  branch_id: number | null
  store_id: number | null
  additional_info: string
  task_datetime: string
  start_time: string
  end_time: string
  daily_wage: number
}

export interface JoinRequest {
  id: number
  workerName: string
}

export interface AttendanceForm {
  actualStartTime: string
  actualEndTime: string
  calculatedDailyWage: number
}

export interface AttendanceRecord {
  id: number
  employeeName: string
  actualStartTime: string
  actualEndTime: string
  calculatedDailyWage: number
  status: string
}

export interface Branch {
  id: number
  name: string
}

export interface Store {
  id: number
  name: string
}

// Task APIs
const fetchTasks = (): Promise<AxiosResponse<PageResponseDTO<Task>>> => {
  return request.get('/tasks')
}

const createTask = (task: Task): Promise<AxiosResponse<Task>> => {
  return request.post('/task', task)
} 

const approveJoinRequest = (requestId: number): Promise<AxiosResponse<void>> => {
  return request.post(`/task/request/${requestId}/approve`)
}

const fetchBranches = (): Promise<AxiosResponse<Branch[]>> => {
  return request.get('/branches')
}

const fetchStores = (): Promise<AxiosResponse<Store[]>> => {
  return request.get('/stores')
}
export default {
  fetchTasks,
  createTask,
  fetchBranches,
  fetchStores,
  approveJoinRequest
} 