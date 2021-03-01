<template>
  <div class="user-info">
    <v-container>
      <h1>{{ $auth.user.name }}</h1>
      <h2>{{ $auth.user.username }}</h2>
      <v-btn>Change password</v-btn>
      <v-btn>Change profile picture</v-btn>
    </v-container>
  </div>
</template>

<script>
export default {
  name: 'Userinfo',
  async fetch () {
    this.data = await this.$repositories.users.get(this.$auth.user.id)
  },
  data () {
    return {
      data: null
    }
  }
}

</script>

<style scoped>

</style>
