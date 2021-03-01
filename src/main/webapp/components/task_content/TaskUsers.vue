<template>
  <div class="task-users">
    <section>
      <avatar v-for="user in users" :key="user" :user-id="user" />
      <v-btn
        icon
        @click="dialogOpened = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
      <v-dialog
        v-model="dialogOpened"
        max-width="500px"
      >
        <v-card class="pa-5">
          <v-card-title>
            New User
          </v-card-title>
          <v-form>
            <v-select
              v-model="selectedValues"
              :items="allUsers"
              item-value="id"
              item-text="name"
              multiple
              chips
              @change="handleSelectChange"
            />
          </v-form>
        </v-card>
      </v-dialog>
    </section>
  </div>
</template>

<script>
export default {
  name: 'TaskUsers',
  components: {
    Avatar: () => import('@/components/AvatarImage')
  },
  props: {
    users: {
      type: Array,
      default: null
    },
    taskId: {
      type: Number,
      default: null
    }
  },
  async fetch () {
    const users = await this.$repositories.users.getAll()
    for (const userId in users) {
      const ID = parseInt(userId)
      const user = users[ID]
      this.allUsers.push(user)
    }
  },
  data () {
    return {
      allUsers: [],
      dialogOpened: false,
      selectedValues: this.users
    }
  },
  methods: {
    handleSelectChange (newValues) {
      // if user was added or removed
      const added = newValues.filter(x => !this.users.includes(x))
      // new user was added
      if (added.length > 0) {
        const dto = {
          userId: added[0],
          taskId: this.taskId
        }
        // add user
        this.$repositories.tasks.addUser(dto)
          .then((response) => {
            this.$emit('task_users_changed', this.selectedValues)
          })
      } else {
        // remove user
        const removed = this.users.filter(x => !newValues.includes(x))
        this.$repositories.tasks.removeUser(this.taskId, removed[0])
          .then((response) => {
            this.$emit('task_users_changed', this.selectedValues)
          })
      }
    }
  }
}
</script>

<style scoped>

</style>
