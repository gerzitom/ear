import ProjectRepository from '~/api/projectRepository'
import TasksRepository from '~/api/tasksRepository'
import TrackedTimeRepository from '~/api/trackedTimeRepository'
import UserRepository from '@/api/userRepository'

export default (ctx, inject) => {
  const repositories = {
    tasks: new TasksRepository(ctx.$axios),
    projects: new ProjectRepository(ctx.$axios),
    trackedTime: new TrackedTimeRepository(ctx.$axios, ctx.$moment),
    users: new UserRepository(ctx.$axios)
  }

  inject('repositories', repositories)
}
