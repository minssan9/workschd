import service from '@/api/axios-voyagerss'
import { encodeURIComponent } from 'js-base64'

const ASSEMBLY_API_URL = 'https://open.assembly.go.kr/portal/openapi/ALLNAMEMBER'
const API_KEY = 'dKNTmNiGHpcJLrC4HIJc3nW4AaPPKOQLCvlzV7IQZDTTztv6PTuDusZxS8iC1vpBEtkLsnk97WEzKpEvf3Zqgg=='

interface AssemblyParams {
  pIndex?: string | number;
  pSize?: string | number;
  Type?: string;
}

export default {
  /**
   * Get assembly members list with pagination
   * @param params Pagination and filter parameters
   * @returns Promise with assembly members data
   */
  getMembers: (params: AssemblyParams = {}) => {
    const defaultParams = {
      KEY: API_KEY,
      Type: 'json',
      pIndex: '1',
      pSize: '20'
    }

    return service.get(ASSEMBLY_API_URL, {
      params: { ...defaultParams, ...params },
      headers: {
        'Accept': 'application/json'
      }
    })
  }
} 