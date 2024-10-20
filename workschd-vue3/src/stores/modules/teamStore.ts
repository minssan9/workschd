import { defineStore } from 'pinia';

export const useTeamStore = defineStore('team', {
  state: () => ({
    teams: [],
  }),
  actions: {
    createTeam(team) {
      this.teams.push(team);
    },
    requestJoinTeam(request) {
      console.log('Join Request:', request);
      // API 통신 로직 또는 팀 요청 처리
    },
  },
});