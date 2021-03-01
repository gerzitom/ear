<template>
  <v-app>
    <v-main>
      <v-container>
        <v-row
          justify="center"
        >
          <v-col
            cols="4"
          >
            <v-form
              onsubmit="return false"
              @submit="userLogin"
            >
              <v-text-field
                v-model="login.username"
                label="Username"
                :error="error.username"
              />
              <v-text-field
                v-model="login.password"
                :error="error.password"
                label="Password"
                type="password"
              />
              <v-btn
                type="submit"
                class="primary login-button mt-10"
                :loading="loading"
              >
                Log in
              </v-btn>
              <v-btn
                class="mt-5"
                width="100%"
                to="/register"
              >
                Register
              </v-btn>
            </v-form>
            <v-alert
              v-if="alertText"
              class="mt-5 text-center"
              color="red"
              dense
              type="error"
              outlined
              v-text="alertText"
            />
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {
  data () {
    return {
      login: {
        username: '',
        password: ''
      },
      error: {
        username: false,
        password: false
      },
      loading: false,
      alertText: ''
    }
  },
  methods: {
    userLogin () {
      this.loading = true
      this.$auth.loginWith('local', { data: this.login })
        .catch((error) => {
          this.alertText = error.response.data.message
          this.error.username = true
          this.error.password = true
        })
        .finally(() => {
          this.loading = false
        })
    }
  }
}
</script>

<style scoped lang="scss">
  .login-button{
    width: 100%;
  }
</style>
