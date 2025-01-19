import { AxiosResponse } from 'axios';
import request from '../axios-voyagerss';

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

export default {
  create(data: AttendanceDTO): Promise<AxiosResponse> {
    return request.post('/api/attendance', data);
  },

  update(id: number, data: AttendanceDTO): Promise<AxiosResponse> {
    return request.put(`/api/attendance/${id}`, data);
  },

  delete(id: number): Promise<AxiosResponse> {
    return request.delete(`/api/attendance/${id}`);
  },

  getById(id: number): Promise<AxiosResponse> {
    return request.get(`/api/attendance/${id}`);
  },

  getByEmployeeId(employeeId: number): Promise<AxiosResponse> {
    return request.get(`/api/attendance/employee/${employeeId}`);
  },

  getByDateRange(startDate: string, endDate: string): Promise<AxiosResponse> {
    return request.get(`/api/attendance/range?start=${startDate}&end=${endDate}`);
  }
}; 