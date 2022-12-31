<template>
    <NavBar></NavBar>
    <router-view></router-view>
</template>

<script>
import {provide} from "vue"
import * as echarts from "echarts"
import axios from "axios"
import SocketService from "./utils/socket_service"

import NavBar from "../src/components/NavBar.vue"

//请求基准路径的配置
axios.defaults.baseURL = "http://127.0.0.1:3000/"
export default {
  components: {
    NavBar
  },
  setup() {
    provide("echarts", echarts)
    provide("axios", axios)
    provide("Instance", SocketService.Instance)
  }
  }

</script>

<style>
body{
  background-image: url("../src/assets/images/background.jpg");
  background-size: cover;
  width: 100%;
}

*{
  margin:0px;
  padding:0px;
  box-sizing: border-box;
}
</style>