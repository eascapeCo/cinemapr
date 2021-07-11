import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import axios from 'axios'
import VueSimpleAlert from 'vue-simple-alert'
// import VuetifyDialog from 'vuetify-dialog'

// axios.defaults.baseURL = process.env.VUE_APP_BACKEND_SVC_URL
axios.defaults.baseURL = 'http://localhost:3000'
axios.defaults.headers['Content-Type'] = 'application/json'

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
