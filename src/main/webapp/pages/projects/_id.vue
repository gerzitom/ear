<template>
  <div class="project">
    <v-container v-if="projectInfo">
      <div class="project__head">
        <v-row
          justify="space-between"
          align="center"
        >
          <v-col>
            <h1 class="display-3 mt-8 mb-5">
              {{ projectInfo.name }}
            </h1>
          </v-col>
          <v-col
            cols="auto"
          >
            <v-speed-dial
              direction="bottom"
              open-on-hover
            >
              <template v-slot:activator>
                <v-btn
                  icon
                >
                  <v-icon>mdi-dots-vertical</v-icon>
                </v-btn>
              </template>
              <v-card
                outlined
                min-width="150px"
              >
                <v-btn
                  text
                >
                  Upravit
                </v-btn>
                <v-btn
                  text
                  class="red--text"
                >
                  Odstranit
                </v-btn>
              </v-card>
            </v-speed-dial>
          </v-col>
        </v-row>
        <v-progress-linear value="50" />
      </div>
      <div class="project__boby mt-8">
        <h2>Tasky</h2>
        <div v-if="projectTasks.length === 0" class="project__tasks">
          <v-skeleton-loader max-width="40%" type="list-item-two-line" />
          <v-skeleton-loader max-width="35%" type="list-item-two-line" />
          <v-skeleton-loader max-width="44%" type="list-item-two-line" />
        </div>
        <div class="project__tasks">
          <v-lazy
            v-for="projectTask in projectTasks"
            :key="projectTask.taskId"
            v-model="projectTasks"
          >
            <task v-if="showTask(projectTask)" :data="projectTask" :params="$route.params" @completed="markAsCompleted" @clicked="drawer = true" />
          </v-lazy>
          <nuxt-child
            :opened="drawer"
            :tasks="projectTasks"
            @closed="drawer = false"
            @completed="markAsCompleted"
            @uncompleted="markAsUncompleted"
            @delete="deleteTask"
          />
        </div>

        <div class="project__tasks project__tasks--finished">
          <h2>Dokončené úkoly</h2>
          <v-lazy
            v-for="projectTask in projectTasks"
            :key="projectTask.taskId"
            v-model="projectTasks"
          >
            <task v-if="showCompletedTask(projectTask)" :data="projectTask" :params="$route.params" @uncompleted="markAsUncompleted" @clicked="drawer = true" />
          </v-lazy>
        </div>
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
    </v-container>
  </div>
</template>

<script>
export default {
  name: 'ProjectPage',
  components: {
    task: () => import('~/components/Task.vue'),
    NewTask: () => import('~/components/dialogs/NewTask.vue')
  },
  data () {
    return {
      projectInfo: null,
      projectTasks: [],
      progress: null,
      drawer: false,
      newTaskDialogOpened: false
    }
  },
  created () {
    this.$axios.get('/projects/' + this.$route.params.id)
      .then((response) => {
        this.projectInfo = response.data.data
      })

    this.$axios.get('/projects/' + this.$route.params.id + '/tasks')
      .then((response) => {
        response.data.data.forEach((task) => {
          this.projectTasks.push(task)
        })
      })
    this.$axios.get('/projects/' + this.$route.params.id + '/progress')
      .then((response) => {
        this.progress = response.data
      })
  },
  methods: {
    newTaskCreated (newTask) {
      newTask.isSubtask = []
      newTask.comments = []
      newTask.stateId = 1
      newTask.state = this.getState(newTask.stateId)
      this.projectTasks.push(newTask)
      this.newTaskDialogOpened = false
    },
    showTask (task) {
      return task.state_id === 1 && task.isSubtask.length === 0
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
            if (task.taskId === taskId) {
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
            if (task.taskId === taskId) {
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
    }
  }
}
</script>

<style scoped lang="scss">
  .project{
    &__tasks{
      min-height: 50vh;
    }
  }

</style>
