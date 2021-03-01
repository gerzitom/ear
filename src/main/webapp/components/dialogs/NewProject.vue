<template>
  <v-card class="new-project">
    <v-card-title>
      <span class="display-1">New project</span>
    </v-card-title>
    <v-card-text>
      <v-container>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field ref="name" v-model="name" label="Project name" required />
          </v-col>
          <v-col cols="12" md="6">
            <v-menu>
              <template v-slot:activator="{on}">
                <v-text-field :value="datePicker" label="Deadline" readonly v-on="on" />
              </template>
              <v-date-picker v-model="datePicker" />
            </v-menu>
          </v-col>
        </v-row>
        <v-btn color="blue darken-1" text @click="$emit('close')">
          Close
        </v-btn>
        <v-btn color="blue white--text" raised @click="saveNewProject">
          Save
        </v-btn>
      </v-container>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: 'NewProject',
  data () {
    return {
      datePicker: null,
      name: null
    }
  },
  methods: {
    saveNewProject () {
      const data = {
        name: this.name,
        deadline: this.datePicker
      }
      this.$repositories.projects.create(data)
        .then((response) => {
          data.id = response.data
          data.users = [this.$auth.user.id]
          this.$emit('newproject', data)
        })
      return null
    }
  }
}
</script>

<style scoped lang="scss">
  .new-project{
  }
</style>
