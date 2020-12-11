<template>
  <v-layout
    v-if="projects"
    justify-start
    align-center
    wrap
    pa-10
  >
    <v-row
      justify="between"
    >
      <v-col
        cols="9"
      >
        <v-row>
          <v-col
            v-for="project in projects.data"
            :key="project.projectId"
            cols="4"
          >
            <project-card :data="project" />
          </v-col>
        </v-row>
      </v-col>
    </v-row>

    <v-navigation-drawer
      right
      permanent
      width="400"
      absolute
      class="pa-8"
    >
      <p class="headline">
        Hello, Tom
      </p>
      <v-divider />
    </v-navigation-drawer>

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
    newProject: () => import('~/components/dialogs/NewProject.vue')
  },
  data () {
    return {
      projects: null,
      newProjectDialogOpened: false
    }
  },
  mounted () {
    this.$axios.get('/projects')
      .then((response) => {
        this.projects = response.data
      })
  },
  methods: {
    newProjectCreated (data) {
      this.newProjectDialogOpened = false
      this.projects.data.push(data)
    }
  }
}
</script>
