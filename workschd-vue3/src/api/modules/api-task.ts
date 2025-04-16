import { AxiosResponse } from 'axios'
import request from '@/api/axios-voyagerss'
import { PageResponseDTO } from '@/api/modules/api-common'

// Interfaces for task-related data
// These were consolidated from the previous task.ts file in the interface directory
export interface Task {
  id?: number
  title: string
  description?: string
  workerCount: number
  startDateTime: string
  endDateTime: string
  status: string
  teamId: number
  shopId: number | null
  active?: boolean
  taskEmployees?: TaskEmployee[] | null
  pageable?: any
}

export interface TaskEmployee {
  id?: number
  taskId: number
  taskTitle?: string
  taskStatus?: string
  accountId: number
  accountName?: string
  accountEmail?: string
  status: string  // "PENDING", "APPROVED", "REJECTED", "ACTIVE", "INACTIVE"
  requestDate?: string
  approvedAt?: string
  rejectedAt?: string
  rejectionReason?: string
  joinedAt?: string
  leftAt?: string,

  content: TaskEmployee[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

export interface Shop {
  id?: number
  name: string
  teamId?: number
  region?: string
  active?: boolean
}


// Store APIs
const createStore = (teamId: number, store: Shop): Promise<AxiosResponse<Shop>> => {
  return request.post(`/team/${teamId}/shop`, store)
}
const fetchShops = (): Promise<AxiosResponse<Shop[]>> => {
  return request.get('/shop')
}
const getActiveShopsByTeamId = (teamId: number): Promise<AxiosResponse<Shop[]>> => {
  return request.get(`/team/${teamId}/shop/active`)
} 


// Task APIs
const fetchTasks = (): Promise<AxiosResponse<PageResponseDTO<Task>>> => {
  return request.get('/task')
}
// Worker-specific API to fetch available tasks with pagination
const fetchTasksForWorker = (params?: any): Promise<AxiosResponse<TaskEmployee | Task[]>> => {
  return request.get('/task', { params })
}
// Worker-specific API to get a user's task requests
const getUserTaskRequests = (accountId: number): Promise<AxiosResponse<TaskEmployee[]>> => {
  return request.get(`/account/${accountId}/task-requests`)
}
const createTask = (task: Task): Promise<AxiosResponse<Task>> => {
  return request.post('/task', task)
} 



// Task-Employee API to create a task employee request
const createTaskEmployeeRequest = (requestData: Partial<TaskEmployee>): Promise<AxiosResponse<TaskEmployee>> => {
  const taskId = requestData.taskId
  return request.post(`/task-employee/${taskId}/request`, requestData)
}
const approveJoinRequest = (requestData: Partial<TaskEmployee>): Promise<AxiosResponse<void>> => {
  const taskId = requestData.taskId
  const requestId = requestData.id
  return request.post(`/task-employee/${taskId}/request/${requestId}/approve`)
}  
// Updated API to get task employees with pagination and filtering
const getTaskEmployees = (taskId: number, params?: any): Promise<AxiosResponse<TaskEmployee[]>> => {
  return request.get(`/task-employee/${taskId}/employees`, { params });
};




export default {
  fetchTasks,
  fetchTasksForWorker, 
  createTaskEmployeeRequest,
  createTask, 
  fetchShops,
  approveJoinRequest,
  createStore,  
  getActiveShopsByTeamId,
  getTaskEmployees
} 