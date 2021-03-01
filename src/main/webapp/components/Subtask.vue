<template>
  <div class="subtask">
    <nuxt-link class="h4 font-weight-bold text-decoration-none text--primary" :to="{path: `/project/${$route.params.id}/${data.id}`}">
      {{ data.name }}
    </nuxt-link>
    <ul>
      <li v-for="subtask in data.subtasks" :key="subtask.id" style="list-style-type: none">
        <subtask :data="subtask" />
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'Subtask',
  props: {
    data: {
      type: Object,
      default: null
    }
  },
  methods: {
  }
}
</script>

<style scoped>

</style>
