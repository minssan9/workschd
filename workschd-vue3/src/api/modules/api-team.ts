import {TeamDTO, DEFAULT_PAGE_REQUEST, PageRequest, PageResponse} from '@/interface/team';
import {ScheduleConfig} from '@/interface/schedule';
import {Store} from '@/interface/workplace';
import {AxiosResponse} from 'axios';
import request from '@/api/axios-voyagerss';

export interface TeamMemberParams {
  page?: number;
  size?: number;
  sort?: string;
  name?: string;
  email?: string;
  status?: string;
}

// TeamApproveDialog APIs
const approveRequest = (teamId: number, joinRequest: TeamDTO): Promise<AxiosResponse> => {
  return request.post(`/team/${teamId}/approve`, joinRequest);
};

// TeamRegistrationDialog APIs
const registerTeam = (teamData: TeamDTO): Promise<AxiosResponse<TeamDTO>> => {
  return request.post('/team', teamData);
};

const generateInviteLink = (teamData: { teamName: string; region: string }): Promise<AxiosResponse> => {
  return request.post('/team/generate-invite', teamData);
};

const getTeamMembers = (teamName: string, params: TeamMemberParams = {
  page: DEFAULT_PAGE_REQUEST.page, 
  size: DEFAULT_PAGE_REQUEST.size,
  sort: DEFAULT_PAGE_REQUEST.sort
}): Promise<AxiosResponse<PageResponse<TeamDTO>>> => {
  return request.get(`/team/${teamName}/members`, { params });
};

// TeamManage APIs
const getTeams = (params: TeamDTO = { page: DEFAULT_PAGE_REQUEST.page, size: DEFAULT_PAGE_REQUEST.size }): Promise<AxiosResponse<PageResponse<TeamDTO>>> => {
  // Create query parameters in the format expected by Spring
  const queryParams = {
    page: params.page,
    size: params.size
  };
  
  // Add sort parameter if provided
  if (params.sort) {
    queryParams['sort'] = params.sort;
  }
  
  // Add any other filter parameters
  if (params.name) queryParams['name'] = params.name;
  if (params.region) queryParams['region'] = params.region;
  if (params.scheduleType) queryParams['scheduleType'] = params.scheduleType;
  
  return request.get('/team', { params: queryParams });
};

// TeamJoin APIs
const getTeamInfo = (token: string): Promise<AxiosResponse<TeamDTO>> => {
  return request.get(`/team/invite/${token}`);
};

const joinTeam = (teamId: number, data: { accountId: string; inviteToken: string }): Promise<AxiosResponse> => {
  return request.post(`/team/${teamId}/join`, data);
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

const approveJoinRequest = (teamId: number, requestId: number): Promise<AxiosResponse<any>> => {
  return request.post(`/team/${teamId}/approve/${requestId}`);
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
  createStore,

  approveJoinRequest
}; 