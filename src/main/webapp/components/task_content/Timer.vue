<template>
  <div class="task-timer">
    <v-card
      rounded="pill"
      :loading="loading"
    >
      <v-row
        no-gutters
        align="center"
        justify="between"
        class="pa-1"
      >
        <v-col
          cols="auto"
        >
          <v-btn
            icon
            @click="handlePlayPauseClick"
          >
            <v-icon v-show="running">
              mdi-pause
            </v-icon>
            <v-icon v-show="!running">
              mdi-play
            </v-icon>
          </v-btn>
        </v-col>
        <v-col>
          <p class="task-timer__time text-center ma-0">
            {{ formatedTime }}
          </p>
        </v-col>
        <v-col
          cols="auto"
        >
          <v-btn
            icon
            :disabled="running || elapsedTime === 0"
            @click="stop"
          >
            <v-icon>mdi-upload</v-icon>
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
export default {
  name: 'Timer',
  components: {
  },
  props: {
    taskId: {
      type: Number,
      default: null
    }
  },
  data () {
    return {
      alreadyTracked: 0,
      elapsedTime: 0,
      timer: null,
      running: false,
      loading: false
    }
  },
  computed: {
    formatedTime () {
      const timeTotal = this.alreadyTracked + this.elapsedTime
      return this.$moment().startOf('day').add(timeTotal, 's').format('HH:mm:ss')
    }
  },
  mounted () {
    this.$axios.get(`/tasks/${this.taskId}/trackedtime`)
      .then((result) => {
        this.alreadyTracked = parseInt(result.data)
      })
  },
  methods: {
    handlePlayPauseClick () {
      if (this.running) {
        this.pause()
      } else {
        this.start()
      }
    },
    start () {
      this.running = true
      this.timer = setInterval(() => {
        this.elapsedTime += 1
      }, 1000)
    },
    stop () {
      this.pause()
      this.loading = true
      const data = {
        duration: this.elapsedTime,
        userId: 1
      }
      this.$axios.post(`/tasks/${this.taskId}/trackedtime`, data)
        .then((result) => {
          this.alreadyTracked += this.elapsedTime
          this.elapsedTime = 0
          this.loading = false
        })
    },
    pause () {
      this.running = false
      clearInterval(this.timer)
    }
  }
}
</script>

<style scoped lang="scss">

</style>
