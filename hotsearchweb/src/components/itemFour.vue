<template>
    <div>
        <h2>热搜关键词</h2>
        <div class="chart" id="Chart4">
        </div>
    </div>
</template>

<script>
import {inject, onMounted, onUnmounted} from 'vue'
import "echarts-wordcloud"

export default{
    setup(){
        let $socket = inject("Instance")
        let $echarts = inject("echarts")
        let myChart = null
        let keywords = []

        function getData(ret){
            if(ret.world){
                console.log("WorldCloudRet: ", ret)
                for(let i = 0; i < ret.world.length; i ++){
                    keywords.push({
                        name:ret.world[i].word,
                        value: ret.world[i].frequency
                    })
                }
                console.log("keywords: ", keywords)
                updateChart()
            }
        }

        let  wordCloudColor = [
                '#1890FF',
                '#2FC25B',
                '#41D9C7',
                '#FACC14',
                '#9AE65C'
              ]

        const updateChart = () => myChart.setOption ({
                series: [{
                type: 'wordCloud',
                shape: 'circle',
                keepAspect: false,
                //maskImage: maskImage,
                left: 'center',
                top: 'center',
                width: '70%',
                height: '80%',
                right: null,
                bottom: null,
                sizeRange: [12, 60],
                rotationRange: [-90, 90],
                rotationStep: 45,
                gridSize: 8,
                drawOutOfBound: false,
                shrinkToFit: false,
                layoutAnimation: true,
                textStyle: {
                    fontFamily: 'sans-serif',
                    fontWeight: 'bold',
                    color: function () {
                        return 'rgb(' + [
                            Math.round(Math.random() * 160),
                            Math.round(Math.random() * 160),
                            Math.round(Math.random() * 160)
                        ].join(',') + ')';
                    }
                },
                emphasis: {
                    focus: 'self',

                    textStyle: {
                        textShadowBlur: 10,
                        textShadowColor: '#333'
                    }
                },
                data:keywords,
                }]
        })

        
        onMounted(()=>{
          $socket.registerCallBack('trendDataWorld', getData)
          myChart = $echarts.init(document.getElementById("Chart4")) 
        
        })

        onUnmounted(() =>{
            this.$socket.unRegisterCallBack("trendDataWorld")
        })


        return{
            getData,
            updateChart,
            wordCloudColor,
            keywords,
            myChart,
  
        }
    }
}
</script>

<style>
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