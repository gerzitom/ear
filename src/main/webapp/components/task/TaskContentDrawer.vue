<template>
  <v-navigation-drawer
    v-model="drawerOpened"
    width="800"
    fixed
    temporary
    right
    disable-route-watcher
  >
    <div v-if="$route.params.task" class="task__content">
      <task-details :task-id="+$route.params.task" v-on="$listeners" @task_delete="goToProjectUrl" />
    </div>
  </v-navigation-drawer>
</template>

<script>
export default {
  name: 'DrawerContent',
  components: {
    TaskDetails: () => import('@/components/task/TaskDetailsContent')
  },
  data () {
    return {
      drawerOpened: false
    }
  },
  watch: {
    $route (to, from) {
      if (to.params.task) {
        this.drawerOpened = true
      } else {
        this.drawerOpened = false
      }
    },
    drawerOpened (to, from) {
      if (to === false) {
        this.goToProjectUrl()
      }
    }
  },
  mounted () {
    if (this.$route.params.task) {
      this.drawerOpened = true
    }
  },
  methods: {
    goToProjectUrl () {
      this.$router.push({
        path: `/project/${this.$route.params.id}`
      })
    }
  }
}
</script>

<style scoped>

</style>
