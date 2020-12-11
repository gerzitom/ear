<template>
  <v-btn
    icon
    @click="handleClick"
    @mouseover="hovered = true"
    @mouseout="hovered = false"
  >
    <v-icon v-show="!hovered && !completedState || completedState && hovered">
      mdi-checkbox-blank-circle-outline
    </v-icon>
    <v-icon v-show="hovered && !completedState || completedState && !hovered">
      mdi-checkbox-marked-circle-outline
    </v-icon>
  </v-btn>
</template>

<script>
export default {
  name: 'TaskButton',
  props: {
    completed: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      hovered: false,
      completedState: this.completed
    }
  },
  methods: {
    handleClick () {
      if (this.completedState) {
        this.$emit('uncompleted')
        this.completedState = false
      } else {
        this.$emit('completed')
        this.completedState = true
      }
    }
  }
}
</script>

<style scoped lang="scss">

</style>
