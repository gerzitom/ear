<template>
  <div class="task-content">
    <v-row>
      <v-col
        cols="8"
      >
        <div class="task-content__main pa-10">
          <h3 class="display-1 font-weight-bold mb-2">
            <task-button
              :completed="data.state_id === 2"
              @uncompleted="$emit('uncompleted', data.taskId)"
              @completed="$emit('completed', data.taskId)"
            />
            <span>{{ data.name }}</span>
          </h3>
          <v-row>
            <v-col>
              <p class="caption mb-1">
                Úkol #{{ data.taskId }}
              </p>
            </v-col>
            <v-col
              cols="auto"
            >
              <v-btn
                text
                small
                color="error"
                @click="$emit('delete', data.taskId)"
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
            :subtasks="subtasks"
            @success="handleNewSubtask"
          />
          <v-divider class="my-5" />
          <comments
            :task-id="data.taskId"
            :comments="data.comments"
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
        <h4 class="hedline mt-7">
          Stav
        </h4>
        <p class="overline">
          {{ data.state.name }}
        </p>
        <timer
          :task-id="data.taskId"
        />
      </v-col>
    </v-row>
  </div>
</template>

<script>
import Timer from './task_content/Timer'
export default {
  name: 'TaskContent',
  components: {
    Timer,
    subtasks: () => import('~/components/task_content/Subtasks.vue'),
    comments: () => import('~/components/task_content/Comments.vue'),
    deadline: () => import('~/components/Deadline.vue'),
    taskButton: () => import('~/components/TaskButton.vue')
  },
  props: {
    data: {
      type: Object,
      default: null
    }
  },
  data () {
    return {
      comments: null,
      subtasks: null,
      newTaskDialogOpened: false
    }
  },
  created () {
    this.$axios.get('tasks/' + this.data.taskId + '/comments')
      .then((response) => {
        this.comments = response.data.data
      })
    this.$axios.get('tasks/' + this.data.taskId + '/subtasks')
      .then((response) => {
        this.subtasks = response.data
      })
  },
  methods: {
    handleDeadlineUpdate (newDeadline) {
      // const date = new Date(Date.parse(newDeadline))
      const data = {
        deadline: newDeadline
      }
      this.$axios.put(`/tasks/${this.data.taskId}`, data).then((result) => {
        console.log(result)
      })
    },
    handleNewSubtask (subtask) {
      this.newTaskDialogOpened = false
      this.subtasks.push(subtask)
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
