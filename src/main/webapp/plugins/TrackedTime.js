/*
    Author: Tomas Gerzicak
    Version: 1.0
    Description:
*/
// import axios from '../.nuxt/axios'
export class TrackedTime {
  #start;
  #end;

  constructor (start, end) {
    this.#start = start
    this.#end = end
    this._start = start
    this._end = end
    // axios.get()
  }

  get start () {
    return this._start
  }

  set start (value) {
    this._start = value
  }

  get end () {
    return this._end
  }

  set end (value) {
    this._end = value
  }
}
