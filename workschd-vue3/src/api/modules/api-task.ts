import service from "@/api/axios-voyagerss";

const baseURL = 'task';

export const apiTask = {
  createTask(task: any) {
    return service.post(`${baseURL}`, task);
  },

  getTaskById(id: number) {
    return service.get(`${baseURL}/${id}`);
  },

  updateTask(id: number, task: any) {
    return service.put(`${baseURL}/${id}`, task);
  },

  deleteTask(id: number) {
    return service.delete(`${baseURL}/${id}`);
  },

  queryTasks(params: any) {
    return service.get(`${baseURL}`, { params });
  },

  // Task Employee operations
  assignEmployee(taskId: number, employeeId: number) {
    return service.post(`${baseURL}/employee`, {
      taskId,
      employeeId
    });
  }
}; 