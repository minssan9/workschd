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

export interface Shop {
  id?: number
  name: string
  teamId?: number
  region?: string
  active?: boolean
}

export interface ShopDTO extends Shop {
  id: number
}

// Task APIs
const fetchTasks = (): Promise<AxiosResponse<PageResponseDTO<Task>>> => {
  return request.get('/task')
}

const createTask = (task: Task): Promise<AxiosResponse<Task>> => {
  return request.post('/task', task)
} 

const approveJoinRequest = (requestId: number): Promise<AxiosResponse<void>> => {
  return request.post(`/task/request/${requestId}/approve`)
}


// Store APIs
const createStore = (teamId: number, store: Shop): Promise<AxiosResponse<StoreDTO>> => {
  return request.post(`/team/${teamId}/shop`, store)
}

const updateStore = (teamId: number, storeId: number, store: Shop): Promise<AxiosResponse<void>> => {
  return request.put(`/team/${teamId}/shop/${storeId}`, store)
}

const deleteStore = (teamId: number, storeId: number): Promise<AxiosResponse<void>> => {
  return request.delete(`/team/${teamId}/shop/${storeId}`)
}

const getStoreById = (teamId: number, storeId: number): Promise<AxiosResponse<StoreDTO>> => {
  return request.get(`/team/${teamId}/shop/${storeId}`)
}

const getStoresByTeamId = (teamId: number, region?: string): Promise<AxiosResponse<StoreDTO[]>> => {
  const params = region ? { region } : {}
  return request.get(`/team/${teamId}/shop`, { params })
}

const getActiveShopsByTeamId = (teamId: number): Promise<AxiosResponse<StoreDTO[]>> => {
  return request.get(`/team/${teamId}/shop/active`)
}

const fetchShops = (): Promise<AxiosResponse<Shop[]>> => {
  return request.get('/shop')
}

export default {
  fetchTasks,
  createTask, 
  fetchShops,
  approveJoinRequest,
  createStore,
  updateStore,
  deleteStore,
  getStoreById,
  getStoresByTeamId,
  getActiveShopsByTeamId
} 