<template>
  <div class="task-content">
    <v-row v-if="data">
      <v-col
        cols="8"
      >
        <div class="task-content__main pa-10">
          <h3 class="display-1 font-weight-bold mb-2">
            <span>{{ data.name }}</span>
          </h3>
          <v-row>
            <v-col>
              <p class="caption mb-1">
                Úkol #{{ data.id }}
              </p>
            </v-col>
            <v-col
              cols="auto"
            >
              <v-btn
                text
                small
                color="error"
                @click="handleDelete"
              >
                Odstranit
              </v-btn>
            </v-col>
          </v-row>
          <v-divider class="mb-5" />
          <p v-if="data.description">
            {{ data.description }}
          </p>
          <p v-if="!data.description">
            Žádný popis není dostupný.
          </p>
          <v-divider class="my-5" />
          <subtasks
            :subtasks="data.subtasks"
            @success="handleNewSubtask"
          />
          <v-divider class="my-5" />
          <comments
            :task-id="data.id"
          />
        </div>
      </v-col>
      <v-col
        class="task-content__sidebar pr-6"
      >
        <h4 class="hedline mt-7">
          Deadline
        </h4>
        <deadline
          :isostring="data.deadline"
          @updated="handleDeadlineUpdate"
        />
        <p class="overline">
          <task-state :task-id="taskId" v-on="$listeners" />
        </p>
        <p>
          <task-users :users="data.users" :task-id="taskId" @task_users_changed="handleNewUser" />
        </p>
        <h3 class="mt-7">
          Time tracking
        </h3>
        <timer
          :task-id="data.id"
        />
      </v-col>
    </v-row>
  </div>
</template>

<script>
import Timer from '@/components/task_content/Timer'
import Comments from '@/components/task_content/Comments'
import Subtasks from '@/components/task_content/Subtasks'
import TaskState from '@/components/task_content/State'

export default {
  name: 'TaskDetailsContent',
  components: {
    Timer,
    Comments,
    Subtasks,
    TaskState,
    deadline: () => import('~/components/Deadline.vue'),
    TaskUsers: () => import('@/components/task_content/TaskUsers')
  },
  props: {
    taskId: {
      type: Number,
      default: null
    }
  },
  async fetch () {
    const response = await this.$repositories.tasks.get(this.taskId)
    this.data = response.data
  },
  data () {
    return {
      newTaskDialogOpened: false,
      data: null
    }
  },
  watch: {
    taskId (val) {
      this.$fetch()
    }
  },
  methods: {
    handleDeadlineUpdate (newDeadline) {
      // const date = new Date(Date.parse(newDeadline))
      const data = {
        deadline: newDeadline
      }
      this.$repositories.tasks.update(this.data.id, data)
        .then((result) => {
        })
    },
    handleNewSubtask (subtask) {
      this.newTaskDialogOpened = false
      this.subtasks.push(subtask)
    },
    handleNewUser (newUser) {
      this.data.users = newUser
    },
    handleDelete () {
      this.$repositories.tasks.delete(this.data.id)
        .then((response) => {
          this.$emit('task_delete', this.data.id)
        })
    }
  }
}
</script>

<style scoped lang="scss">
.task-content{
  &__sidebar{
    background-color: $grey;
    min-height: 100vh;
  }
}
</style>
