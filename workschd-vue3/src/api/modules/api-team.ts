import { Team, JoinRequest, TeamMember } from '@/interface/team';
import { ScheduleConfig } from '@/interface/schedule';
import { Store } from '@/interface/workplace';
import { AxiosResponse } from 'axios';
import request from '@/api/axios-voyagerss';

// TeamApproveDialog APIs
const approveRequest = (teamId: number, joinRequest: JoinRequest): Promise<AxiosResponse> => {
  return request.post(`/teams/${teamId}/approve`, joinRequest);
};

// TeamRegistrationDialog APIs
const registerTeam = (teamData: Partial<Team>): Promise<AxiosResponse> => {
  return request.post('/teams', teamData);
};

const generateInviteLink = (teamData: { teamName: string; location: string }): Promise<AxiosResponse> => {
  return request.post('/teams/generate-invite', teamData);
};

const getTeamMembers = (teamName: string): Promise<AxiosResponse> => {
  return request.get(`/teams/${teamName}/members`);
};

// TeamManage APIs
const getTeams = (): Promise<AxiosResponse<Team[]>> => {
  return request.get('/teams');
};

// TeamJoin APIs
const getTeamInfo = (token: string): Promise<AxiosResponse<Team>> => {
  return request.get(`/teams/invite/${token}`);
};

const joinTeam = (teamId: number, data: { accountId: string; inviteToken: string }): Promise<AxiosResponse> => {
  return request.post(`/teams/${teamId}/join`, data);
};

// TeamScheduleConfig APIs
const saveScheduleConfig = (config: ScheduleConfig): Promise<AxiosResponse> => {
  return request.post('/schedule-config', config);
};

// TeamWorkPlace APIs
const getStores = (): Promise<AxiosResponse> => {
  return request.get('/stores');
};

const createStore = (store: Store): Promise<AxiosResponse> => {
  return request.post('/stores', store);
};

export default {
  // TeamApproveDialog
  approveRequest,

  // TeamRegistrationDialog
  registerTeam,
  generateInviteLink,
  getTeamMembers,

  // TeamManage
  getTeams,

  // TeamJoin
  getTeamInfo,
  joinTeam,

  // TeamScheduleConfig
  saveScheduleConfig,

  // TeamWorkPlace
  getStores,
  createStore
}; 