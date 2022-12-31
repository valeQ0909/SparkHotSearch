<template>
    <ContentField>
          <div class="err_infor">
              404 not found
          </div>
    </ContentField>
  </template>
  
  <script>
  import ContentField from '../../components/ContentField.vue'
  
  export default {
      components:{
          ContentField
      }
  }
  
  </script>
  
  <style scoped>
  .err_infor{
      margin-left: 30%;
      color: white;
      font-size: 10vh;
  }
  
  </style>
  