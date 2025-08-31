/* Api */
import apiAccount from '@/api/modules/api-account'
import apiTask from '@/api/modules/api-task'
import apiTeam from '@/api/modules/api-team'
import apiSys from "@/api/public-modules/api-sys";

const api = {
  accountAPI: apiAccount,
  taskAPI: apiTask,
  teamAPI: apiTeam,
  sysAPI: apiSys
}

export default api

