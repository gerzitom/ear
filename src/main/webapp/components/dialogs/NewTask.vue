<template>
  <v-card class="new-project">
    <v-card-title>
      New task
    </v-card-title>
    <v-card-text>
      <v-form
        id="new-task"
        ref="form"
        onsubmit="return false"
        @submit="formSubmit"
      >
        <v-container>
          <v-row>
            <v-col md="12">
              <v-text-field
                v-model="name"
                :error-messages="nameErrors"
                outlined
                label="Task name"
                required
                @input="$v.name.$touch()"
                @blur="$v.name.$touch()"
              />
            </v-col>
          </v-row>
          <v-row>
            <v-col md="12">
              <v-menu
                content-class="new-task__datepicker-menu"
                :offset-y="true"
              >
                <template v-slot:activator="{on}">
                  <v-text-field outlined :value="deadline" label="Deadline" readonly v-on="on">
                    <template v-slot:append>
                      <v-icon>
                        mdi-calendar-month
                      </v-icon>
                    </template>
                  </v-text-field>
                </template>
                <v-date-picker
                  v-model="deadline"
                  :min="datepickerMin"
                />
              </v-menu>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-select
                v-model="selectedValues"
                :items="allUsers"
                item-value="id"
                item-text="name"
                multiple
                chips
                @change="handleSelectChange"
              />
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-textarea
                v-model="description"
                outlined
                label="Popis"
              />
            </v-col>
          </v-row>
        </v-container>
        <v-btn
          type="submit"
          form="new-task"
          :loading="submiting"
          :disabled="submiting"
          @click="formSubmit"
        >
          Vytvořit
        </v-btn>
        <v-btn
          @click="$emit('closed')"
        >
          Zavřít
        </v-btn>
      </v-form>
    </v-card-text>
    <v-snackbar
      v-model="snackbarOpened"
      :timeout="snackbarTimeout"
    >
      Task created
    </v-snackbar>
  </v-card>
</template>

<script>

import { validationMixin } from 'vuelidate'
import {
  required
} from 'vuelidate/src/validators'
export default {
  name: 'NewTask',
  mixins: [validationMixin],
  props: {
    parentTask: {
      type: Number,
      default: null
    }
  },
  async fetch () {
    const users = await this.$repositories.users.getAll()
    for (const userId in users) {
      const ID = parseInt(userId)
      const user = users[ID]
      this.allUsers.push(user)
    }
    this.selectedValues.push(this.$auth.user.id)
  },
  data () {
    return {
      submiting: false,
      name: '',
      description: '',
      priority: 1,
      nameUnique: true,
      snackbarOpened: false,
      snackbarTimeout: 3000,
      deadline: null,
      datepickerMin: this.$moment().format('YYYY-MM-DD'),
      allUsers: [],
      selectedValues: []
    }
  },
  validations: {
    name: { required }
  },
  computed: {
    nameErrors () {
      const errors = []
      if (!this.nameUnique) { errors.push('Task with this name already exists') }
      if (!this.$v.name.$dirty) { return errors }
      !this.$v.name.required && errors.push('Name is required.')
      return errors
    }
  },
  methods: {
    handleSelectChange (newValues) {
    },
    formSubmit (ev) {
      this.nameUnique = true
      ev.preventDefault()
      this.$v.$touch()
      if (!this.$v.$invalid) {
        this.submiting = true
        const data = {
          name: this.name,
          state: 'IN_PROGRESS',
          description: this.description,
          projectId: this.$route.params.id,
          deadline: this.deadline
        }
        if (this.parentTask) {
          this.$repositories.tasks.saveSubtask(this.parentTask, data)
            .then((result) => {
              data.id = result
              data.subtasks = []
              this.submiting = false
              this.snackbarOpened = true
              this.$emit('success', data)
              this.$nextTick(() => { this.$v.$reset() })
              this.clearForm()
            })
            .catch((reason) => {
              this.submiting = false
              this.nameUnique = false
              this.$v.$touch()
            })
        } else {
          this.$repositories.tasks.save(data)
            .then((taskId) => {
              data.id = taskId
              data.subtasks = []
              this.addUsers(this.selectedValues, taskId)
              this.submiting = false
              this.snackbarOpened = true
              this.$emit('success', data)
              this.$nextTick(() => { this.$v.$reset() })
              this.clearForm()
            })
            .catch((reason) => {
              this.submiting = false
              this.nameUnique = false
              this.$v.$touch()
            })
        }
      }
    },
    async addUsers (users, taskId) {
      for (const userId of users) {
        const dto = {
          taskId,
          userId
        }
        await this.$repositories.tasks.addUser(dto)
      }
    },
    clearForm () {
      this.name = ''
      this.description = ''
      this.deadline = null
    }
  }
}
</script>

<style scoped lang="scss">
  .new-task{
    &__datepicker-menu{
      box-shadow: none;
    }
  }
</style>
