<template>
  <div class="task-timer">
    <v-card
      :loading="loading"
    >
      <v-row
        no-gutters
        align="center"
        justify="space-between"
        class="pa-1"
      >
        <v-col
          cols="auto"
        >
          <p class="task-timer__time text-right ma-0 pl-3">
            {{ formatedTime }}
          </p>
        </v-col>
        <v-col
          cols="auto"
        >
          <v-btn
            icon
            @click="handlePlayPauseClick"
          >
            <v-icon v-show="running">
              mdi-stop
            </v-icon>
            <v-icon v-show="!running">
              mdi-play
            </v-icon>
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
    <p class="font-weight-bold mt-5 mb-1">
      Total tracked times
    </p>
    <tracked-times class="mt-2" :times="userTimes" />
  </div>
</template>

<script>
export default {
  name: 'Timer',
  components: {
    TrackedTimes: () => import('@/components/task_content/UserTrackedTimes')
  },
  props: {
    taskId: {
      type: Number,
      default: null
    }
  },
  async fetch () {
    this.userTimes = await this.$repositories.tasks.getTrackedTimes(this.taskId)
    const activeTracking = await this.$repositories.trackedTime.getUserActiveTracking(this.$auth.user.id)
    // active time tracking
    if (activeTracking) {
      if (!activeTracking.end) {
        this.trackedTimeId = activeTracking.id
        const start = this.$moment(activeTracking.start)
        const difference = this.$moment().diff(start, 'seconds')
        this.elapsedTime = difference
        this.startTimer()
      }
    }
  },
  data () {
    return {
      alreadyTracked: 0,
      elapsedTime: 0,
      timer: null,
      running: false,
      loading: false,
      trackedTimeId: null,
      userTimes: null
    }
  },
  computed: {
    formatedTime () {
      const timeTotal = this.alreadyTracked + this.elapsedTime
      return this.$moment().startOf('day').add(timeTotal, 's').format('HH:mm:ss')
    }
  },
  methods: {
    handlePlayPauseClick () {
      if (this.running) {
        this.stop()
      } else {
        this.start()
      }
    },
    start () {
      this.$repositories.trackedTime.startTracking(this.$auth.user.id, this.taskId)
        .then((trackedTimeId) => {
          this.trackedTimeId = trackedTimeId
          this.$store.commit('trackingTime/add', trackedTimeId)
          this.startTimer()
        })
    },
    startTimer () {
      this.running = true
      this.timer = setInterval(() => {
        this.elapsedTime += 1
      }, 1000)
    },
    stop () {
      this.loading = true
      this.$repositories.trackedTime.stopTracking(this.trackedTimeId, this.$auth.user.id, this.taskId)
        .then((response) => {
          this.userTimes[12] += this.elapsedTime
          this.elapsedTime = 0
          this.running = false
          clearInterval(this.timer)
          this.loading = false
          this.$repositories.tasks.getTrackedTimes(this.taskId)
            .then((userTimes) => {
              this.userTimes = userTimes
            })
        })
    }
  }
}
</script>

<style scoped lang="scss">

</style>
