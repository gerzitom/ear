<template>
  <div>
    <v-menu>
      <template v-slot:activator="{on}">
        <v-text-field
          v-show="validDeadline"
          :value="deadline"
          readonly
          v-on="on"
        />
        <v-btn
          v-show="!validDeadline"
          small
          v-on="on"
        >
          Add deadline
        </v-btn>
      </template>
      <v-date-picker
        v-model="date"
        @change="handleDatePickerChange"
      />
    </v-menu>
  </div>
</template>

<script>
export default {
  name: 'Deadline',
  props: {
    isostring: {
      type: String,
      default: null
    }
  },
  data () {
    return {
      date: null,
      validDeadline: false,
      menu: {
        type: Boolean,
        default: false
      }
    }
  },
  computed: {
    deadline () {
      if (this.date != null) {
        const moment = this.$moment(this.date)
        return moment.format('DD. MM. YYYY')
      } else {
        return ''
      }
    },
    isCriticalDeadlineDiffFromNow () {
      if (this.isostring) {
        const subtract = this.$moment(this.isostring).diff(this.$moment(), 'days')
        return subtract < 1
      }
      return ''
    }
  },
  mounted () {
    this.date = this.datepicker()
  },
  methods: {
    datepicker () {
      if (this.isostring != null) {
        this.validDeadline = true
        const moment = this.$moment(this.isostring)
        return moment.format('YYYY-MM-DD')
      } else {
        return ''
      }
    },
    handleDatePickerChange () {
      if (this.date !== null) {
        this.validDeadline = true
        this.$emit('updated', this.date)
        // const task = new Task(this.rou)
      } else {
        this.validDeadline = false
      }
    }
  }
}
</script>

<style scoped lang="scss">

</style>
