import { defineStore } from 'pinia';
import { TeamDTO as Team, JoinRequest, TeamMember, TeamForm, Shop } from '@/api/modules/api-team';
import apiTeam from '@/api/modules/api-team';
import { ScheduleConfig, DayConfig, MonthConfig } from '@/interface/schedule';

// State type
interface TeamState {
  teams: Team[];
  selectedTeam: Team | null;
  teamMembers: TeamMember[];
  scheduleConfig: ScheduleConfig;
  shops: Shop[];
}  

export const useTeamStore = defineStore('team', {
  state: (): TeamState => ({
    teams: [],
    selectedTeam: null,
    teamMembers: [],
    scheduleConfig: {
      minStaffPerDay: {
        MONDAY: 1,
        TUESDAY: 1,
        WEDNESDAY: 1,
        THURSDAY: 1,
        FRIDAY: 1,
        SATURDAY: 1,
        SUNDAY: 1
      },
      maxOffDaysPerMonth: {
        1: 4, 2: 4, 3: 4, 4: 4, 5: 4, 6: 4,
        7: 4, 8: 4, 9: 4, 10: 4, 11: 4, 12: 4
      }
    },
    shops: []
  }),

  actions: {
    createTeam(team: Partial<Team>) {
      this.teams.push(team as Team);
    },

    setSelectedTeam(team: Team | null) {
      this.selectedTeam = team;
    },

    async setTeam(teamId: number | null): Promise<void> {
      if (!teamId) {
        this.selectedTeam = null;
        this.shops = [];
        return;
      }
      
      const team = this.teams.find(t => t.id === teamId);
      if (team) {
        this.selectedTeam = team;
      } else {
        // If team not found in current list, you might want to fetch it
        console.warn(`Team with id ${teamId} not found in current list`);
      }
      
      // Fetch shops for the selected team
      await this.fetchShopsByTeamId(teamId);
    },

    updateTeamMembers(members: TeamMember[]) {
      this.teamMembers = members;
    },

    updateScheduleConfig(config: ScheduleConfig) {
      this.scheduleConfig = config;
    },

    updateShops(shops: Shop[]) {
      this.shops = shops;
    },

    async fetchShopsByTeamId(teamId: number): Promise<void> {
      try {
        const response = await apiTeam.getShopsByTeamId(teamId);
        this.shops = response.data;
      } catch (error) {
        console.error('Error fetching shops:', error);
        throw error;
      }
    },

    requestJoinTeam(request: JoinRequest) {
      if (!this.selectedTeam) return;
      this.selectedTeam.joinRequests.push(request);
    }
  },

  getters: {
    getTeamById: (state) => (id: number) => {
      return state.teams.find(team => team.id === id);
    },
    
    getTeamMembers: (state) => {
      return state.teamMembers;
    },

    getScheduleConfig: (state) => {
      return state.scheduleConfig;
    },

    getShops: (state) => {
      return state.shops;
    },

    getShopOptions: (state) => {
      return state.shops.map(shop => ({
        label: shop.name,
        value: shop.id
      }));
    }
  }
});