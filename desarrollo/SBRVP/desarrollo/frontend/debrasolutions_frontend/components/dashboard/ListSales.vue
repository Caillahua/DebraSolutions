<template>
  <div id="page">
    <div class="card">
      <div class="card-title">
        <h1>Ventas</h1>
      </div>
      <div class="content">
        <table class="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
          <thead>
            <tr>
              <th>fecha</th>
              <th>Cliente</th>
              <th>Monto</th>
              <th>Detalles</th>
            </tr>
          </thead>
          <tbody  >
            <tr v-for="sale in sales">
              <th>{{sale.fecha}}</th>
              <th>{{ sale.nombreCliente }}</th>
              <th>S/ {{ sale.total}}</th>
              <th><a @click="showSaleDetail( sale.idVenta)" class="button is-primary">Detalle</a></th>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
<script>
import Axios from 'axios';
export default {
  name: "ListSalesView",

  data(){
    return{
      sales: null,
    }
  },

  mounted(){
    Axios.get("https://capturaloservice.azurewebsites.net/Capturalo/listarVentaCabecera",{
      params: {
        idCliente: 0,
        idTienda: 0
      }
    }).then( result => {
      console.log("lista de productos", result);
      this.sales = result.data;
    })
  },

  methods: {
    showCreateProduct(){
      console.log("showCreateProduct");
      this.$emit("showCreateProduct");
    },
    showSaleDetail(idVenta){
      this.$emit("showSaleDetail", idVenta);
    }
  }
}
</script>
<style lang="scss" scoped>
@import '../../../node_modules/bulma/bulma.sass';
</style>
