import { createStore } from 'vuex'

export default createStore({
    state: {
      currentpage:"",
    },
    mutations: {
      updatecurrentpage(state, currentpage){
        state.currentpage = currentpage
      }
    },
    actions: {
    },
    modules: {
    }
  })