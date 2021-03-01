<template>
  <section>
    <avatar v-for="user in users" :key="user" :user-id="user" />
    <template v-if="addButton">
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
    </template>
  </section>
</template>

<script>
export default {
  name: 'ProjectUsers',
  components: {
    Avatar: () => import('@/components/AvatarImage')
  },
  props: {
    users: {
      type: Array,
      default: null
    },
    projectId: {
      type: Number,
      default: null
    },
    addButton: {
      type: Boolean,
      default: true
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
          user: added[0],
          project: this.projectId
        }
        // add user
        this.$repositories.projects.addUser(dto)
      } else {
        // remove user
      }
      this.$emit('project_users_changed', this.selectedValues)
    }
  }
}
</script>

<style scoped>

</style>
