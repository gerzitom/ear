<template>
  <div v-if="data" class="task" :class="{'task--small' : small, 'task--completed' : data.state_id === 2}">
    <v-row
      align="center"
    >
      <v-col class="ma-0 py-0">
        <nuxt-link :to="{path: `/project/${$route.params.id}/${data.id}`}" class="task__body">
          <p class="task__name black--text mb-0" :class="small ? 'subtitle-1' : 'title'">
            {{ data.name }}
          </p>
          <div v-if="deadline" :class="{'red--text' : isCriticalDeadlineDiffFromNow}" class="task__deadline overline">
            {{ deadline }}
          </div>
        </nuxt-link>
      </v-col>
    </v-row>
    <v-row v-if="data.subtasks.length > 0 && displaySubtasks">
      <v-col class="pa-0">
        <ul class="pl-8">
          <li v-for="subtask in data.subtasks" :key="subtask.id" style="list-style-type: none">
            <task-card :data="subtask" :small="small" />
          </li>
        </ul>
      </v-col>
    </v-row>
  </div>
</template>

<script>
export default {
  name: 'TaskCard',
  props: {
    data: {
      type: Object,
      default: null
    },
    params: {
      type: Object,
      default: null
    },
    small: {
      type: Boolean,
      default: false
    },
    displaySubtasks: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      drawer: false,
      hovered: false
    }
  },
  computed: {
    deadline () {
      if (this.data.deadline !== null) {
        const moment = this.$moment(this.data.deadline)
        return moment.format('DD. MM. YYYY')
      } else {
        return ''
      }
    },
    isCriticalDeadlineDiffFromNow () {
      if (this.data.deadline) {
        const subtract = this.$moment(this.data.deadline).diff(this.$moment(), 'days')
        return subtract < 1
      }
      return ''
    },
    completed () {
      return this.data.state_id === 2
    }
  },
  methods: {
    taskMarkedAsCompleted () {
      if (!this.completed) {
        this.$emit('completed', this.data.id)
      } else {
        this.$emit('uncompleted', this.data.id)
      }
    }
  }
}
</script>

<style scoped lang="scss">
  .task{
    margin: 0.5em 0;
    &__select-button{
      border-radius: 50%;
      border: 1px solid black;
      $size: 20px;
      width: $size;
      height: $size;
    }
    &__body{
      cursor: pointer;
      text-decoration: none;
    }
    &--small{
      margin: 0;
    }
    &--completed{
      opacity: 0.6;
    }
  }

</style>
