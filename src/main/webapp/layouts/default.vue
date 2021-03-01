<template>
  <v-app>
    <v-navigation-drawer
      v-model="drawer"
      :mini-variant="miniVariant"
      :clipped="clipped"
      fixed
      app
    >
      <v-list>
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          :to="item.to"
          router
          exact
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title v-text="item.title" />
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-app-bar
      :clipped-left="clipped"
      fixed
      app
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-btn
        icon
        @click.stop="miniVariant = !miniVariant"
      >
        <v-icon>mdi-{{ `chevron-${miniVariant ? 'right' : 'left'}` }}</v-icon>
      </v-btn>
      <v-btn
        icon
        @click.stop="clipped = !clipped"
      >
        <v-icon>mdi-application</v-icon>
      </v-btn>
      <v-btn
        icon
        @click.stop="fixed = !fixed"
      >
        <v-icon>mdi-minus</v-icon>
      </v-btn>
      <v-toolbar-title v-text="title" />
      <v-spacer />
      <user-avatar />
    </v-app-bar>
    <v-main>
      <nuxt />
    </v-main>
    <v-footer
      :fixed="fixed"
      app
    >
      <span>&copy; {{ new Date().getFullYear() }}</span>
      <v-spacer />
      <a :href="$const.BASE_URL + '/swagger-ui.html'">REST API documentation</a>
    </v-footer>
    <v-snackbar
      v-model="noConnection"
      :timeout="-1"
      color="error"
    >
      <v-icon>mdi-error</v-icon>
      No backend connection
    </v-snackbar>
    <v-snackbar
      v-model="appError"
      color="error"
    >
      {{ appErrorMessage }}
    </v-snackbar>
  </v-app>
</template>

<script>
export default {
  components: {
    userAvatar: () => import('~/components/UserAvatar.vue')
  },
  data () {
    return {
      clipped: false,
      drawer: false,
      fixed: false,
      items: [
        {
          icon: 'mdi-apps',
          title: 'Dashboard',
          to: '/'
        },
        {
          icon: 'mdi-comment-account-outline',
          title: 'Moje úkoly',
          to: '/my-tasks'
        }
      ],
      miniVariant: false,
      right: true,
      rightDrawer: false,
      title: 'Task manager',
      noConnection: false,
      noConnectionMessage: '',
      appError: false,
      appErrorMessage: ''
    }
  },
  created () {
    this.$nuxt.$on('error', (error) => {
      this.noConnection = true
      this.noConnectionMessage = error.response.data.message
    })
    this.$nuxt.$on('app_error', (error) => {
      this.appError = true
      this.appErrorMessage = error.response.data.message
    })
  }
}
</script>
