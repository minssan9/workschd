import service from "@/api/axios-voyagerss";

const baseURL = 'api/schedules';

export const apiSchedule = {
  createSchedule(schedule: any) {
    return service.post(`${baseURL}`, schedule);
  },

  updateSchedule(id: number, schedule: any) {
    return service.put(`${baseURL}/${id}`, schedule);
  },

  deleteSchedule(id: number) {
    return service.delete(`${baseURL}/${id}`);
  },

  getScheduleConfig() {
    return service.get(`${baseURL}/config`);
  },

  updateScheduleConfig(config: any) {
    return service.put(`${baseURL}/config`, config);
  }
}; 