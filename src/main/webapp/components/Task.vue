<template>
  <div v-if="data" class="task" :class="{'task--small' : small, 'task--completed' : data.state_id === 2}">
    <div class="task__container">
      <v-btn
        icon
        @click="taskMarkedAsCompleted"
        @mouseover="hovered = true"
        @mouseout="hovered = false"
      >
        <v-icon v-show="!hovered && !completed || completed && hovered">
          mdi-checkbox-blank-circle-outline
        </v-icon>
        <v-icon v-show="hovered && !completed || completed && !hovered">
          mdi-checkbox-marked-circle-outline
        </v-icon>
      </v-btn>
      <nuxt-link :to="{path: `/projects/${$route.params.id}/${data.taskId}`}" class="task__body" @click.native="$emit('clicked')">
        <p class="task__name black--text mb-0" :class="small ? 'subtitle-1' : 'title'">
          {{ data.name }}
        </p>
        <div v-if="deadline" :class="{'red--text' : isCriticalDeadlineDiffFromNow}" class="task__deadline overline">
          {{ deadline }}
        </div>
      </nuxt-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Task',
  components: {
  },
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
      if (this.data.deadline !== undefined) {
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
        this.$emit('completed', this.data.taskId)
      } else {
        this.$emit('uncompleted', this.data.taskId)
      }
    }
  }
}
</script>

<style scoped lang="scss">
  .task{
    margin: 0.5em 0;
    &__container{
      display: flex;
      align-items: center;
    }
    &__select-button{
      border-radius: 50%;
      border: 1px solid black;
      $size: 20px;
      width: $size;
      height: $size;
    }
    &__body{
      margin-left: 1em;
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
