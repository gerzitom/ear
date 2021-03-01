<template>
  <div class="comments">
    <div class="task-content__comments mt-10">
      <h3 class="headline">
        Komentáře
      </h3>
      <v-form
        class="mt-4 mb-10"
        onsubmit="return false"
        @submit="newComment"
      >
        <v-textarea
          v-model="commentText"
          :auto-grow="true"
          rows="1"
          required
          label="Comment"
          :error-messages="nameErrors"
          @input="$v.commentText.$touch()"
          @keydown="handleShortcut"
        />
        <v-btn
          type="submit"
          small
        >
          Send
        </v-btn>
      </v-form>
      <v-list v-if="!loading">
        <p v-if="comments.length === 0">
          There are no comments yet
        </p>
        <div v-for="comment in comments" v-else :key="comment.id">
          <div class="d-flex pb-5">
            <avatar :user-id="comment.userId" />
            <div class="pl-3 flex-grow-1">
              <p class="my-0">
                {{ getUser(comment.userId).name }}
              </p>
              <p class="overline my-0">
                {{ formatTime(comment.created) }}
              </p>
              <p class="mt-3 mb-3">
                {{ comment.text }}
              </p>
              <v-divider />
            </div>
          </div>
        </div>
      </v-list>
      <v-list v-else>
        <v-list-item v-for="i in 4" :key="i">
          <v-list-item-avatar>
            <v-skeleton-loader type="avatar" />
          </v-list-item-avatar>
          <v-list-item-content>
            <p class="overline">
              <v-skeleton-loader type="text" max-width="10%" />
            </p>
            <v-skeleton-loader type="paragraph" max-width="80%" />
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </div>
  </div>
</template>

<script>
import { validationMixin } from 'vuelidate'
import {
  required
} from 'vuelidate/src/validators'
export default {
  name: 'Comments',
  components: {
    Avatar: () => import('@/components/AvatarImage')
  },
  mixins: [validationMixin],
  props: {
    taskId: {
      type: Number,
      default: null
    }
  },
  async fetch () {
    this.users = await this.$repositories.users.getAll()
    this.$repositories.tasks.getComments(this.taskId)
      .then((response) => {
        this.comments = response.data
      })
      .finally(() => {
        this.loading = false
      })
  },
  data () {
    return {
      commentText: null,
      comments: [],
      users: {},
      loading: true
    }
  },
  validations: {
    commentText: { required }
  },
  computed: {
    nameErrors () {
      const errors = []
      if (!this.$v.commentText.$dirty) { return errors }
      if (!this.$v.commentText.required) {
        errors.push('Text is required.')
      }
      return errors
    }
  },
  created () {
    this.$fetch()
  },
  methods: {
    handleShortcut (e) {
      if ((e.metaKey || e.ctrlKey) && e.keyCode === 13) {
        this.saveComment()
      }
    },
    newComment (ev) {
      ev.preventDefault()
      this.saveComment()
    },
    saveComment () {
      const createdTime = this.$moment()
      this.$v.$touch()
      if (!this.$v.$invalid) {
        const data = {
          created: createdTime.format('YYYY-MM-DD hh:mm:ss'),
          userId: this.$auth.user.id,
          text: this.commentText
        }
        this.$repositories.tasks.addComment(this.taskId, data)
          .then((result) => {
            data.created = createdTime
            this.comments.unshift(data)
            this.commentText = ''
            this.$v.$reset()
          })
          .catch((err) => {
            this.$nuxt.$emit('app_error', err)
          })
      }
    },
    formatTime (rawTime) {
      return this.$moment(rawTime).format('DD. MM. YYYY HH:mm')
    },
    getUser (userId) {
      return this.users[userId]
    }
  }
}
</script>

<style scoped lang="scss">

</style>
