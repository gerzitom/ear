<template>
  <nuxt-link class="project-card" :to="{path: `/projects/${data.projectId}`}">
    <v-card
      :raised="hovered"
      @mouseover="hovered = true"
      @mouseout="hovered = false"
    >
      <v-row v-if="data">
        <v-col
          md="9"
        >
          <v-card-title class="project-card__title">
            {{ data.name }}
          </v-card-title>
        </v-col>
        <v-col
          md="3"
        >
          <v-btn
            icon
          >
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </v-col>
      </v-row>
      <v-card-subtitle class="project-card__status">
        {{ data.state.name }}
      </v-card-subtitle>
      <v-progress-linear :value="countProgressState" />
    </v-card>
  </nuxt-link>
</template>

<script>
export default {
  name: 'ProjectCard',
  props: {
    data: {
      type: Object,
      default: null
    }
  },
  data () {
    return {
      progress: null,
      progressState: 0,
      hovered: false
    }
  },
  computed: {
    countProgressState () {
      if (!this.progress) { return 0 }
      if (this.progress.done === 0 && this.progress.inProgress === 0) { return 0 }
      return (this.progress.done / (this.progress.inProgress + this.progress.done)) * 100
    }
  },
  created () {
    this.$axios.get('/projects/' + this.data.projectId + '/progress')
      .then((response) => {
        this.progress = response.data
      })
  }
}
</script>

<style scoped lang="scss">
  .project-card {
    min-width: 200px;
    display: block;
    text-decoration: none;
  }
</style>
