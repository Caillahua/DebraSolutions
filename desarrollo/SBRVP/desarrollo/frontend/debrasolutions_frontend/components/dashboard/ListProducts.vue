<template>
  <div id="page">
    <div class="card">
      <div class="card-title">
        <h1>Productos</h1>
      </div>
      <div class="content">
        <table class="table is-bordered is-striped is-narrow is-hoverable is-fullwidth">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Categoria</th>
              <th>Precio</th>
              <th>Detalles</th>
            </tr>
          </thead>
          <tbody  >
            <tr v-for="product in products">
              <th>{{ product.descripcion }}</th>
              <th>{{ product.NombreCategoria}}</th>
              <th>S/ {{ product.precio }}</th>
              <th><a @click="showProductDetail(product.idArticulo)" class="button is-primary">Detalle</a></th>
            </tr>
          </tbody>
        </table>
        <button class="btn btn-primary" @click="showCreateProduct">Añadir Producto</button>
      </div>
    </div>
  </div>
</template>
<script>
import Axios from 'axios';
export default {
  name: "ListProductsView",

  data(){
    return{
      products: [],
    }
  },

  mounted(){
    Axios.get("https://capturaloservice.azurewebsites.net/Capturalo/BuscarArticulo2",{
      params: {
        idArticulo: 0,
        nombreArticulo: "",
        idCategoria: 0,
        caracteristicas: "",
        idTienda: parseInt(localStorage.getItem("idTienda"))
      }
    }).then( result => {
      console.log("lista de productos", result);
      this.products = result.data;
    })
  },

  methods: {
    showCreateProduct(){
      console.log("showCreateProduct");
      this.$emit("showCreateProduct");
    },
    showProductDetail(idProducto){
      console.log("IDPRODUCTO", idProducto);
      this.$emit("showProductDetail", idProducto);
    }
  }
}
</script>
<style lang="scss" scoped>
@import '../../../node_modules/bulma/bulma.sass';
</style>
