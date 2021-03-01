export default (ctx, inject) => {
  const constants = {
    BASE_URL: 'http://localhost:8080',
    BACKEND_BASE_URL: 'http://localhost:8080/rest'
  }
  inject('const', constants)
}
