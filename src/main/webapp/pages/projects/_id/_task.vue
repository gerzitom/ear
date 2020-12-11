<template>
  <div class="subtask">
    <v-navigation-drawer
      v-model="drawerOpened"
      width="800"
      fixed
      temporary
      right
    >
      <div v-if="tasks" class="task__content">
        <task-content
          v-for="task in tasks"
          v-show="parseInt(task.taskId) === parseInt($route.params.task)"
          :key="task.taskId"
          :data="task"
          @clicked="drawerOpened = true"
          @delete="handleDelete"
          v-on="$listeners"
        />
      </div>
    </v-navigation-drawer>
  </div>
</template>

<script>
export default {
  name: 'TaskSubpage',
  components: {
    TaskContent: () => import('~/components/TaskContent.vue')
  },
  props: {
    tasks: {
      type: Array,
      default: null
    },
    opened: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      drawerOpened: false
    }
  },
  watch: {
    opened (newVal, oldVal) {
      if (newVal === true) {
        this.drawerOpened = true
      }
    },
    drawerOpened (newVal, oldVal) {
      if (newVal === false) {
        this.$emit('closed')
        this.goToProjectUrl()
      }
    }
  },
  created () {
    if (this.$route.params.task) {
      this.drawerOpened = true
    }
  },
  methods: {
    handleDelete () {
      this.drawerOpened = false
    },
    goToProjectUrl () {
      this.$router.push({
        path: `/projects/${this.$route.params.id}`
      })
    }
  }
}
</script>

<style scoped lang="scss">

</style>
