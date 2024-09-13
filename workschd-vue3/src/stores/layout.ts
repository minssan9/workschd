import { defineStore } from 'pinia';
import api from "@/api/axios";

export const useLayoutStore = defineStore('layout', {
    state: () => ({
      factoryName: '',
      isFullScreen: false,
      isSideBarExtended: true,
      isRightSideBarExtended: false,
      baseSideWidth: 340,
    }),
    getters: {
        getFactoryName(): string {
          return this.factoryName;
        },
        getIsFullScreen(): boolean {
          return this.isFullScreen;
        },
        getIsSideBarExtended(): boolean {
            return this.isSideBarExtended;
        },
        getIsRightSideBarExtended(): boolean {
            return this.isRightSideBarExtended;
        },
        getBaseSideWidth(): string {
          return this.baseSideWidth + 'px';
        }
    },
    actions: {
      async fetchFactoryName() {
        const res = await api.get('/info/factory')
        this.factoryName = res.data.name
      },
        toggleFullScreen() {
          this.isFullScreen = !this.isFullScreen;
          if (this.isFullScreen)
            this.isSideBarExtended = false;
          else
            this.isSideBarExtended = true;
        },
        toggleSideBarExtended() {
          this.isSideBarExtended = !this.isSideBarExtended;
        },
        toggleRightSideBarExtended() {
            this.isRightSideBarExtended = !this.isRightSideBarExtended;
        },
        setBaseSideWidth(width) {
          this.baseSideWidth = width;
        }
    }
});
