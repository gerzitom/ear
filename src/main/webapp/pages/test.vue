<template>
  <div class="test">
    <v-navigation-drawer
      v-model="drawerOpened"
      width="800"
      fixed
      temporary
      right
      disable-route-watcher
    >
      <nuxt-link :to="{path: `/test/30`}" class="task__body" @click.native="handleLinkClick">
        HAHAHA
      </nuxt-link>
    </v-navigation-drawer>

    <h1>This is main page</h1>
    <nuxt-child />
  </div>
</template>

<script>
export default {
  name: 'Test',
  data () {
    return {
      drawerOpened: true
    }
  },
  methods: {
    handleLinkClick (ev) {
      ev.preventDefault()
      this.drawerOpened = true
    }

  }
}
</script>

<style scoped>

</style>
