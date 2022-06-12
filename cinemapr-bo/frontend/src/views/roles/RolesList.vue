<template>
  <div class="rolesList">
    <v-container fluid>
      asd
      <v-row>
        <v-col>
          <v-card
            class="mt-2 pa-2"
          >
            검색조건 영역
            <v-layout row>
              <v-flex xs2>
                <v-subheader>Label here</v-subheader>
              </v-flex>
              <v-flex xs2>
                <v-text-field />
              </v-flex>
              <v-flex xs2>
                <v-subheader>Label here</v-subheader>
              </v-flex>
              <v-flex xs2>
                <v-text-field />
              </v-flex>
            </v-layout>
            <v-layout row>
              <v-flex xs2>
                <v-subheader>Label here</v-subheader>
              </v-flex>
              <v-flex xs2>
                <v-text-field
                ></v-text-field>
              </v-flex>
            </v-layout>
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
                         :frameworkComponents="frameworkComponents"
                         :defaultColDef="defaultColDef">
            </ag-grid-vue>
            <v-card-actions class="justify-center">
              <v-pagination
                v-model="currentPage"
                :length="totalPages"
                :total-visible="7"
                @input="getPage"
              />
            </v-card-actions>
            <v-card>
              <v-dialog
                v-model="dialog"
                persistent
                max-width="800px"
              >
                <v-card>
                  <v-card-title>
                    <span class="headline">권한 정보</span>
                  </v-card-title>
                  <v-card-text>
                    <v-container>
                      <v-row>
                        <v-col
                          cols="12"
                          sm="6"
                          md="4"
                        >
                          <v-text-field
                            label="Legal first name*"
                            required
                          ></v-text-field>
                        </v-col>
                        <v-col
                          cols="12"
                          sm="6"
                          md="4"
                        >
                          <v-text-field
                            label="Legal middle name"
                            hint="example of helper text only on focus"
                          ></v-text-field>
                        </v-col>
                        <v-col
                          cols="12"
                          sm="6"
                          md="4"
                        >
                          <v-text-field
                            label="Legal last name*"
                            hint="example of persistent helper text"
                            persistent-hint
                            required
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12">
                          <v-text-field
                            label="Email*"
                            required
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12">
                          <v-text-field
                            label="Password*"
                            type="password"
                            required
                          ></v-text-field>
                        </v-col>
                        <v-col
                          cols="12"
                          sm="6"
                        >
                          <v-select
                            :items="['0-17', '18-29', '30-54', '54+']"
                            label="Age*"
                            required
                          ></v-select>
                        </v-col>
                        <v-col
                          cols="12"
                          sm="6"
                        >
                          <v-autocomplete
                            :items="['Skiing', 'Ice hockey', 'Soccer', 'Basketball', 'Hockey', 'Reading', 'Writing', 'Coding', 'Basejump']"
                            label="Interests"
                            multiple
                          ></v-autocomplete>
                        </v-col>
                      </v-row>
                    </v-container>
                    <small>*indicates required field</small>
                  </v-card-text>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn
                      color="blue darken-1"
                      text
                      @click="dialog = false"
                    >
                      Close
                    </v-btn>
                    <v-btn
                      color="blue darken-1"
                      text
                      @click="dialog = false"
                    >
                      Save
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </v-card>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import { AgGridVue } from 'ag-grid-vue'
import RolesButton from '@/components/roles/rolesButton.vue'

export default {
  name: 'rolesList',
  created () {
    this.$axios.get('/api/roles?limit=3').then((res) => {
      this.rowData = res.data.content
      this.currentPage = res.data.number
      this.totalPages = res.data.totalPages
      this.gridApi.setRowData(this.rowData)
      console.log(res.data)
    })
  },
  data: () => ({
    menus: [],
    currentPage: 0,
    totalPages: 0,
    dialog: false,
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
      suppressCellSelection: false
    }
    this.columnDefs = [
      { headerName: 'rolNo', field: 'rolNo' },
      { headerName: 'rolNm', field: 'rolNm' },
      { headerName: 'roles', field: 'roles', cellRenderer: 'rolesButton' },
      { headerName: 'regDate', field: 'regDate' },
      { headerName: 'regNo', field: 'regNo' },
      { headerName: 'modDate', field: 'modDate' },
      { headerName: 'modNo', field: 'modNo' }
    ]
    this.rowData = []
    this.context = { componentParent: this }
    this.frameworkComponents = {
      rolesButton: RolesButton
    }
    this.defaultColDef = {
      editable: false,
      minWidth: 100
    }
    console.log(this.frameworkComponents.rolesButton)
  },
  methods: {
    rolesPopup: function (rolNo) {
      // this.gridApi.refreshCells()
      console.log(`test ${rolNo}`)
      this.dialog = true
    },
    getPage: function (page) {
      this.currentPage = page
      this.$axios.get('/api/roles?limit=3&page=' + this.currentPage).then((res) => {
        console.log(res.data.number)
        this.rowData = res.data.content
        this.currentPage = res.data.number
        this.totalPages = res.data.totalPages
        this.gridApi.setRowData(this.rowData)
      })
    }
  },
  mounted () {
    this.gridApi = this.gridOptions.api
    // this.gridOptions.api.sizeColumnsToFit()
  }
}
</script>
