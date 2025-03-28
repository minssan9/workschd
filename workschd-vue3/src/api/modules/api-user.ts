import request from '@/api/axios-voyagerss'

export default {
  getCurrentUser: () => request.get('/user/current'),
  updateProfile: (data: any) => request.put('/user/profile', data),
  
  // File upload helper
  uploadFile: (formData: FormData) => 
    request.post('/user/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
} 