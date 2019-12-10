<template>
  <div id="page">
    <div class="card">
      <div class="card-title">
        <h1>DETALLE DE VENTA</h1>
      </div>
      <div class="content">
        <table class="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
          <tbody  >
            <tr >
              <th><span class="spanTitle">Articulo:</span> {{sale.descripcionArticulo}}</th>
            </tr>
            <tr >
              <th><span class="spanTitle">Características:</span> {{sale.caracteristicas}}</th>
            </tr>
            <tr>
              <th><span class="spanTitle">Cantidad:</span> {{ sale.cantidad }}</th>
            </tr>
            <tr>
              <th><span class="spanTitle">Descuento:</span> S/ {{ sale.descuento }}</th>
            </tr>
            <tr>
              <th><span class="spanTitle">Monto Total:</span> S/ {{ sale.monto }}</th>
            </tr>

          </tbody>
        </table>
        <a @click="showListSales" class="button is-primary">Lista de Ventas</a>
      </div>
    </div>
  </div>
</template>
<script>
import Axios from 'axios';
import { METHODS } from 'http';
export default {
  name:"DetailSaleView",
  props:{
    idSale: null,
    sale: null,
  },
  data(){
    return{

    }
  },
  mounted(){
     Axios.get("https://capturaloservice.azurewebsites.net/Capturalo/listarVentaDetalle",{
      params: {
        idVenta: this.idSale,
      }
    }).then( result => {
      console.log("Venta", result);
      this.sale = result.data[0];
    })
  },
  methods: {
    showListSales(){
      this.$emit("showListSales");
    }
  }

}
</script>
<style lang="scss" scoped >
@import '../../../node_modules/bulma/bulma.sass';

.spanTitle{
  font-weight: bold;
  font-size: 20px;
}

th{
  font-weight: 100;
}

</style>

