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
  // total: number;
  sort?: string;
}

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
export const DEFAULT_PAGE_REQUEST: PageRequest = {
  page: 0,
  size: 10,
  sort: 'id,desc'
};

// For backward compatibility and type safety in specific contexts
export type JoinRequest = Pick<TeamDTO, 'id' | 'userId' | 'userName' | 'email' | 'requestDate' | 'status'>;
export type TeamMember = Pick<TeamDTO, 'id' | 'name' | 'email' | 'joinDate' | 'status'>;
export type TeamForm = Pick<TeamDTO, 'name' | 'region' | 'scheduleType' | 'preferredPlaces'>; 