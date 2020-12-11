<template>
  <div class="comments">
    <div class="task-content__comments mt-10">
      <h3 class="headline">
        Komentáře
      </h3>
      <v-form
        class="mt-4"
        onsubmit="return false"
        @submit="newComment"
      >
        <v-textarea
          v-model="commentText"
          outlined
          required
          label="Komentář"
          :error-messages="nameErrors"
          @input="$v.commentText.$touch()"
          @keydown="handleShortcut"
        />
        <v-btn
          type="submit"
          small
        >
          Odeslat
        </v-btn>
      </v-form>
      <v-list v-if="!comments && comments.length > 0">
        <v-skeleton-loader type="list-item-avatar" />
        <v-skeleton-loader max-width="80%" type="list-item-avatar" />
        <v-skeleton-loader max-width="90%" type="list-item-avatar" />
      </v-list>
      <v-list>
        <v-list-item v-for="comment in comments" :key="comment.comment_id">
          <v-list-item-avatar>
            <v-img src="https://randomuser.me/api/portraits/women/75.jpg" />
          </v-list-item-avatar>
          <v-list-item-content>
            <p class="overline">
              {{ comment.created.format("DD. MM. YYYY HH:mm") }}
            </p>
            <v-list-item-subtitle>
              {{ comment.text }}
            </v-list-item-subtitle>
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
  mixins: [validationMixin],
  props: {
    comments: {
      type: Array,
      default: null
    },
    taskId: {
      type: Number,
      default: null
    }
  },
  data () {
    return {
      commentText: null
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
    this.sortComments()
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
          taskId: this.taskId,
          created: createdTime.format('YYYY-MM-DDThh:mm:ssZZ'),
          userId: 1,
          text: this.commentText
        }
        this.$axios.post('/comments', data)
          .then((result) => {
            data.created = createdTime
            this.comments.push(data)
            this.commentText = ''
            this.$v.$reset()
            this.sortComments()
          })
      }
    },
    sortComments () {
      this.comments.forEach((comment) => {
        console.log(comment.created)
        comment.created = this.$moment(comment.created)
      })
      this.comments.sort(function (a, b) {
        return b.created - a.created
      })
    }
  }
}
</script>

<style scoped lang="scss">

</style>
