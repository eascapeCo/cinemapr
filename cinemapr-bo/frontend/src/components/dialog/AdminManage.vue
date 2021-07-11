<script>
import Vue from 'vue'

export default Vue.extend({
  template: `
        <div @click="link">{{this.params.data.username}}</div>
    `,
  data: () => ({}),
  beforeMount () {},
  created () {},
  mounted () {
    console.log(this.a)
  },
  methods: {
    invokeParentMethod: function () {
      console.log('여기 맞냐 : ' + this.params.data)
      /*
      this.params.context.componentParent.methodFromParent(
        `Row: ${this.params.node.rowIndex}, Col: ${this.params.colDef.headerName}`
      )
      */
      /*
        <router-link to='adminForm/{{this.a.rolNo}}'>{{this.a.rolNo}}</router-link>
        <button type="button" class="v-btn v-btn--contained theme--dark v-size--default primary" v-on:click="invokeParentMethod">
          <span class="v-btn__content">Share - {{this.a.rolNo}}</span>
        </button>
      */
    },
    link: function () {
      this.$router.push(`/adminManagement/${this.params.data}`)
    }
  }
})
</script>
