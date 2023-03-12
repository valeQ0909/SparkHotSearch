<template>
    <div>
        <h2>搜索用户年龄分布</h2>
        <div class="chart" id="Chart3">
        </div>
    </div>
</template>

<script>
import {inject, onMounted,  onUnmounted} from 'vue'
export default{
    setup(){
        let $echarts = inject("echarts")
        let $socket = inject("Instance")
        let myChart = null
        let ageData = [{value:0, name:"20岁以下"},
                       {value:0, name:"20-29岁"},
                       {value:0, name:"30-39岁"},
                       {value:0, name:"40-49岁"},
                       {value:0, name:"50岁以上"}]

        //ret是服务端发送给客户端的图表的数据
        function getData(ret){    
            if(ret.age){
                console.log("ageRet: ", ret)
                ageData = [{value:0, name:"20岁以下"},
                           {value:0, name:"20-29岁"},
                           {value:0, name:"30-39岁"},
                           {value:0, name:"40-49岁"},
                           {value:0, name:"50岁以上"}]

                let CountAge = 0
                let IntAge = 0
                for(let i = 0; i < ret.age.length; i ++){
                    CountAge = parseInt(ret.age[i]["count"]) 
                    IntAge = parseInt(ret.age[i]["age"])
                    if(IntAge < 20){  //"20岁以下"
                        ageData[0]["value"] += CountAge
                    } else if(IntAge >= 20 && IntAge <= 44){ //"20-29岁"
                        ageData[1]["value"] += CountAge
                    } else if(IntAge >= 45 && IntAge <= 64){ // "30-39岁"
                        ageData[2]["value"] += CountAge 
                    } else if(IntAge >= 65 && IntAge <= 69){ // "40-49岁"
                        ageData[3]["value"] += CountAge
                    } else {  // "50岁以上"
                        ageData[4]["value"] += CountAge
                    }
                }
                updateChart()
            }  

        }

        const updateChart = () => myChart.setOption ({
            tooltip: {
                trigger: "item",
                formatter: "{a} <br/>{b}: {c} ({d}%)",
                position: function(p) {
                //其中p为当前鼠标的位置
                return [p[0] + 10, p[1] - 10];
                }
            },
            legend: {
                top: "5%",
                itemWidth: 10,
                itemHeight: 10,
                data: ["20岁以下", "20-29岁", "30-39岁", "40-49岁", "50岁以上"],
                textStyle: {
                    color: "rgba(255,255,255,.7)",
                    fontSize: "12"
                }
            },

            series: [
                {
                name: "年龄分布",
                type: "pie",
                center: ["50%", "50%"],
                radius: ["40%", "60%"],
                color: [
                    "rgba(182,159,226,.9",
                    "rgba(252,85,49,.9)",
                    "rgba(4,122,238,.9)",
                    "rgba(250,200,88,.9)",
                    "#06a0ab",
                    "#06b4ab",
                    "#06c8ab",
                    "#06dcab",
                    "#06f0ab"
                ],
                label : {
                    normal: {
                        show: true,
                        //position: 'inner', // 数值显示在内部
                        formatter: '{d}%', // 格式化数值百分比输出
                        textStyle : {
                            fontWeight : 'normal',
                            fontSize : 13
                        }
                    },
                },
                //labelLine: { show: false },
                data: ageData,

                }
            ]


            })

        onMounted(()=>{
          $socket.registerCallBack('trendDataAge', getData) 
          myChart = $echarts.init(document.getElementById("Chart3")) 
        })

        onUnmounted(() =>{
            this.$socket.unRegisterCallBack("trendDataAge")
        })


        return{
            myChart,
            ageData,
            updateChart,
        }
    }
}
</script>

<style>

</style>