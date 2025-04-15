import {ScheduleConfig} from '@/interface/schedule';
import {Shop} from '@/interface/workplace';
import {AxiosResponse} from 'axios';
import request from '@/api/axios-voyagerss';
import { PageDTO, PageResponseDTO, DEFAULT_PAGE_DTO, parseSortParam } from '@/api/modules/api-common';

// Team related interfaces
export interface TeamDTO {
  // Core properties
  id?: number;
  name?: string;
  region?: string;
  scheduleType?: string;
  invitationHash?: string;
  
  // Optional properties
  location?: string;
  memberCount?: number;
  joinRequests?: JoinRequest[];
  createdAt?: string;
  managerName?: string;
  preferredPlaces?: string[];
  
  // Team member properties (when used as a team member)
  email?: string;
  joinDate?: string;
  status?: string;
  userId?: number;
  userName?: string;
  requestDate?: string; 
  page: number;
  size: number;
  sort?: string;
}

// For backward compatibility and type safety in specific contexts
export type JoinRequest = Pick<TeamDTO, 'id' | 'userId' | 'userName' | 'email' | 'requestDate' | 'status'>;
export type TeamMember = Pick<TeamDTO, 'id' | 'name' | 'email' | 'joinDate' | 'status'>;
export type TeamForm = Pick<TeamDTO, 'name' | 'region' | 'scheduleType' | 'preferredPlaces'>;

export interface TeamMemberParams {
  page?: number;
  size?: number;
  sort?: string;
  name?: string;
  email?: string;
  status?: string;
}

export interface Shop {
  id?: number;
  isActive?: boolean; 
  address?: string;
  branchId?: number;
  name: string;
  region?: string;
}


// Re-export common DTOs
export { DEFAULT_PAGE_DTO };
export type { PageDTO, PageResponseDTO };

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
  page: DEFAULT_PAGE_DTO.page, 
  size: DEFAULT_PAGE_DTO.size,
  sort: DEFAULT_PAGE_DTO.sort
}): Promise<AxiosResponse<PageResponseDTO<TeamDTO>>> => {
  return request.get(`/team/${teamName}/members`, { params });
};

// TeamManage APIs
const getTeams = (params: TeamDTO = { page: DEFAULT_PAGE_DTO.page, size: DEFAULT_PAGE_DTO.size }): Promise<AxiosResponse<PageResponseDTO<TeamDTO>>> => {
  // Create query parameters in the format expected by Spring
  const queryParams = {
    page: params.page,
    size: params.size
  };
  
  // Add sort parameter if provided
  if (params.sort) {
    queryParams['sort'] = parseSortParam(params.sort);
  }
  
  // Add any other filter parameters
  if (params.name) queryParams['name'] = params.name;
  if (params.region) queryParams['region'] = params.region;
  if (params.scheduleType) queryParams['scheduleType'] = params.scheduleType;
  
  return request.get('/team', { params: queryParams });
};

// TeamJoin APIs
const joinTeamByInvitation = (invitationHash: string, accountId: string): Promise<AxiosResponse<TeamDTO>> => {
  return request.get(`/team/join/${invitationHash}`, { accountId });
};

// TeamScheduleConfig APIs
const saveScheduleConfig = (config: ScheduleConfig): Promise<AxiosResponse> => {
  return request.post('/schedule-config', config);
};
 
const approveJoinRequest = (teamId: number, requestId: number): Promise<AxiosResponse<any>> => {
  return request.post(`/team/${teamId}/approve/${requestId}`);
};

// Shop APIs
const getShopsByTeamId = (teamId: number): Promise<AxiosResponse<Shop[]>> => {
  return request.get(`/team/${teamId}/shop`);
};

export default {
  // Remove type exports
  // TeamDTO,
  // Shop,
  
  // TeamApproveDialog
  approveRequest,

  // TeamRegistrationDialog
  registerTeam,
  generateInviteLink,
  getTeamMembers,

  // TeamManage
  getTeams,

  // TeamJoin
  joinTeamByInvitation,

  // TeamScheduleConfig
  saveScheduleConfig,

  // Shop APIs
  getShopsByTeamId,

  approveJoinRequest
}; 