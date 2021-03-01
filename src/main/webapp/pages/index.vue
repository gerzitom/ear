<template>
  <v-layout
    justify-start
    align-center
    wrap
    pa-10
  >
    <v-container>
      <v-row>
        <v-col
          cols="9"
        >
          <v-row
            justify="center"
          >
            <v-col
              v-if="projects.length === 0"
              cols="auto"
              class="d-flex align-center flex-column justify-center"
            >
              <h1 class="text-center mt-5 mb-5">
                No projects found
              </h1>
              <v-btn
                @click="newProjectDialogOpened = true"
              >
                Add first project
              </v-btn>
            </v-col>
            <v-col
              v-else
            >
              <v-row>
                <v-col
                  v-for="project in projects"
                  :key="project.id"
                  cols="12"
                  sm="6"
                  md="6"
                  lg="6"
                  xl="6"
                >
                  <project-card :data="project" />
                </v-col>
              </v-row>
            </v-col>
          </v-row>
        </v-col>
        <v-col>
          <v-navigation-drawer
            permanent
            absolute
            right
            width="300"
            class="pa-5"
          >
            <h2>Hello {{ $auth.user.name }}</h2>
            <h3 class="pt-4 pb-2">
              Upcomming tasks:
            </h3>
            <task
              v-for="task in userUpcommingTasks"
              :key="task.id"
              :data="task"
              :display-subtasks="false"
              small
              class="pb-1"
            />
          </v-navigation-drawer>
        </v-col>
      </v-row>
    </v-container>

    <v-dialog
      v-model="newProjectDialogOpened"
      persistent
      max-width="500px"
    >
      <template v-slot:activator="{on}">
        <v-btn
          fab
          dark
          bottom
          left
          fixed
          v-on="on"
        >
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </template>
      <new-project @close="newProjectDialogOpened = false" @newproject="newProjectCreated" />
    </v-dialog>
  </v-layout>
</template>

<script>

export default {
  components: {
    projectCard: () => import('~/components/ProjectCard.vue'),
    newProject: () => import('~/components/dialogs/NewProject.vue'),
    Task: () => import('@/components/TaskCard')
  },
  fetch () {
    this.$repositories.users.getUpcommingTasks(this.$auth.user.id)
      .then((response) => {
        this.userUpcommingTasks = response
      })
  },
  data () {
    return {
      projects: [],
      newProjectDialogOpened: false,
      userUpcommingTasks: []
    }
  },
  mounted () {
    this.$repositories.projects.getAll()
      .then((response) => {
        this.projects = response.data
      })
      .catch((error) => {
        this.$nuxt.$emit('error', {
          message: 'No backend connection',
          err: error
        })
      })
  },
  methods: {
    newProjectCreated (data) {
      this.newProjectDialogOpened = false
      this.projects.push(data)
    }
  }
}
</script>
