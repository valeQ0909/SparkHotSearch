import { createRouter, createWebHistory } from 'vue-router'

import ScreenIndexView from "../views/screen/ScreenIndexView"
import NotFound from '../views/error/NotFound'

const routes = [
  {
    path:"/",
    name: 'home',
    redirect:"/screen/",
  },
  {
    path: "/screen/",
    name: 'screen_index',
    component: ScreenIndexView,
  },
  {
    path: "/404/",
    name: '404',
    component: NotFound,
  },
  {
    path:"/:catchAll(.*)",
    redirect: "/404/"
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})


export default router