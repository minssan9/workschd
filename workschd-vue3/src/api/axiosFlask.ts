import axios from 'axios';

// Create an Axios instance
const flaskApi = axios.create({
  baseURL: import.meta.env.VITE_FLASK_API,
  timeout: 10000,
  headers: {
    'X-Requested-With': 'XMLHttpRequest',
    'Content-Type': 'application/json', 
  }
});

export default flaskApi;
