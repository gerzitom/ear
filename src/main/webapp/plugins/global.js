/*
    Author: Tomas Gerzicak
    Version: 1.0
    Description:
*/
import Vue from 'vue'

export const scripts = {
  methods: {
    getState (stateId) {
      const states = JSON.parse(localStorage.getItem('states'))
      if (!states) { return null }
      let foundState = null
      states.forEach((state) => {
        if (state.stateId === stateId) { foundState = state }
      })
      return foundState
    },
    /**
     * Gets image path and parse it to full url
     * @param imagePath
     * @returns {string|null} image url or null
     */
    parseImagePath (imagePath) {
      return imagePath !== null ? this.$const.BACKEND_BASE_URL + imagePath : null
    }
  },
  mounted () {
  }
}
Vue.mixin(scripts)
