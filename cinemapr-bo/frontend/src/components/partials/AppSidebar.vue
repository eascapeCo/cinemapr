<template>
<!--
  <v-navigation-drawer
      v-model="drawer"
      app
      clipped
  >
-->
  <v-list>
    <v-list-group
      prepend-icon="mdi-view-dashboard"
      no-action
      v-for="menu in menus"
      :key="menu.mnuName"
    >
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title v-text="menu.mnuName"></v-list-item-title>
        </v-list-item-content>
      </template>
      <template
        v-for="secondMenu in menu.subMenus"
      >
        <template v-if="secondMenu.subMenus.length !== 0">
          <v-list-group
            no-action
            sub-group
            v-bind:key="secondMenu.mnuNo"
          >
            <template v-slot:activator>
              <v-list-item-content>
                <template v-if="secondMenu.subMenus.length !== 0">
                  <v-list-item-title v-text="secondMenu.mnuName"></v-list-item-title>
                </template>
                <template v-else>
                  <v-list-item-title v-text="secondMenu.mnuName"  v-on:click="test(secondMenu.urlAdr)"></v-list-item-title>
                </template>
              </v-list-item-content>
            </template>
            <v-list-item
              link
              v-for="thirdMenu in secondMenu.subMenus"
              :key="thirdMenu.mnuNo"
            >
              <v-list-item-title v-text="thirdMenu.mnuName" v-on:click="test(thirdMenu.urlAdr)"></v-list-item-title>
            </v-list-item>
          </v-list-group>
        </template>
        <template v-else>
          <v-list-item link v-bind:key="secondMenu.mnuNo">
            <v-list-item-content>
              <v-list-item-title v-text="secondMenu.mnuName" v-on:click="test(secondMenu.urlAdr)"></v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </template>
      </template>
    </v-list-group>
    <v-list-group
      prepend-icon="mdi-view-dashboard"
      no-action
    >
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>sample</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item link>
        <v-list-item-content>
          <v-list-item-title>
            <router-link to="/">
              main
            </router-link>
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-list-item link>
        <v-list-item-content>
          <v-list-item-title>
            <router-link to="/grid">
              grid
            </router-link>
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list-group>
  </v-list>
<!-- </v-navigation-drawer> -->
</template>

<script>
export default {
  name: 'AppSidebar',
  created () {
    this.$axios.get('/api/menus').then((res) => {
      this.menus = res.data
      // this.confirm()
      // if (this.$alert('만료시간이 지났습니다. 로그인페이지로 이동합니다')) {
    }).catch(err => {
      console.error(err)
      this.invalidTkn()
    })
  },
  data: () => ({
    menus: [],
    persistent: false,
    title: '',
    contents: ''
  }),
  methods: {
    getMenuList: function () {
      console.log('11')
    },
    test: function (url) {
      console.log(url)
      console.log(this.$route)
      this.$router.push(url).catch(error => {
        if (error.name !== 'NavigationDuplicated') {
          throw error
        }
      })
    },
    invalidTkn: function () {
      this.$fire({
        title: 'Error',
        text: 'Token is not valid. Go to the login form.',
        type: 'warning'

      }).then(r => {
        this.$router.push('/loginForm')
      })
    }
  }
}
</script>
