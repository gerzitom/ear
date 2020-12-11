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
              />
              <v-text-field
                v-model="login.password"
                label="Password"
                type="password"
              />
              <v-btn
                type="submit"
                class="primary login-button"
              >
                Log in
              </v-btn>
            </v-form>
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
      }
    }
  },
  methods: {
    async userLogin () {
      try {
        const response = await this.$auth.loginWith('local', { data: this.login })
        console.log(response)
      } catch (err) {
        console.log(err)
      }
    }
  }
}
</script>

<style scoped lang="scss">
  .login-button{
    width: 100%;
  }
</style>
