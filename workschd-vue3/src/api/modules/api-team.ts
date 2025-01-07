import service from "@/api/axios-voyagerss";

const baseURL = 'team';

export const apiTeam = {
  // Team operations
  createTeam(team: any) {
    return service.post(`${baseURL}`, team);
  },

  getTeamById(id: number) {
    return service.get(`${baseURL}/${id}`);
  },

  updateTeam(id: number, team: any) {
    return service.put(`${baseURL}/${id}`, team);
  },

  deleteTeam(id: number) {
    return service.delete(`${baseURL}/${id}`);
  },

  queryTeams(params: any) {
    return service.get(`${baseURL}`, { params });
  },

  // Team join operations
  joinTeam(teamId: number, username: string) {
    return service.post(`${baseURL}/join`, { teamId, username });
  },

  handleInvite(hash: string) {
    return service.get(`${baseURL}/join/${hash}`);
  }
}; 