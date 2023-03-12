<template>
    <div>
        <h2>搜索用户性别比例</h2>
        <div class="chart" id="twoChart">
        </div>
    </div>
</template>

<script>
import {inject, onMounted, reactive, onUnmounted} from 'vue'

export default{
    setup(){
        let $echarts = inject("echarts")
        let $socket = inject("Instance")

        let myChart = null
        let sexData = reactive([])

        function getData(ret){
            if(ret.sex){
                console.log("sexRet: ", ret)
                sexData = ret.sex
                updateChart()
            }

        }

        const updateChart = () => myChart.setOption({
                    title: {
                        text: '',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left'
                    },
                    series: [
                        {
                        name: '',
                        type: 'pie',
                        radius: '50%',
                        label : {
                            normal: {
                                show: true,
                                //position: 'inner', // 数值显示在内部
                                formatter: '{d}%', // 格式化数值百分比输出
                                textStyle : {
                                    fontWeight : 'normal',
                                    fontSize : 15
                                }
                            },
                        },
                        data: sexData,
                        emphasis: {
                            itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                        }
                    ]

            })

        

        onMounted(()=>{
          $socket.registerCallBack('trendDataSex', getData)   
          myChart = $echarts.init(document.getElementById("twoChart")) 
            
        })

        onUnmounted(() =>{
            this.$socket.unRegisterCallBack("trendDataSex")
        })


        return{
            myChart,
            sexData,
            getData,

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