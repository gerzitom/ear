<template>
  <div class="user-avatar">
    <div v-if="!$auth.loggedIn">
      <v-btn
        to="/login"
        class="primary"
      >
        Log in
      </v-btn>
    </div>
    <v-menu
      v-if="$auth.loggedIn"
      offset-y
    >
      <template v-slot:activator="{ on }">
        <v-btn
          text
          v-on="on"
        >
          <v-avatar
            :size="40"
            :color="'#000'"
          >
            <span class="white--text">TG</span>
          </v-avatar>
          <span class="user-avatar__name mb-0 pl-3 subtitle-1">{{ $auth.user.name }}</span>
        </v-btn>
      </template>
      <template>
        <v-list>
          <v-list-item>
            <v-list-item-title>User settings</v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              <v-btn
                text
                @click="$auth.logout()"
              >
                Log out
              </v-btn>
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </template>
    </v-menu>
  </div>
</template>

<script>
export default {
  name: 'UserAvatar'
}
</script>

<style scoped lang="scss">
  .user-avatar{
    display: flex;
    align-items: center;
    &__name{
    }
  }
</style>
