import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import SocketService from './utils/socket_service'
//引用插件
import "lib-flexible/flexible.js"

//对服务端进行websocket连接
SocketService.Instance.connect()

createApp(App).use(store).use(router).mount('#app')
