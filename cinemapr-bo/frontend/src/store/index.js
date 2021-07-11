import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import createPersistedState from 'vuex-persistedstate'
// import {user} from './api'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    access_token: sessionStorage.getItem('access_token'),
    refresh_token: sessionStorage.getItem('refresh_token'),
    expires_in: '',
    // claims: JSON.parse(sessionStorage.getItem('claims')),
    intervalId: null
  },
  plugins: [
    // paths는 값을 유지해야할 값만 넣어줌
    createPersistedState({ paths: ['access_token', 'refresh_token'] })
  ],
  getters: {
    getTokenExpiresIn (state) {
      return state.expires_in
    },
    getIntervalId (state) {
      return state.intervalId
    },
    getClaims (state) {
      return state.claims
    }
  },
  mutations: {
    LOGIN_SUCCESS: function (state, data) {
      state.loginSuccess = true

      state.access_token = data.accessToken
      state.refresh_token = data.refreshToken

      sessionStorage.setItem('access_token', data.accessToken)
      sessionStorage.setItem('refresh_token', data.refreshToken)

      // state.expires_in = data.expires_in

      // console.log('CHK >> ' + JSON.stringify(state))
    },
    LOGIN_ERROR: function (state, data) {
      state.loginError = true
      state.userName = data.userName
    },
    LOGOUT: function (state, data) {
      /* eslint-disable no-console */
      // console.log("delToken....")
      sessionStorage.removeItem('access_token')
      sessionStorage.removeItem('refresh_token')
      sessionStorage.removeItem('claims')
      if (state.access_token) {
        state.access_token = null
      }
      if (state.refresh_token) {
        state.expires_in = null
      }
      if (state.claims) {
        state.claims = null
      }
    }
  },
  actions: {
    LOGIN: ({ commit }, { id, pwd }) => {
      return new Promise((resolve, reject) => {
        return axios({
          method: 'post',
          url: '/api/login',
          data: {
            username: id,
            password: pwd
          }
        })
          .then((res) => {
            if (res.status === 200) {
              commit('LOGIN_SUCCESS', {
                accessToken: 'Bearer ' + res.data.access_token,
                refreshToken: 'Bearer ' + res.data.refresh_token
              })
            }
            resolve(res)
          })
          .catch((error) => {
            console.log('Error: ' + error)
            commit('LOGIN_ERROR', {
              userName: id
            })
            reject(new Error('Invalid credentials!'))
          })
      })
    },
    destroySetJwtExpiresInScheduler (context) {
      clearInterval(context.getters.getIntervalId)
    }
  },
  modules: {
  }
})
