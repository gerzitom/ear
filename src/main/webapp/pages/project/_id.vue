<template>
  <div class="project">
    <v-container>
      <info :data="projectInfo" :progress="progress" class="mb-8" @project_delete="deleteProject" @project_update="handleProjectUpdate" />
      <sprint v-if="projectInfo" :data="projectInfo.currentSprint" />
      <tasks ref="tasks" :project-id="+this.$route.params.id" class="mt-5" @tasks_updated="updateTasks" />
      <nuxt-child @task_state_changed="handleTaskStateChange" @task_delete="handleTaskDelete" />
      <v-dialog
        v-model="updateFormDialog"
        max-width="500px"
      >
        <v-card
          class="pa-5"
        >
          <v-form
            onsubmit="return false"
            @submit="formSubmit"
          >
            <v-container>
              <v-row>
                <v-col>
                  <v-text-field
                    v-model="updateForm.name"
                    outlined
                  />
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-menu
                    content-class="new-task__datepicker-menu"
                    :offset-y="true"
                  >
                    <template v-slot:activator="{on}">
                      <v-text-field outlined :value="updateForm.deadline" label="Deadline" readonly v-on="on">
                        <template v-slot:append>
                          <v-icon>
                            mdi-calendar-month
                          </v-icon>
                        </template>
                      </v-text-field>
                    </template>
                    <v-date-picker
                      v-model="updateForm.deadline"
                      :min="$moment().format('YYYY-MM-DD')"
                    />
                  </v-menu>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-btn
                    @click="formSubmit"
                  >
                    Update
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-form>
        </v-card>
      </v-dialog>
    </v-container>
  </div>
</template>

<script>
export default {
  name: 'Project',
  components: {
    info: () => import('~/components/project/ProjectInfo'),
    tasks: () => import('~/components/project/tasks'),
    Sprint: () => import('@/components/project/Sprint')
    // mindmap: () => import('~/components/mindmap')
  },
  async fetch () {
    this.projectInfo = await this.$repositories.projects.get(this.$route.params.id)
    this.updateForm.users = this.projectInfo.users
    this.updateForm.name = this.projectInfo.name
    this.updateForm.deadline = this.projectInfo.deadline
  },
  data () {
    return {
      projectInfo: null,
      projectTasks: [],
      progress: null,
      tasksProgress: null,
      drawer: false,
      newTaskDialogOpened: false,
      tasksLoading: true,
      updateFormDialog: false,
      updateForm: {
        name: '',
        deadline: null
      }
    }
  },
  methods: {
    formSubmit (ev) {
      ev.preventDefault()
      this.$repositories.projects.update(this.projectInfo.id, this.updateForm)
        .then((response) => {
          this.projectInfo.name = this.updateForm.name
          this.projectInfo.deadline = this.updateForm.deadline
          this.updateFormDialog = false
        })
    },
    updateTasks (tasks) {
      this.projectTasks = tasks
      this.computeProgress()
    },
    computeProgress () {
      this.tasksProgress = {
        done: 0,
        inProgress: 0
      }
      if (this.projectTasks.length === 0) {
        this.progress = 0
      } else {
        this.projectTasks.forEach((task) => {
          this.loadProgress(this.tasksProgress, task)
        })
        this.progress = 100 * (this.tasksProgress.done / (this.tasksProgress.done + this.tasksProgress.inProgress))
      }
    },
    handleProjectUpdate () {
      this.updateFormDialog = true
    },
    loadProgress (progress, task) {
      if (task.state === 'DONE') {
        progress.done++
      } else {
        progress.inProgress++
      }
      if (task.subtasks.length > 0) {
        task.subtasks.forEach((task) => {
          this.loadProgress(progress, task)
        })
      }
      return progress
    },
    handleTaskStateChange (data) {
      this.projectTasks.forEach((task) => {
        this.updateTaskState(task, data)
      })
      this.computeProgress()
    },
    updateTaskState (task, data) {
      if (task.id === data.taskId) {
        task.state = data.state
      } else if (task.subtasks.length > 0) {
        task.subtasks.forEach((task) => {
          this.updateTaskState(task, data)
        })
      }
    },
    handleTaskDelete (taskId) {
      this.$refs.tasks.refresh()
    },
    newTaskCreated (newTask) {
      newTask.isSubtask = []
      newTask.comments = []
      newTask.stateId = 1
      newTask.state = this.getState(newTask.stateId)
      this.projectTasks.push(newTask)
      this.newTaskDialogOpened = false
    },
    showTask (task) {
      // return task.state_id === 1 && task.isSubtask.length === 0
      return true
    },
    showCompletedTask (task) {
      return task.state_id === 2 && task.isSubtask.length === 0
    },
    markAsCompleted (taskId) {
      // const completedTask = this.projectTasks.find(task => task.taskId === taskId)
      const data = {
        state_id: 2
      }
      this.$axios.put(`/tasks/${taskId}`, data)
        .then((response) => {
          this.projectTasks.forEach((task) => {
            if (task.id === taskId) {
              task.state_id = 2
            }
          })
        })
    },
    markAsUncompleted (taskId) {
      // const completedTask = this.projectTasks.find(task => task.taskId === taskId)
      const data = {
        state_id: 1
      }
      this.$axios.put(`/tasks/${taskId}`, data)
        .then((response) => {
          this.projectTasks.forEach((task) => {
            if (task.id === taskId) {
              task.state_id = 1
            }
          })
        })
    },
    deleteTask (taskId) {
      this.$axios.delete(`/tasks/${taskId}`)
        .then((response) => {
          this.projectTasks = this.projectTasks.filter(function (task) {
            return task.taskId !== taskId
          })
        })
    },
    deleteProject (projectId) {
      this.$repositories.projects.delete(projectId)
        .then((response) => {
          this.projectTasks = this.projectTasks.filter(item => item.id !== projectId)
          this.$router.push({
            path: '/'
          })
        })
    }
  }
}
</script>

<style scoped>

</style>
