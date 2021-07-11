<template>
  <v-app>
  <v-navigation-drawer
      v-model="drawer"
      app
      clipped
  >
    <AppSidebar/>
    <template v-slot:append>
      <div class="pa-2">
        <v-btn block @click="logoutDialog = true">Logout</v-btn>
        <v-dialog v-model="logoutDialog" max-width="500px">
        <v-card>
          <v-card-title>Logout</v-card-title>
          <v-card-text>로그아웃 하시겠습니까?</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" text @click="logoutAdmin">Logout</v-btn>
            <v-btn color="primary" text @click="logoutDialog = false">Close</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      </div>
    </template>

  </v-navigation-drawer>
    <v-app-bar
      app
      clipped-left
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title>Application</v-toolbar-title>
    </v-app-bar>
    <v-main>
      <router-view/>
    </v-main>
    <AppFooter/>
  </v-app>
</template>

<script>
// @ is an alias to /src
import AppSidebar from '@/components/partials/AppSidebar.vue'
import AppFooter from '@/components/partials/AppFooter.vue'

export default {
  name: 'Main',
  components: {
    AppSidebar,
    AppFooter
  },
  props: {
    source: String
  },
  data: () => ({
    drawer: null,
    logoutDialog: false
  }),
  methods: {
    logoutAdmin () {
      this.$store.commit('LOGOUT')
      this.$store.dispatch('destroySetJwtExpiresInScheduler')
        .then(() => {
          // close this dialog
          this.logoutDialog = false
          this.$router.push('/loginForm')
        }).catch(error => {
          this.errors.push(error)
          this.error = true
        })
    }
  }
}
</script>
