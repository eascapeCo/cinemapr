import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import VueSimpleAlert from 'vue-simple-alert'
import axios from './api'

const local = axios.create({
  baseURL: ''
})

Vue.prototype.$local = local

Vue.prototype.$axios = axios
Vue.prototype.$store = store

Vue.use(VueSimpleAlert)

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
