import { defineStore } from 'pinia';
import api from "@/api/axios";

export const useMapTreeStore = defineStore('mapTree', {
  state: () => ({
    treeRoot: {},
    treeNodes: [],
    filteredNodes: [],
    selectedDiagType: [],
    selectedStatus: [],
    groupNodes: [],
  }),
  getters: {

  },
  actions: {
    async fetchMapTree() {
      await api.get("/map/tree/root")
        .then(res => {
          this.treeRoot = res.data.nodeId
        })

      await api.get(`/map/tree/${this.treeRoot}`)
        .then(res => {
          this.treeNodes = res.data
          // this.sortTree(this.treeNodes[0])
          this.filterNode()
        })
    },

    sortTree(node) {
      if (!node.children || node.children.length === 0) return;

      // Sort the current node's children
      node.children.sort((a, b) => a.menuId - b.menuId); // Replace 'value' with your property

      // Recursively sort each child's children
      node.children.forEach(this.sortTree);
    },

    filterNode() {
      const filterRecursive = (nodes) => {
        return nodes.map(node => {
            const children = filterRecursive(node.children || []);
            if (this.selectedDiagType.length === 0 && this.selectedStatus.length === 0) {
              return { ...node, children };
            } else if ( this.selectedDiagType.length === 0 && this.selectedStatus.includes(node.diagStatus) ) {
              return {...node, children};
            } else if ( this.selectedStatus.length === 0 && this.selectedDiagType.includes(node.diagTypeCode) ) {
              return {...node, children};
            } else if ( ( this.selectedDiagType.includes(node.diagTypeCode) && this.selectedStatus.includes(node.diagStatus) ) || children.length) {
              return { ...node, children };
            }
            return null;
          })
          .filter(node => node !== null);
      };

      this.filteredNodes = filterRecursive(this.treeNodes);
    },

    updateTreeNode(node) {
      const targetNode = this.treeNodes.find(item => item.equipmentDiagId === node.id);
      if (targetNode) {
        targetNode.status = node.status;
      }
    },

    getGroupNode(targetId) {
      const filterRecursive = (nodes, targetId) => {
          for (const node of nodes) {
              if (node.nodeId === targetId) {
                  return [{ ...node, children: node.children }];
              }

              const children = filterRecursive(node.children || [], targetId);
              if (children.length > 0) {
                  return children;
              }
          }
          return []; // 해당 targetId를 찾지 못한 경우 빈 배열 반환
      };

      this.groupNodes = filterRecursive(this.treeNodes, targetId);
    }
  }
});
