import {TeamDTO} from '@/interface/team';
import {ScheduleConfig} from '@/interface/schedule';
import {Store} from '@/interface/workplace';
import {AxiosResponse} from 'axios';
import request from '@/api/axios-voyagerss';

// Pagination interface
export interface PageRequest {
  page: number;
  size: number;
  sort?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

// Default pagination parameters
const DEFAULT_PAGE_REQUEST: PageRequest = {
  page: 0,
  size: 10,
  sort: 'id,desc'
};

// Request parameter interfaces
export interface TeamListParams {
  pageable: PageRequest;
  name?: string;
  region?: string;
  scheduleType?: string;
  fromDate?: string;
  toDate?: string;
}

export interface TeamMemberParams {
  pageable: PageRequest;
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

const getTeamMembers = (teamName: string, params: TeamMemberParams = { pageable: DEFAULT_PAGE_REQUEST }): Promise<AxiosResponse<PageResponse<TeamDTO>>> => {
  return request.get(`/team/${teamName}/members`, { params });
};

// TeamManage APIs
const getTeams = (params: TeamListParams = { pageable: DEFAULT_PAGE_REQUEST }): Promise<AxiosResponse<PageResponse<TeamDTO>>> => {
  return request.get('/team',  params );
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