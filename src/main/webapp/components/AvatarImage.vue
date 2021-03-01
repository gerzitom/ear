<template>
  <v-avatar
    :size="35"
    :color="'#000'"
  >
    <template
      v-if="user"
    >
      <img v-if="avatarImage" :src="avatarImage" alt="">
      <span v-else class="white--text">{{ acronym }}</span>
    </template>
  </v-avatar>
</template>

<script>
import Diacritics from 'diacritic'

export default {
  name: 'AvatarImage',
  props: {
    userId: {
      type: Number,
      default: null
    }
  },
  async fetch () {
    this.user = await this.$repositories.users.get(this.userId)
  },
  data () {
    return {
      user: null
    }
  },
  computed: {
    acronym () {
      const userNameWithoutDiacritisc = Diacritics.clean(this.user.name)
      return userNameWithoutDiacritisc.match(/\b\w/g).join('')
    },
    avatarImage () {
      return this.user.avatar === null ? null : this.parseImagePath(this.user.avatar.url)
    }
  }
}
</script>

<style scoped>

</style>
