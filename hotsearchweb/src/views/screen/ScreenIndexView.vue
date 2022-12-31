<template>
    <div>
        <!--大容器-->
        <section class="container">
            <!--左容器-->
            <section class="itemLeft">
                <ItemPage>
                    <ItemOne/>
                </ItemPage>
                <ItemPage>
                    <ItemTwo/>
                </ItemPage>
            </section>
            <!--中容器-->
            <section class="itemCenter">
                <MapPage/>
            </section>
            <!--右容器-->
            <section class="itemRight">
                <ItemPage>
                    <ItemThree/>
                </ItemPage>
                <ItemPage>
                    <ItemFour/>
                </ItemPage>
            </section>

        </section>
    </div>
</template>
  
<script>
import ItemPage from '../../components/itemPage.vue';
import ItemOne from '../../components/itemOne.vue';
import ItemTwo from '../../components/itemTwo.vue';
import ItemThree from '../../components/itemThree.vue';
import ItemFour from '../../components/itemFour.vue';
import MapPage from '../../components/mapPage.vue';

// import {inject} from 'vue'
export default {
    components:{
        ItemPage,
        ItemOne,
        ItemTwo,
        ItemThree,
        ItemFour,
        MapPage,
    },

    setup(){
        // let $echarts = inject("echarts")
        // let $http = inject("axios")
    }

}
  
</script>
  
<style scoped>
.container{
    min-width: 1200px;
    max-width: 2048px;
    margin: 0 auto;
    padding: .125rem .125rem;
    display: flex;
}
.container .itemLeft{
    flex: 3;
}

.container .itemCenter{
    flex: 5;
    height: 9rpx;
    border: 1px solid blue;
    padding: 0.125rem;
    margin: .25rem;
}

.container .itemRight{
    flex: 3;
}

</style>