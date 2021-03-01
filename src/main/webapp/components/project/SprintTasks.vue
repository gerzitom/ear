<template>
  <div class="sprint-tasks">
    <task v-for="task in tasksData" :key="task.id" :data="task" />
  </div>
</template>

<script>
export default {
  name: 'SprintTasks',
  components: {
    Task: () => import('@/components/TaskCard')
  },
  props: {
    tasks: {
      type: Array,
      default: () => []
    }
  },
  fetch () {
    this.tasks.forEach((task) => {
      this.$repositories.tasks.get(task)
        .then((response) => {
          this.tasksData.push(response.data)
        })
    })
  },
  data () {
    return {
      tasksData: []
    }
  }
}
</script>

<style scoped>

</style>
