<template>
  <v-simple-table>
    <thead>
      <tr>
        <th>name</th>
        <th>Time</th>
      </tr>
    </thead>
    <tbody v-if="parsedTimes">
      <tr v-for="parsedTime in parsedTimes" :key="parsedTime.user.id">
        <td>{{ parsedTime.user.name }}</td>
        <td>{{ formatTime(parsedTime.time) }}</td>
      </tr>
    </tbody>
    <tbody v-else>
      <tr v-for="i in 3" :key="i">
        <td><v-skeleton-loader type="text" /></td>
        <td><v-skeleton-loader type="text" /></td>
      </tr>
    </tbody>
  </v-simple-table>
</template>

<script>
export default {
  name: 'UserTrackedTimes',
  props: {
    times: {
      type: Object,
      default: null
    }
  },
  async fetch () {
    this.users = await this.$repositories.users.getAll()
  },
  data () {
    return {
      users: []
    }
  },
  computed: {
    parsedTimes () {
      if (this.users.length === 0) {
        return null
      }
      const ret = []
      for (const userId in this.times) {
        ret.push({
          user: this.users[userId],
          time: this.times[userId]
        })
      }
      return ret
    }
  },
  methods: {
    formatTime (seconds) {
      return this.$moment().startOf('day').add(seconds, 's').format('HH:mm')
    }
  }
}
</script>

<style scoped>

</style>
