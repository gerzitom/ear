<template>
  <div class="project-tasks">
    <h2 class="pb-5">
      Tasks
    </h2>
    <div v-if="tasksLoading" class="project__tasks">
      <v-skeleton-loader max-width="40%" type="list-item-two-line" />
      <v-skeleton-loader max-width="35%" type="list-item-two-line" />
      <v-skeleton-loader max-width="44%" type="list-item-two-line" />
    </div>
    <div v-else class="tasks">
      <template v-if="tasks.length > 0">
        <v-lazy
          v-for="task in tasksInProgress"
          :key="task.id"
          v-model="tasks"
        >
          <task :data="task" :params="$route.params" />
        </v-lazy>
      </template>
      <template v-else>
        <p class="text">
          No tasks found
        </p>
        <v-btn @click="newTaskDialogOpened = true">
          Create new task
        </v-btn>
      </template>
    </div>

    <v-dialog
      v-model="newTaskDialogOpened"
      persistent
      max-width="500px"
    >
      <template v-slot:activator="{on}">
        <v-btn
          fab
          dark
          bottom
          left
          fixed
          v-on="on"
        >
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </template>
      <new-task @closed="newTaskDialogOpened = false" @success="newTaskCreated" />
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: 'Tasks',
  components: {
    task: () => import('@/components/TaskCard'),
    NewTask: () => import('~/components/dialogs/NewTask.vue')
  },
  props: {
    projectId: {
      type: Number,
      default: null
    }
  },
  async fetch () {
    const response = await this.$repositories.projects.getTasks(this.projectId)
    this.tasks = response.data
    this.tasksLoading = false
    this.$emit('tasks_updated', this.tasks)
  },
  data () {
    return {
      tasks: [],
      tasksLoading: true,
      newTaskDialogOpened: false
    }
  },
  computed: {
    tasksInProgress () {
      const tasks = this.tasks.filter(task => task.state !== 'DONE')
      return tasks
    }
  },
  methods: {
    newTaskCreated (newTask) {
      this.tasks.push(newTask)
      this.newTaskDialogOpened = false
    },
    refresh () {
      this.$fetch()
    }
  }
}
</script>

<style >
.v-treeview-node__root{
  cursor: pointer;
}
</style>
