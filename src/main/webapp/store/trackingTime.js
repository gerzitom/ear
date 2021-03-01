export const state = () => ({
  trackingTime: null
})

export const mutations = {
  add (state, trackingTime) {
    state.trackingTime = trackingTime
  },
  get () {
    return state.trackingTime
  }
}
