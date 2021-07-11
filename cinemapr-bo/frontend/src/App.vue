<template>
  <v-app>
      <router-view/>
  </v-app>
</template>

<script>

export default {
  name: 'App',
  props: {
    source: String
  },
  components: {
  },
  data: () => ({
  }),
  created () {
    this.$vuetify.theme.dark = true
  },
  methods: {
    test: function () {
      alert('a')
      this.$axios.get('/api/users')
        .then((res) => {
          console.log(res)
        })
    }
  }
}
</script>
<style lang="scss">
  @import "../node_modules/ag-grid-community/dist/styles/ag-grid.css";
  @import "../node_modules/ag-grid-community/dist/styles/ag-theme-alpine.css";
</style>
