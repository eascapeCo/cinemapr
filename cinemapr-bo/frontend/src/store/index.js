import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    access_token: '',
    expires_in: '',
    intervalId: null
  },
  plugins: [
    // paths는 값을 유지해야할 값만 넣어줌
    createPersistedState({ paths: ['access_token', 'expires_in'] })
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
      state.expires_in = data.expiryDuration
    },
    LOGIN_ERROR: function (state, data) {
      state.loginError = true
      state.userName = data.userName
    },
    LOGOUT: function (state, data) {
      /* eslint-disable no-console */
      // console.log("delToken....")
      if (state.access_token) {
        state.access_token = null
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
            admId: id,
            pwd: pwd
          }
        })
          .then((res) => {
            console.log(res)
            if (res.status === 200) {
              commit('LOGIN_SUCCESS', {
                accessToken: 'Bearer ' + res.data.accessToken,
                expiryDuration: res.data.expiryDuration
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
  modules: {}
})
