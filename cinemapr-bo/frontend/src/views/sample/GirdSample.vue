<template>
  <div class="girdSample">
    <v-container>
      <v-row>
        <v-col>
          <v-card
            class="pa-2"
            tile
            outlined
          >
            <form>
              <v-subheader>Form</v-subheader>
              <v-text-field v-model="name" label="Name" />
              <v-select v-model="useYn" :items="[true, false]" label="사용여부"></v-select>
              <v-btn class="mr-4" @click="submit">submit</v-btn>
              <v-btn>clear</v-btn>
            </form>
          </v-card>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-card
            class="pa-2"
            tile
            outlined
          >
            col1<br>
            col1<br>
            col1<br>
            col1<br>
            col1<br>
            col1<br>
            col1<br>
          </v-card>
        </v-col>
        <v-col>
          <v-card
            class="pa-2"
            tile
            outlined
          >
            col2
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
  name: 'GirdSample',
  data: () => ({
    name: null,
    useYn: false
  }),
  methods: {
    submit: function () {
      console.log('submit')
      console.log(this.name)
    }
  }
}
</script>
