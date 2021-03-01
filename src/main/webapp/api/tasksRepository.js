export default class TasksRepository {
  constructor ($axios) {
    this.$axios = $axios
  }

  /**
   *
   * @param id
   * @returns {Promise<AxiosResponse<any>>}
   */
  get (id) {
    return this.$axios.get(`/tasks/${id}`)
  }

  /**
   *
   * @returns {Promise<AxiosResponse<any>>}
   */
  getAll () {
    return this.$axios.get('/tasks')
  }

  save (taskData) {
    return this.$axios.$post('/tasks', taskData)
  }

  saveSubtask (parentTaskId, taskData) {
    return this.$axios.$post(`/tasks/${parentTaskId}/subtasks`, taskData)
  }

  /**
   *
   * @param id
   * @param data
   * @returns {Promise<any>}
   */
  update (id, data) {
    return this.$axios.$put(`/tasks/${id}`, data)
  }

  delete (taskId) {
    return this.$axios.$delete(`/tasks/${taskId}`)
  }

  /**
   * @param id
   * @returns {Promise<AxiosResponse<any>>}
   */
  getComments (id) {
    return this.$axios.get(`/tasks/${id}/comments`)
  }

  /**
   *
   * @param id
   * @param data
   * @returns {Promise<any>}
   */
  addComment (id, data) {
    return this.$axios.$post(`/tasks/${id}/comments`, data)
  }

  getTrackedTimes (taskId) {
    return this.$axios.$get(`/tasks/${taskId}/trackedtime`)
  }

  addUser (dto) {
    return this.$axios.$post('/tasks/users', dto)
  }

  removeUser (taskId, userId) {
    return this.$axios.$delete(`/tasks/${taskId}/users/${userId}`)
  }

  /**
   * @param value
   * @returns state for v-select component
   */
  getTaskStateByValue (value) {
    let finalState = null
    this.getTaskStates().forEach((state) => {
      if (state.value === value) {
        finalState = state
      }
    })
    return finalState
  }

  getTaskStates () {
    return [
      { text: 'In progress', value: 'IN_PROGRESS' },
      { text: 'Done', value: 'DONE' }
    ]
  }
}
