<template>
  <div class="user-info">
    {{ data }}
  </div>
</template>

<script>
export default {
  name: 'Id',
  async fetch () {
    this.data = await this.$repositories.users.get(this.$route.params.id)
  },
  data () {
    return {
      data: null
    }
  }
}

</script>

<style scoped>

</style>
