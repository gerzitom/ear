<template>
  <section>
    <v-btn @click="exportData">Export</v-btn>
    <div class="mindmap" id="map">
    </div>
  </section>
</template>

<script>

import MindElixir from 'mind-elixir'
export default {
  name: 'mindmap',
  props: {
    tasks: {
      type: Object,
      default: null
    }
  },
  data () {
    return {
      mind: null
    }
  },
  computed: {
    formatedData () {
      const data = [
        {
          topic: 'Task 1',
          id: 1,
          style: { fontSize: '32', color: '#3298db', background: '#ecf0f1' },
          parent: null,
          tags: ['Tag']
        },
        {
          topic: 'Task 2',
          id: 2,
          style: { fontSize: '32', color: '#3298db', background: '#ecf0f1' },
          parent: 1,
          tags: ['Tag']
        },
        {
          topic: 'Task 3',
          id: 3,
          style: { fontSize: '32', color: '#3298db', background: '#ecf0f1' },
          parent: 1,
          tags: ['Tag']
        }
      ]
      return data
    }
  },
  mounted () {
    this.mind = new MindElixir({
      el: '#map',
      direction: MindElixir.LEFT,
      // create new map data
      // data: MindElixir.new('new topic'),
      data: {
        linkData: {},
        nodeData: {
          id: 'root',
          topic: 'Netflix PWA App',
          children: [
            {
              topic: 'Task 1',
              id: '607016b904018a0c',
              style: { fontSize: '12', color: '#3298db', background: '#ecf0f1' },
              parent: null,
              tags: ['Tag']
            },
            {
              topic: 'Task 2',
              id: '607016b904018a0d',
              style: { fontSize: '12', color: '#3298db', background: '#ecf0f1' },
              parent: '607016b904018a0c',
              tags: ['Tag']
            },
            {
              topic: 'Task 3',
              id: '607016b904018a0e',
              style: { fontSize: '12', color: '#3298db', background: '#ecf0f1' },
              parent: '607016b904018a0c',
              tags: ['Tag']
            }
          ],
          expanded: true
        }
      },
      // or set as data that is return from `.getAllData()`
      draggable: true, // default true
      contextMenu: false, // default true
      toolBar: false, // default true
      nodeMenu: false, // default true
      keypress: true, // default true
      locale: 'en', // [zh_CN,zh_TW,en,ja] waiting for PRs
      overflowHidden: false, // default false
      primaryLinkStyle: 2, // [1,2] default 1
      contextMenuOption: {
        focus: true,
        link: true,
        extend: [
          {
            name: 'Node edit',
            onclick: () => {
              alert('extend menu')
            }
          }
        ]
      },
      allowUndo: false,
      before: {
        insertSibling (el, obj) {
          return true
        }
        // async addChild (el, obj) {
        //   // await sleep()
        //   return true
        // }
      }
    })
    this.mind.init()
  },
  methods: {
    exportData () {
      console.log(this.mind.getAllData())
    }
  }
}
</script>

<style scoped>
.mindmap{
  height: 500px;
  width: 1000px;
}
</style>
