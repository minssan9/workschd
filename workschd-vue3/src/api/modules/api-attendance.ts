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
    return request.post('/attendance', data);
  },

  update(id: number, data: AttendanceDTO): Promise<AxiosResponse> {
    return request.put(`/attendance/${id}`, data);
  },

  delete(id: number): Promise<AxiosResponse> {
    return request.delete(`/attendance/${id}`);
  },

  getById(id: number): Promise<AxiosResponse> {
    return request.get(`/attendance/${id}`);
  },

  getByEmployeeId(employeeId: number): Promise<AxiosResponse> {
    return request.get(`/attendance/employee/${employeeId}`);
  },

  getByDateRange(startDate: string, endDate: string): Promise<AxiosResponse> {
    return request.get(`/attendance/range?start=${startDate}&end=${endDate}`);
  }
}; 