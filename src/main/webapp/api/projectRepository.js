export default class ProjectRepository {
  constructor ($axios) {
    this.$axios = $axios
    this.tasks = []
  }

  async get (id) {
    const response = await this.$axios.get(`/projects/${id}`)
    return response.data
  }

  getAll () {
    return this.$axios.get('/projects')
  }

  getTasks (id) {
    return this.$axios.get(`/projects/${id}/tasks`)
  }

  addUser (dto) {
    return this.$axios.post('/projects/users', dto)
  }

  removeUser (projectId, userId) {
    return this.$axios.$delete(`/projects/${projectId}/users/${userId}`)
  }

  create (dto) {
    return this.$axios.post('/projects', dto)
  }

  update (projectId, dto) {
    return this.$axios.$put(`/projects/${projectId}`, dto)
  }

  delete (projectId) {
    return this.$axios.$delete(`/projects/${projectId}`)
  }
}
