<template>
  <div class="subtaks">
    <v-row
      align="center"
      justify="space-between"
    >
      <v-col>
        <h3 class="headline">
          Subtasks
        </h3>
      </v-col>
      <v-col
        cols="auto"
      >
        <v-dialog
          v-model="newTaskDialogOpened"
          persistent
          max-width="500px"
        >
          <template
            v-slot:activator="{ on }"
          >
            <v-btn
              color="primary"
              small
              text
              v-on="on"
            >
              Add new subtask
            </v-btn>
          </template>
          <new-task
            :parent-task="parseInt($route.params.task)"
            @closed="newTaskDialogOpened = false"
            @success="handleNewSubtask"
          />
        </v-dialog>
      </v-col>
    </v-row>
    <v-list v-if="subtasks && subtasks.length > 0">
      <v-list-item v-for="subtask in subtasks" :key="subtask.id">
        <subtask :data="subtask" v-on="$listeners" />
      </v-list-item>
    </v-list>
  </div>
</template>

<script>
export default {
  name: 'Subtasks',
  components: {
    newTask: () => import('~/components/dialogs/NewTask.vue'),
    subtask: () => import('~/components/Subtask')
  },
  props: {
    subtasks: {
      type: Array,
      default: null
    }
  },
  data () {
    return {
      newTaskDialogOpened: false
    }
  },
  methods: {
    handleNewSubtask (subtask) {
      this.newTaskDialogOpened = false
      this.subtasks.push(subtask)
      this.$emit('subtaskadded', subtask)
    }
  }
}
</script>

<style scoped lang="scss">

</style>
