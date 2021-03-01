<template>
  <div class="child">
    <inner-content v-on="$listeners" />
  </div>
</template>

<script>
export default {
  name: 'TaskContent',
  components: {
    innerContent: () => import('@/components/task/TaskContentDrawer')
  }
}
</script>

<style scoped>

</style>
