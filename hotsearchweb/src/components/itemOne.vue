<template>
    <div>
        <h2>实时热搜榜</h2>
        <div class="chart" id="Chart1">
        </div>
    </div>
</template>

<script>
import {inject, onMounted, reactive, onUnmounted} from 'vue';

export default{
    setup(){
        let $echarts = inject("echarts")
        let $socket = inject("Instance")

        let xdata = reactive([])
        let ydata = reactive([])
        let myChart = null
    
        //ret是服务端发送给客户端的图表的数据
        function getData(ret){
            if(ret.news){
                console.log("newsRet: ", ret)
                xdata = ret.news.map(v=>v.name)
                ydata = ret.news.map(v=>v.count)
                updateChart()
            }
        }

        const updateChart = () => myChart.setOption({
                grid:{
                    top:"3%",
                    left:"1%",
                    right:"6%",
                    bottom:"3%",
                    containLabel:true
                },
                xAxis:{
                    type:"value",
                    axisLabel: {
                            textStyle: {
                              show:true,
                              color: "#fff",
                            },                           
                        }

                },
                yAxis:{
                    type:"category",
                    data: xdata,
                    inverse: true, //倒序
                    axisLabel: {
                            textStyle: {
                              show:true,
                              color: "#fff",
                            },                           
                        }
                },
                series:[
                    {
                        data: ydata,
                        type:"bar",
                        itemStyle:{
                            normal:{
                                barBorderRadius:[0,20, 20, 0],
                                color:new $echarts.graphic.LinearGradient(0, 0, 1, 0,
                                [
                                    {
                                        offset:0,
                                        color:"#005eaa"    
                                    },
                                    {
                                        offset:0.5,
                                        color:"#339ca8"
                                    },
                                    {
                                        offset:1,
                                        color:"#cda819"
                                    }
                                ]
                                )
                            }
                        }
                    }
                ]
            
        })

        
        onMounted(()=>{
          $socket.registerCallBack('trendDataNews', getData)
          myChart = $echarts.init(document.getElementById("Chart1"))
          
        })

        onUnmounted(() =>{
            this.$socket.unRegisterCallBack("trendDataNews")
        })


        return{
            getData,
            updateChart,
            xdata,
            ydata,
            myChart,
        }
    }
}
</script>

<style scoped>
.chart{
    height: 4rem;
}

h2{
    height: .6rem;
    color: #fff;
    line-height: 0.6rem;
    font-size: 0.25rem;
    text-align:center;
}

</style>