import axios from 'axios'
import store from '@/store'
// 인증이 안되었을 경우 리턴 로그인 url 로 라우팅 하기 위해

axios.interceptors.request.use(
  function (config) {
    // Do something before request is sent
    config.headers.Authorization = store.state.access_token
    config.headers['Content-Type'] = 'application/json'
    return config
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error)
  }
)
// Add a response interceptor
axios.interceptors.response.use(
  function (response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    return response
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error)
  }
)
