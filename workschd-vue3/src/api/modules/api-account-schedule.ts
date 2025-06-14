import { AxiosResponse } from 'axios';
import service from '@/api/axios-voyagerss';

// Value type configurations
export interface DayValueConfig {
  value: string;
  label: string;
}

export interface MonthValueConfig {
  value: number;
  label: string;
}

// Types for API requests and responses
export interface AccountWorkHourDto {
  id?: number;
  date: string;  // LocalDate in ISO format
  day: string;   // Day of week
  startTime: string; // LocalTime in ISO format
  endTime: string;   // LocalTime in ISO format
  preferred: boolean;
}

export interface AccountWorkHoursResponse {
  workHours: AccountWorkHourDto[];
}

export interface AccountWorkOffDateDto {
  id?: number;
  offDate: string;  // LocalDate in ISO format
}

export interface AccountWorkOffDatesResponse {
  offDates: AccountWorkOffDateDto[];
}

// Constants for schedule configuration
export const daysOfWeek: DayValueConfig[] = [
  { value: 'MONDAY', label: 'Monday' },
  { value: 'TUESDAY', label: 'Tuesday' },
  { value: 'WEDNESDAY', label: 'Wednesday' },
  { value: 'THURSDAY', label: 'Thursday' },
  { value: 'FRIDAY', label: 'Friday' },
  { value: 'SATURDAY', label: 'Saturday' },
  { value: 'SUNDAY', label: 'Sunday' }
];

export const months: MonthValueConfig[] = [
  { value: 1, label: 'January' },
  { value: 2, label: 'February' },
  { value: 3, label: 'March' },
  { value: 4, label: 'April' },
  { value: 5, label: 'May' },
  { value: 6, label: 'June' },
  { value: 7, label: 'July' },
  { value: 8, label: 'August' },
  { value: 9, label: 'September' },
  { value: 10, label: 'October' },
  { value: 11, label: 'November' },
  { value: 12, label: 'December' }
];

const apiAccountSchedule = {
  // Work hour methods
  getWorkHours(accountId: string | number): Promise<AxiosResponse<AccountWorkHoursResponse>> {
    return service.get(`account/${accountId}/schedule/work-hours`);
  },

  saveOrUpdateWorkHour(accountId: string | number, workHour: AccountWorkHourDto): Promise<AxiosResponse<number>> {
    return service.post(`account/${accountId}/schedule/work-hours`, workHour);
  },

  saveOrUpdateWorkHours(accountId: string | number, workHours: AccountWorkHourDto[]): Promise<AxiosResponse<number[]>> {
    return service.post(`account/${accountId}/schedule/work-hours/batch`, workHours);
  },

  deleteWorkHour(accountId: string | number, workHourId: number): Promise<AxiosResponse> {
    return service.delete(`account/${accountId}/schedule/work-hours/${workHourId}`);
  },

  // Work off dates methods
  getWorkOffDates(accountId: string | number): Promise<AxiosResponse<AccountWorkOffDatesResponse>> {
    return service.get(`account/${accountId}/schedule/off-dates`);
  },

  saveOrUpdateWorkOffDate(accountId: string | number, workOffDate: AccountWorkOffDateDto): Promise<AxiosResponse<number>> {
    return service.post(`account/${accountId}/schedule/off-dates`, workOffDate);
  },

  saveOrUpdateWorkOffDates(accountId: string | number, workOffDates: AccountWorkOffDateDto[]): Promise<AxiosResponse<number[]>> {
    return service.post(`account/${accountId}/schedule/off-dates/batch`, workOffDates);
  },

  deleteWorkOffDate(accountId: string | number, workOffDateId: number): Promise<AxiosResponse> {
    return service.delete(`account/${accountId}/schedule/off-dates/${workOffDateId}`);
  }
};

export default apiAccountSchedule;