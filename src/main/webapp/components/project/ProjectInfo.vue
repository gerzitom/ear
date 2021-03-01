<template>
  <div class="project-info">
    <v-row
      v-if="data"
      justify="space-between"
      align="center"
    >
      <v-col>
        <h1 class="display-3 mt-8 mb-5">
          {{ data.name }}
        </h1>
        <project-users :users="data.users" :project-id="data.id" @project_users_changed="handleUsersChange" />
      </v-col>
      <v-col
        cols="auto"
      >
        <v-speed-dial
          direction="bottom"
          open-on-hover
        >
          <template v-slot:activator>
            <v-btn
              icon
            >
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </template>
          <v-card
            outlined
            min-width="150px"
          >
            <v-btn
              text
              @click="$emit('project_update')"
            >
              Upravit
            </v-btn>
            <v-btn
              text
              class="red--text"
              @click="$emit('project_delete', data.id)"
            >
              Odstranit
            </v-btn>
          </v-card>
        </v-speed-dial>
      </v-col>
    </v-row>
    <p class="caption mb-1">
      Progress: {{ formatedProgress }} %
    </p>
    <v-progress-linear :value="progress" />
  </div>
</template>

<script>
export default {
  name: 'ProjectInfo',
  components: {
    ProjectUsers: () => import('@/components/project/ProjectUsers')
  },
  props: {
    data: {
      type: Object,
      default: null
    },
    progress: {
      type: Number,
      default: 0
    }
  },
  computed: {
    formatedProgress () {
      return Math.round(this.progress)
    }
  },
  methods: {
    handleUsersChange (newUsers) {
      this.data.users = newUsers
    }
  }

}
</script>

<style scoped>

</style>
