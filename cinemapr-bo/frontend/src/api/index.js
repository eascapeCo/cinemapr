import axios from 'axios'
import store from '@/store'

axios.defaults.headers['Content-Type'] = 'application/json'

axios.interceptors.request.use(
  function (config) {
    config.headers.Authorization = store.state.access_token
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
    if ('access_token' in response.headers && store.state.access_token !== response.headers.access_token) {
      console.log('response > ' + response)
      store.state.access_token = 'Bearer ' + response.headers.access_token
      store.state.expires_in = response.headers.expires_in
    }
    return response
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error)
  }
)

export default axios
