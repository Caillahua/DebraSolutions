<template>
  <div id="page">
    <div class="card">
      <div class="card-title">
        <h1>DETALLE DE PRODUCTO</h1>
      </div>
      <div class="content">
          <div class="card-image">
            <figure class="image is-4by3">
              <img src="https://bulma.io/images/placeholders/1280x960.png" alt="Placeholder image">
            </figure>
          </div>
          <div class="card-content">
            <div class="media">
              <div class="media-content">
                <p class="title is-4">{{ product.descripcion }}</p>
                <p class="subtitle is-6">{{ product.NombreCategoria }}</p>
              </div>
              <div class="media-right">
                <div class="price" >S/ {{ product.precio }}</div>
              </div>
            </div>
            <div>Stock {{ product.stock }}</div>
          </div>
          <a @click="showListProducts" class="button is-primary">Lista de Productos</a>
      </div>
    </div>
  </div>
</template>
<script>
import Axios from 'axios';
export default {
  name: "DetailProductView",
  props:{
    idProduct: null,
  },
  data(){
    return {
      product: null,
    }
  },
  mounted(){
    Axios.get("https://capturaloservice.azurewebsites.net/Capturalo/BuscarArticulo2",{
      params: {
        idArticulo: this.idProduct,
        nombreArticulo: "",
        idCategoria: 0,
        caracteristicas: "",
        idTienda: 0
      }
    }).then( result => {
      console.log("Producto", result);
      this.product = result.data[0];
    })
  },
  methods: {
    showListProducts(){
      this.$emit("showListProducts");
    }
  }
}
</script>
<style lang="scss" >
@import '../../../node_modules/bulma/bulma.sass';

figure {
  margin: 0 !important;
}
.price {
  font-size: 25px;
  font-weight: bold;
}

</style>

