import { defineStore } from 'pinia';
import { TeamDTO as Team, JoinRequest, TeamMember, TeamForm } from '@/api/modules/api-team';
import { ScheduleConfig, DayConfig, MonthConfig } from '@/interface/schedule';
import { Store, Branch } from '@/interface/workplace';

// State type
interface TeamState {
  teams: Team[];
  selectedTeam: Team | null;
  teamMembers: TeamMember[];
  scheduleConfig: ScheduleConfig;
  stores: Store[];
  branches: Branch[];
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
    stores: [],
    branches: []
  }),

  actions: {
    createTeam(team: Partial<Team>) {
      this.teams.push(team as Team);
    },

    setSelectedTeam(team: Team | null) {
      this.selectedTeam = team;
    },

    updateTeamMembers(members: TeamMember[]) {
      this.teamMembers = members;
    },

    updateScheduleConfig(config: ScheduleConfig) {
      this.scheduleConfig = config;
    },

    updateStores(stores: Store[]) {
      this.stores = stores;
    },

    updateBranches(branches: Branch[]) {
      this.branches = branches;
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

    getStores: (state) => {
      return state.stores;
    },

    getBranches: (state) => {
      return state.branches;
    }
  }
});