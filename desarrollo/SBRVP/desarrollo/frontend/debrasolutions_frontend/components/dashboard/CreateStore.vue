<template>
  <div id="page">
    <div class="card">
      <div class="card-title">
        <h1>Aun no tienes un tienda!</h1>
      </div>
      <div class="content">
        <button @click="showRegisterStore">Crear Tienda</button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "CreateStoreView",
  methods: {
    showRegisterStore(){
      this.$emit("showRegisterStore");
    }
  }
}
</script>
<style lang="scss" scoped>
@import '../../../node_modules/bulma/bulma.sass';
</style>


