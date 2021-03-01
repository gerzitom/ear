export default class UserRepository {
  constructor ($axios) {
    this.$axios = $axios
    this.users = null
  }

  /**
   * Saves user
   * @param userData
   * @returns {Promise<any>}
   */
  save (userData) {
    return this.$axios.$post('/users', userData)
  }

  async getAll () {
    if (this.users === null) {
      this.users = await this.$axios.$get('/users')
    }
    return this.users
  }

  async get (userId) {
    if (this.users === null) {
      await this.getAll()
    }
    return this.users[userId]
  }

  getUpcommingTasks (userId) {
    return this.$axios.$get(`/users/${userId}/upcomming_tasks`)
  }
}
