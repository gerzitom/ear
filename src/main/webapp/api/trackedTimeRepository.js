export default class TrackedTimeRepository {
  constructor ($axios, $moment) {
    this.$axios = $axios
    this.$moment = $moment
  }

  startTracking (userId, taskId) {
    const data = {
      userId,
      taskId,
      startTime: this.$moment().format('YYYY-MM-DD HH:mm:ss')
    }
    return this.$axios.$post('/trackedtime', data)
  }

  stopTracking (trackedTimeId, userId, taskId) {
    const data = {
      userId,
      taskId,
      endTime: this.$moment().format('YYYY-MM-DD HH:mm:ss')
    }
    return this.$axios.$put(`/trackedtime/${trackedTimeId}`, data)
  }

  getUserActiveTracking (userId) {
    return this.$axios.$get(`/users/${userId}/activetracking`)
  }
}
