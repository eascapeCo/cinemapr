<template>
  <div class="adminList">
    <v-container fluid>
      관리자 목록
      <v-row>
        <v-col>
          <v-card
            class="mt-2 pa-2"
          >
            검색조건 영역
          </v-card>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-card
            class="pa-2"
          >
            <ag-grid-vue domLayout="autoHeight"
                         class="ag-theme-alpine-dark mt-10"
                         :gridOptions="gridOptions"
                         :columnDefs="columnDefs"
                         :rowData="rowData"
                         :context="context"
                         :defaultColDef="defaultColDef"
                         :animateRows="true"
                         :frameworkComponents="frameworkComponents">
            </ag-grid-vue>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="primary"  v-on:click="test('/adminManagement')">Register</v-btn>
              <v-btn v-on:click="rolesPopup">Explore</v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import { AgGridVue } from 'ag-grid-vue'
import moment from 'moment'
import AdminManagement from '@/components/dialog/AdminManage.vue'

export default {
  name: 'adminList',
  // mount로 변경 (라이프사이클 확인)  ==>  mount는 DOM이 그려지고나서
  created () {
    this.rowData = []
    this.$axios.get('/api/admin').then((res) => {
      this.rowData = res.data
      this.gridApi.setRowData(res.data)
      console.log(this.rowData)
    })
    // this.$request('get', '/api/admin', '',
    //   function (res, i) {
    //     console.log('123123' + res)
    //     this.rowData = res.data
    //     this.gridApi.setRowData(this.rowData)
    //   },
    //   function (e, i) {
    //     console.log(e)
    //   }
    // )
  },
  data: () => ({
    menus: [],
    data: {
      gridOptions: null,
      columnDefs: null,
      rowData: [],
      frameworkComponents: null,
      context: null,
      defaultColDef: null
    }
  }),
  components: {
    AgGridVue
  },
  beforeMount () {
    this.gridOptions = {
      columnDefs: this.columnDefs,
      rowData: this.rowData
      // suppressCellSelection: false
    }
    this.columnDefs = [
      { headerName: '관리자 번호', field: 'admNo' },
      { headerName: '관리자 아이디', field: 'username', cellRenderer: 'adminManagement' },
      { headerName: '등록일자', field: 'regDate', valueFormatter: this.dateFormatter },
      { headerName: '등록자 번호', field: 'regNo' },
      { headerName: '최종 수정 일자', field: 'modDate', valueFormatter: this.dateFormatter },
      { headerName: '최종 수정자 번호', field: 'modNo' },
      { headerName: '비밀번호 수정여부', fleld: 'pwdExpd', checkboxSelection: true }
    ]
    this.rowData = []
    this.context = { componentParent: this }
    this.frameworkComponents = {
      adminManagement: AdminManagement
    }
    this.defaultColDef = {
      editable: false,
      minWidth: 100
    }
    // console.log(this.frameworkComponents.adminManagement)
  },
  methods: {
    test: function (url) {
      this.$router.push(url).catch(error => {
        if (error.name !== 'NavigationDuplicated') {
          throw error
        }
      })
    },
    rolesPopup: function (createElement) {
      this.gridApi.refreshCells()
      // console.log(this.frameworkComponents.adminManagement.template)
    },
    dateFormatter: function (params) {
      // console.log(params)
      return moment(params.value, 'YYYYMMDDhhmmss').format('YYYY/MM/DD HH:mm:ss')
    },
    register: function (params) {

    }
  },
  mounted () {
    this.gridApi = this.gridOptions.api
    // this.gridOptions.api.sizeColumnsToFit()
  }
}

</script>
