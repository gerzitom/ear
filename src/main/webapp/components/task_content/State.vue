<template>
  <div class="task-state">
    <v-select
      v-model="state"
      :items="stateItems"
      item-text="text"
      item-value="value"
      :loading="loading"
      dense
      solo
    />
  </div>
</template>

<script>
export default {
  name: 'TaskState',
  props: {
    taskId: {
      type: Number,
      default: null
    }
  },
  async fetch () {
    this.stateItems = this.$repositories.tasks.getTaskStates()
    const response = await this.$repositories.tasks.get(this.taskId)
    this.data = response.data
    this.state = this.data.state
    this.loading = false
  },
  data () {
    return {
      loading: true,
      state: null,
      stateItems: []
    }
  },
  watch: {
    state (to, from) {
      if (from != null) {
        this.loading = true
        const updateData = {
          state: to
        }
        this.$repositories.tasks.update(this.taskId, updateData)
          .then(() => {
            this.loading = false
            this.$emit('task_state_changed', {
              taskId: this.taskId,
              state: to
            })
          })
      }
    }
  }
}
</script>

<style scoped>

</style>
