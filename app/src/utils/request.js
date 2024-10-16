import axios from 'axios';

const request = axios.create({
  baseURL: 'http://localhost:8080', // 替换成你的后端 API 地址
  timeout: 5000,
  withCredentials: true,
});

// 请求拦截器，可以在这里处理请求头或者 token
request.interceptors.request.use(config => {
  // config.headers['Authorization'] = 'Bearer ' + token;
  return config;
});

// 响应拦截器，可以处理全局错误
request.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export default request;
