<template>
    <div class="map" id="map">

    </div>
</template>

<script>
import axios from 'axios';
import {onMounted, reactive, inject, onUnmounted} from 'vue'

export default{
    setup(){
        let $echarts = inject("echarts")
        let $socket = inject("Instance")
        let myChart = null

        let mapData = reactive({});
        let provinceDate = []


        async function getMapdata(){
            mapData = await axios.get("http://localhost:8080/map/china.json")
            console.log("map: ", mapData)
        }


        function getData(ret){
            if(ret.province){
                console.log("ProvinceRet: ", ret)
                provinceDate = ret.province
                updateChart()
            }
        }

        const updateChart = () => myChart.setOption({
                geo:{
                    map:"china",
                    itemStyle:{
                            areaColor:"#0099ff",
                            borderColor:"#00ffff",
                            shadowColor:"rgba(230,130,70,0.5)",
                            shadowBlur:30,
                            emphasis:{
                                focus:"self"
                            }
                        }
                },
                tooltip:{
                    trigger:"item"
                },
                title:{
                    text:"搜索用户省份分布",
                    left:"38%",
                    textStyle:{
                        color:"#fff",
                        fontSize:20,
                        textShadowBlur:10,
                        textShadowColor:"#33fffff"
                    }
                },
                visualMap:{
                    type:"continuous",
                    min:0,
                    max:100,
                    calculable:true,
                    inRange:{
                        color: ["#50a3ba", "#eac736", "#d94e5d"]
                    },
                    textStyle:{
                        color:"#fff"
                    }
                },
                series:{
                    name: '数据',  
                    type: 'map',  
                    mapType: 'china',   
                    roam: true,  
                    label: {  
                        normal: {  
                            show: true  //省份名称  
                        },  
                        emphasis: {  
                            show: false  
                        }  
                    },  
                    data: provinceDate  

                }
        })



        // onMounted(()=>{
        //     getState().then(()=>{
        //         console.log("map", mapData)
        //         $echarts.registerMap("china", mapData.data)

        //         let myChart = $echarts.init(document.getElementById("map"))
                
        //         myChart.setOption({
        //             geo:{
        //                 map:"china",
        //                 itemStyle:{
        //                     areaColor:"#0099ff",
        //                     borderColor:"#00ffff",
        //                     shadowColor:"rgba(230,130,70,0.5)",
        //                     shadowBlur:30,
        //                     emphasis:{
        //                         focus:"self"
        //                     }
        //                 }
        //             },
        //             tooltip:{
        //                 trigger:"item"
        //             },
        //             title:{
        //                 text:"搜索用户省份分布",
        //                 left:"38%",
        //                 textStyle:{
        //                     color:"#fff",
        //                     fontSize:20,
        //                     textShadowBlur:10,
        //                     textShadowColor:"#33fffff"
        //                 }
        //             },
        //             visualMap:{
        //                 type:"continuous",
        //                 min:0,
        //                 max:500,
        //                 calculable:true,
        //                 inRange:{
        //                     color: ["#50a3ba", "#eac736", "#d94e5d"]
        //                 },
        //                 textStyle:{
        //                     color:"#fff"
        //                 }
        //             },
        //             series:{
        //                 name: '数据',  
        //                 type: 'map',  
        //                 mapType: 'china',   
        //                 roam: true,  
        //                 label: {  
        //                     normal: {  
        //                         show: true  //省份名称  
        //                     },  
        //                     emphasis: {  
        //                         show: false  
        //                     }  
        //                 },  
        //                 data: provinceDate  

        //             }

        //         })

        //     })
        // })

        onMounted(()=>{
          getMapdata().then(()=>{
            $echarts.registerMap("china", mapData.data)
            $socket.registerCallBack('trendDataProvince', getData)
            myChart = $echarts.init(document.getElementById("map"))
          })
        })

        onUnmounted(() =>{
            this.$socket.unRegisterCallBack("trendDataProvince")
        })

        return{
            getData,
            getMapdata, 
            updateChart,
            mapData
        }
    }
}


</script>

<style>
.map{
    width: 100%;
    height: 100%;
}
</style>