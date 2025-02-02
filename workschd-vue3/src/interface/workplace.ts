export interface Store {
  name: string;
  address: string;
  region: string;
  branch_id: number | null;
}

export interface Branch {
  id: number;
  name: string;
} 