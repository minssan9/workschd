import apiClient from '../apiClient';

export type TeamRole = 'OWNER' | 'MANAGER' | 'MEMBER';
export type InvitationStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED';

export interface TeamMemberDTO {
    id?: number;
    teamId: number;
    accountId: number;
    role: TeamRole;
    invitationStatus: InvitationStatus;
    invitationToken?: string;
    invitationExpiresAt?: string;
    accountName?: string;
    accountEmail?: string;
}

const apiTeamMember = {
    inviteMember: (data: TeamMemberDTO) => 
        apiClient.post('/api/team-members/invite', data),
        
    acceptInvitation: (id: number, token: string) => 
        apiClient.post(`/api/team-members/${id}/accept`, null, {
            params: { token }
        }),
        
    rejectInvitation: (id: number, token: string) => 
        apiClient.post(`/api/team-members/${id}/reject`, null, {
            params: { token }
        }),
        
    getTeamMembers: (teamId: number) => 
        apiClient.get(`/api/team-members/team/${teamId}`),
        
    updateRole: (id: number, role: TeamRole) => 
        apiClient.put(`/api/team-members/${id}/role`, { role }),
        
    removeMember: (id: number) => 
        apiClient.delete(`/api/team-members/${id}`)
};

export default apiTeamMember; 