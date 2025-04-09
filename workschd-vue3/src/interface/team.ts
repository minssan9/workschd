export interface TeamDTO {
  // Core properties
  id?: number;
  name: string;
  region: string;
  scheduleType: string;
  
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
}

// For backward compatibility and type safety in specific contexts
export type JoinRequest = Pick<TeamDTO, 'id' | 'userId' | 'userName' | 'email' | 'requestDate' | 'status'>;
export type TeamMember = Pick<TeamDTO, 'id' | 'name' | 'email' | 'joinDate' | 'status'>;
export type TeamForm = Pick<TeamDTO, 'name' | 'region' | 'scheduleType' | 'preferredPlaces'>; 