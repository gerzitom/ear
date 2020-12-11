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
    }
  },
  mounted () {
    // if (!localStorage.getItem('states')) {
    //   this.$axios.get('/states')
    //     .then((response) => {
    //       localStorage.setItem('states', JSON.stringify(response.data))
    //     })
    // }
  }
}
Vue.mixin(scripts)
