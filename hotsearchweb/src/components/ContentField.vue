<template>
    <div class="contentfield">
        <div class="kongbai"></div>
        <slot></slot>
    </div>
</template>

<script>

</script>

<style scoped>
.contentfield{
    background-color: rgba(7, 7, 7, 0.5);
    height: 40vh;
    width: 80vw;
    margin-top: 20px;
    margin-left: 10vw;
}
.contentfield .kongbai{
    height: 10vh;
}
</style>