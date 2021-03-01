<template>
  <div class="user-avatar">
    <div v-if="!$auth.loggedIn">
      <v-btn
        to="/register"
      >
        Register
      </v-btn>
      <v-btn
        to="/login"
        class="primary"
      >
        Log in
      </v-btn>
    </div>
    <nuxt-link
      v-if="activeTrackingTask"
      :to="`/project/${activeTrackingTask.projectId}/${activeTrackingTask.id}`"
      class="text-decoration-none"
    >
      <v-card class="pa-1">
        <p class="overline pa-0 ma-0">
          Tracking on task
        </p>
        <p class="pa-0 ma-0 font-weight-bold">{{ activeTrackingTask.name }}</p>
      </v-card>
    </nuxt-link>
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
            <img v-if="avatar" :src="avatar" alt="">
            <span v-else class="white--text">{{ acronym }}</span>
          </v-avatar>
          <span class="user-avatar__name mb-0 pl-3 subtitle-1">{{ $auth.user.name }}</span>
        </v-btn>
      </template>
      <template>
        <v-list>
          <v-list-item>
            <v-list-item-title>
              <v-btn
                to="/userinfo"
              >
                User settings
              </v-btn>
            </v-list-item-title>
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
import Diacritics from 'diacritic'
export default {
  name: 'UserAvatar',
  async fetch () {
    const activeTracking = await this.$repositories.trackedTime.getUserActiveTracking(this.$auth.user.id)
    if (activeTracking) {
      const response = await this.$repositories.tasks.get(activeTracking.taskId)
      this.activeTrackingTask = response.data
    }
  },
  data () {
    return {
      activeTrackingTask: null
    }
  },
  computed: {
    trackingTime () {
      return this.$store.state.trackingTime.trackingTime
    },
    acronym () {
      const userNameWithoutDiacritisc = Diacritics.clean(this.$auth.user.name)
      return userNameWithoutDiacritisc.match(/\b\w/g).join('')
    },
    avatar () {
      return this.$auth.user.avatar !== null ? this.$const.BACKEND_BASE_URL + this.$auth.user.avatar.url : null
    }
  },
  watch: {
    trackingTime () {
      this.$fetch()
    }
  }
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
