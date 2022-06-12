<script>
import Vue from 'vue'

export default Vue.extend({
  template: `
      <div @click="invokeParentMethod">{{this.params.data.rolNm}}</div>
  `,
  data: () => ({}),
  beforeMount () {},
  created () {},
  mounted () {
  },
  methods: {
    invokeParentMethod: function () {
      console.info('hh' + this.params.data.regNo)
      this.params.context.componentParent.rolesPopup(this.params.data.regNo)
    }
  }
})
</script>
