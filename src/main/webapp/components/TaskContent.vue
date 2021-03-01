<template>
  <div class="task-content">
    <v-row v-if="data">
      <v-col
        cols="8"
      >
        <div class="task-content__main pa-10">
          <h3 class="display-1 font-weight-bold mb-2">
            <task-button
              @uncompleted="$emit('uncompleted', data.id)"
              @completed="$emit('completed', data.id)"
            />
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
                @click="$emit('delete', data.id)"
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
        <h4 class="hedline mt-7">
          Stav
        </h4>
        <p class="overline">
          {{ data.state }}
        </p>
        <timer
          :task-id="data.id"
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
    taskId: {
      type: Number,
      default: null
    }
  },
  fetch () {
    this.$repositories.tasks.get(this.taskId)
      .then((response) => {
        this.data = response.data
      })
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
  mounted () {
    this.$fetch()
  },
  methods: {
    handleDeadlineUpdate (newDeadline) {
      // const date = new Date(Date.parse(newDeadline))
      const data = {
        deadline: newDeadline
      }
      this.$repositories.tasks.update(data, this.data.id)
        .then((result) => {
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
