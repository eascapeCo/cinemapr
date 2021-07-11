<template>
  <v-main>
    <v-container
      class="fill-height"
      fluid
    >
      <v-row
        align="center"
        justify="center"
      >
        <v-col
          cols="12"
          sm="8"
          md="4"
        >
          <v-card class="elevation-12">
            <v-toolbar
              color="primary"
              dark
              flat
            >
              <v-toolbar-title>Login form</v-toolbar-title>
              <v-spacer></v-spacer>
            </v-toolbar>
            <v-card-text>
              <!-- <v-form ref="form"> -->
                <v-text-field
                  v-model="user"
                  label="User"
                  name="user"
                  type="text"
                  counter="15"
                  :rules="nameRules"
                  required
                  @keyup.enter="callLogin"
                ></v-text-field>
                <v-text-field
                  v-model="password"
                  label="Password"
                  name="password"
                  type="password"
                  @keyup.enter="callLogin"
                ></v-text-field>
              <!-- </v-form> -->
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="primary" :disabled="!valid" @click="callLogin">Login</v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-main>
</template>

<script>
// import {user} from './api'

export default {
  name: 'login',
  data: () => ({
    loginError: false,
    user: '',
    password: '',
    error: false,
    errors: [],
    valid: true,
    nameRules: [
      v => !!v || 'User is required',
      v => (v && v.length <= 15) || 'User must be valid',
      v => (v && v.length >= 4) || 'User must be valid'
    ]
  }),
  methods: {
    callLogin () {
      this.errors = []
      this.$store.dispatch('LOGIN', {
        id: this.user,
        pwd: this.password
      }).then(() => {
        this.$router.push('/')
      }).catch(error => {
        this.loginError = true
        this.errors.push(error)
        this.error = true
        // this.$alert('ID나 비밀번호를 확인하세요')
      })
    }
  }
}
</script>
