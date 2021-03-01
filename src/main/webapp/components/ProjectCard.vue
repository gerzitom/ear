<template>
  <nuxt-link class="project-card" :to="{path: `/project/${data.id}`}">
    <v-card
      :raised="hovered"
      class="px-3 pt-3"
      @mouseover="hovered = true"
      @mouseout="hovered = false"
    >
      <v-row
        v-if="data"
        justify="space-between"
      >
        <v-col
          md="8"
          class="ml-2"
        >
          <v-card-title class="project-card__title pa-0">
            {{ data.name }}
          </v-card-title>
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <project-users :users="data.users" :add-button="false" />
        </v-col>
      </v-row>
      <v-card-subtitle class="project-card__status">
        <!--        {{ data.state.name }}-->
      </v-card-subtitle>
    </v-card>
  </nuxt-link>
</template>

<script>
export default {
  name: 'ProjectCard',
  components: {
    ProjectUsers: () => import('@/components/project/ProjectUsers')
  },
  props: {
    data: {
      type: Object,
      default: null
    }
  },
  data () {
    return {
      progress: null,
      hovered: false
    }
  },
  computed: {
    // countProgressState () {
    //   if (!this.progress) { return 0 }
    //   if (this.progress.done === 0 && this.progress.inProgress === 0) { return 0 }
    //   return (this.progress.done / (this.progress.inProgress + this.progress.done)) * 100
    // }
  },
  created () {
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
