<template>
  <v-card>
    <template v-if="data">
      <v-row>
        <v-col
          cols="8"
        >
          <v-card-subtitle class="mb-0 pb-0">
            Active sprint
          </v-card-subtitle>
          <v-card-text>
            <h2 class="h1">
              {{ data.name }}
            </h2>
          </v-card-text>
          <v-card-text class="mb-0 pb-0">
            {{ data.description }}
          </v-card-text>
        </v-col>
        <v-col
          cols="4"
        >
          <v-card-text class="pt-0">
            <p class="h3 mb-0 pb-0">
              Deadline
            </p>
            <deadline :isostring="data.deadline" />
          </v-card-text>
        </v-col>
      </v-row>
      <v-divider />
      <v-card-text>
        <h2>Tasks</h2>
        <tasks :tasks="data.tasks" />
      </v-card-text>
    </template>
  </v-card>
</template>

<script>
export default {
  name: 'Sprint',
  components: {
    Deadline: () => import('@/components/Deadline'),
    Tasks: () => import('@/components/project/SprintTasks')
  },
  props: {
    data: {
      type: Object,
      default: null
    }
  }
}
</script>

<style scoped>

</style>
