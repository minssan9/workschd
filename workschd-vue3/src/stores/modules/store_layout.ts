import { defineStore } from 'pinia';

interface LayoutState {
  title: string;
  drawerLeft: boolean;
  drawerRight: boolean;
  company: {
    name: string;
    email: string;
    phone: string;
    address: string;
    description: string;
  };
}

export const useLayoutStore = defineStore('layout', {
  state: (): LayoutState => ({
    title: 'Scheduler',
    drawerLeft: true,
    drawerRight: false,
    company: {
      name: 'Work Schedule Automation Service',
      email: 'info@workschedule.voyagerss.com',
      phone: '+82 02-123-4567',
      address: '서울특별시 강남구 테헤란로',
      description: '효율적인 근무 관리를 위한 최적의 솔루션을 제공합니다.'
    }
  }),
  
  actions: {
    toggleLeftDrawer() {
      this.drawerLeft = !this.drawerLeft;
    },
    toggleRightDrawer() {
      this.drawerRight = !this.drawerRight;
    },
    setLeftDrawer(value: boolean) {
      this.drawerLeft = value;
    },
    setRightDrawer(value: boolean) {
      this.drawerRight = value;
    },
    resetDrawers() {
      this.drawerLeft = false;
      this.drawerRight = false;
    },
    updateCompanyInfo(companyInfo: Partial<LayoutState['company']>) {
      this.company = { ...this.company, ...companyInfo };
    }
  }
}); 