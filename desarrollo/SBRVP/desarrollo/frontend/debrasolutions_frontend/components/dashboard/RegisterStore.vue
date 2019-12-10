<template>
  <div id="page">
    <div class="card">
      <div class="card-title">
        <h1>Registra Tienda</h1>
      </div>
      <div class="content">
        <form v-on:submit.prevent="onSubmit">
          <input id="rsocial" type="text" name="rsocial" title="rsocial" placeholder="Razón social"  v-model="rsocial" required autofocus>
          <input id="direccion" type="text" name="direccion" title="direccion" placeholder="Dirección" v-model="direccion" required>
          <input id="ruc" type="number" name="ruc" title="ruc" placeholder="Ruc" v-model="ruc" required>
          <div class="checkbox-wrapper">
            <label class="checkbox">
              <input type="checkbox" v-model="ventaOnline">
              Venta Online
            </label>
            <label class="checkbox">
              <input type="checkbox" v-model="delivery">
              Delivery
            </label>
            <label class="checkbox">
              <input type="checkbox" v-model="recojoTienda">
              Recojo en Tienda
            </label>
          </div>
          <button type="submit" class="btn btn-primary">Registrar</button>
        </form>
      </div>
    </div>
  </div>
</template>
<script>
import Axios from 'axios';
export default {
  name: "RegisterStoreView",
  data(){
    return{
      rsocial: "",
      direccion: "",
      ruc: null,
      ventaOnline: false,
      delivery: false,
      recojoTienda: false,
    }
  },
  methods: {
    onSubmit(){
      const {
        rsocial,
        direccion,
        ventaOnline,
        delivery,
        recojoTienda
      }= this;

      let ruc = parseInt(this.ruc);
      let idUsuario = parseInt(localStorage.getItem("idUsuario"));

      const body = {
        rsocial,
        direccion,
        ruc,
        ventaOnline,
        delivery,
        recojoTienda,
        idUsuario,
        estado: 1,
      }
      console.log("request body", body);
      Axios.post("https://capturaloservice.azurewebsites.net/Capturalo/InsertarTienda", body ).then( result => {
        console.log("result registar tienda", result);
        this.$emit("showListProducts");
      })
    }
  }
}
</script>
<style lang="scss" scoped>
@import '../../../node_modules/bulma/bulma.sass';

.checkbox-wrapper{
  display: flex;
  flex-direction: column;
  align-items: baseline;
  margin-bottom: 20px;
}
.radio-wrapper {
  display: flex;
  justify-content: space-between;
}

</style>
