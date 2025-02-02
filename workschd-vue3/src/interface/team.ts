export class JoinRequest {
  constructor(
    public id: number,
    public name: string,
    public location: string
  ) {}
}

export interface Team {
  id: number;
  name: string;
  location: string;
  memberCount: number;
  joinRequests: JoinRequest[];
  createdAt: string;
  managerName?: string;
  preferredPlaces?: string[];
}

export interface TeamMember {
  id: number;
  name: string;
  email: string;
  joinDate: string;
  status: string;
}

export interface TeamForm {
  name: string;
  location: string;
  preferredPlaces: string[];
} 