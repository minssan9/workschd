import axios from '@/utils/axios'

export default {
  getCurrentUser: () => axios.get('/user/current'),
  updateProfile: (data: any) => axios.put('/user/profile', data),
  
  // File upload helper
  uploadFile: (formData: FormData) => 
    axios.post('/user/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
} 