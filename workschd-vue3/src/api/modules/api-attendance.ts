import { AxiosResponse } from 'axios';
import request from "@/api/axios-voyagerss.js";
import { AttendanceRecord } from '@/api/modules/api-task'
import { PageResponseDTO } from '@/api/modules/api-common'

export interface AttendanceDTO {
  id?: number;
  branchId: number;
  taskId: number;
  calculatedDailyWage: number;
  employeeId: number;
  attendanceDate: string;
  dayOfWeek: string;
  startTime: string;
  endTime: string;
  actualStartTime?: string;
  actualEndTime?: string;
}

interface CreateAttendanceParams {
  branchId: number
  taskId: number
  actualStartTime: string
  actualEndTime: string
  calculatedDailyWage: number
  employeeId: string | number
  attendanceDate: string
  dayOfWeek: string
  startTime: string
  endTime: string
}

const create = (params: CreateAttendanceParams): Promise<AxiosResponse<AttendanceRecord>> => {
  return request.post('/attendance', params)
}

const getAttendanceByTaskId = (taskId: number): Promise<AxiosResponse<PageResponseDTO<AttendanceRecord>>> => {
  return request.get(`/attendance/task/${taskId}`)
}

const update = (id: number, params: Partial<CreateAttendanceParams>): Promise<AxiosResponse<AttendanceRecord>> => {
  return request.put(`/attendance/${id}`, params)
}

export default {
  create,
  getAttendanceByTaskId,
  update
}; 