<template>
  <div class="menuView">
    <v-container fluid>
      <v-alert
        dense
        border="left"
        type="warning"
      >
        I'm a dense alert with the <strong>border</strong> prop and a <strong>type</strong> of warning
      </v-alert>
      <v-row>
        <v-col>
          <v-card
            class="pa-2"
            tile
            outlined
          >
            <v-treeview :items="menus"
                        item-text="mnuName"
                        item-children="subMenus"
                        item-key="mnuNo"
                        activatable
                        :open-all=false
                        @update:active="selectMenu"
            >
              <!--
              <template slot="label" slot-scope="{ item }">
                <span @click="selectMenu(item)">{{ item.mnuName }}</span>
              </template>
              -->
            </v-treeview>
          </v-card>
        </v-col>
        <v-col>
          <v-card
            class="pa-2"
            tile
            outlined
          >
            <form>
              <v-text-field v-model="data.mnuName" label="메뉴명" />
              <v-text-field v-model="data.urlAdr" label="메뉴 URL" />
              <v-select v-model="data.useYn" :items="[true, false]" label="사용 여부"></v-select>
              <v-select v-model="data.dpYn" :items="[true, false]" label="전시 여부"></v-select>
              <v-text-field v-model="data.dpSequence" label="전시 순서" />
              <v-radio-group v-model="data.createType" row>
                <v-radio label="동일 레벨 생성" value="siblingLevel"></v-radio>
                <v-radio label="하위 레벨 생성" value="subLevel"></v-radio>
              </v-radio-group>
              <v-btn class="mr-4" @click="save">등록</v-btn>
              <v-btn class="mr-4" @click="update">수정</v-btn>
            </form>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  name: 'menuView',
  created () {
    const config = {
      headers: {
        Authorization: this.$store.state.access_token,
        'Content-Type': 'application/json'
      }
    }
    this.$axios.get('/api/menus', config)
      .then((res) => {
        this.menus = res.data
      })
  },
  data: () => ({
    menus: [],
    data: {
      mnuNo: 0,
      preMnuNo: 1,
      mnuLv: 1,
      mnuName: null,
      urlAdr: null,
      useYn: true,
      dpYn: true,
      dpSequence: 1,
      createType: null
    }
  }),
  methods: {
    save: function () {
      console.log('submit')
      console.log(this.data)
      this.test()
      if (this.data.createType === null) {
        alert('!!')
        return false
      }
      console.log('save')
      this.$axios.post('/api/menus', this.data, {
        headers: {
          Authorization: this.$store.state.access_token,
          'Content-Type': 'application/json'
        }
      })
        .then((res) => {
          console.log(res)
          this.$router.go(this.$route.currentRoute)
        }).catch((error) => {
          console.log('Error: ' + JSON.stringify(error.response))
          console.log(error.response.data.detail)
        })
    },
    update: function () {
      console.log('submit')
      console.log(this.data)
      this.$axios.put('/api/menus/' + this.data.mnuNo, this.data, {
        headers: {
          Authorization: this.$store.state.access_token,
          'Content-Type': 'application/json'
        }
      })
        .then((res) => {
          console.log(res)
          this.$router.go(this.$route.currentRoute)
        })
    },
    setMenuList: function () {
      const config = {
        headers: {
          Authorization: this.$store.state.access_token,
          'Content-Type': 'application/json'
        }
      }
      this.$axios.get('/api/menus', config)
        .then((res) => {
          this.menus = res.data
        })
    },
    selectMenu: function (item) {
      console.log('test')
      console.log(item[0])
      console.log(this.active)
      const mnuNo = item[0]
      if (typeof mnuNo === 'undefined') {
        this.data.mnuName = null
        this.data.preMnuNo = 0
        this.data.mnuLv = 0
        this.data.urlAdr = null
        this.data.useYn = true
        this.data.createType = null
        return
      }

      this.$axios.get('/api/menus/' + mnuNo, {
        headers: {
          Authorization: this.$store.state.access_token,
          'Content-Type': 'application/json'
        }
      })
        .then((res) => {
          console.log(res)
          this.data = res.data
        })
    }
  }
}
</script>
