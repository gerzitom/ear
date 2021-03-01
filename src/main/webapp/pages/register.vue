<template>
  <div class="register">
    <v-main>
      <v-container>
        <v-row
          justify="center"
        >
          <v-cols
            cols="4"
          >
            <v-form
              enctype="multipart/form-data"
              onsubmit="return false"
              @submit="saveUser"
            >
              <h1 class="h1">
                Registration
              </h1>
              <v-text-field
                v-model="userDetails.name"
                label="Name"
              />
              <v-text-field
                v-model="userDetails.username"
                label="Username"
              />
              <v-text-field
                v-model="userDetails.password"
                label="Password"
                type="password"
              />
              <v-file-input
                ref="avatar"
                chips
                counter
                small-chips
                truncate-length="23"
                accept="image/*"
                @change="handleAvatarChange"
              />
              <v-btn
                type="submit"
                class="primary mt-9"
                width="100%"
                :loading="loading"
              >
                Register
              </v-btn>
            </v-form>
          </v-cols>
        </v-row>
      </v-container>
    </v-main>
  </div>
</template>

<script>
export default {
  auth: false,
  name: 'Register',
  data () {
    return {
      loading: false,
      userDetails: {
        name: '',
        username: '',
        password: ''
      },
      avatarImage: null
    }
  },
  methods: {
    saveUser () {
      this.loading = true
      this.$repositories.users.save(this.userDetails)
        .then((response) => {
          this.loading = false
          this.$auth.loginWith('local', { data: this.userDetails })
            .then((loginResponse) => {
              this.$router.push('/')
            })
        })
    },
    handleAvatarChange (file) {
      this.avatarImage = file
    }
  }
}
</script>

<style scoped>

</style>
