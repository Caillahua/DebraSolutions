<template>
  <div>
    <nav class="navbar" role="navigation" aria-label="main navigation">
      <div class="navbar-brand">
        <a class="navbar-item" href="https://bulma.io">
          <img src="https://bulma.io/images/bulma-logo.png" width="112" height="28">
        </a>

        <a role="button" class="navbar-burger burger" aria-label="menu" aria-expanded="false" data-target="navbarBasicExample">
          <span aria-hidden="true"></span>
          <span aria-hidden="true"></span>
          <span aria-hidden="true"></span>
        </a>
      </div>

      <div id="navbarBasicExample" class="navbar-menu">
        <div class="navbar-start">
          <a @click="showListSales" class="navbar-item">
            Ventas
          </a>

          <a @click="showListProducts" class="navbar-item">
            Productos
          </a>
        </div>

        <div class="navbar-end">
          <div class="navbar-item">
            <div class="buttons">
              <a @click="logout" class="button is-primary">
                <strong>Cerrar Sesión</strong>
              </a>
            </div>
          </div>
        </div>
      </div>
    </nav>
    <create-store v-if=" view === 'createstore' "
      @showRegisterStore = "showRegisterStore"
    />
    <register-store v-if=" view === 'registerstore' "
      @showListProducts = "showListProducts"
    />
    <list-products v-if=" view === 'listproducts' "
      @showCreateProduct = "showCreateProduct"
      @showProductDetail = "showProductDetail"
    />
    <create-product v-if=" view === 'createproduct' "
      @showListProducts = "showListProducts"
    />
    <list-sales v-if=" view === 'listsales' "
      @showSaleDetail = "showSaleDetail"
    />
    <detail-product v-if=" view === 'detailproduct' "
      :idProduct = "idProduct"
      @showListProducts = "showListProducts"
    />
    <detail-sale v-if=" view === 'detailsale' "
      :idSale = "idSale"
      @showListSales = "showListSales"
    />
  </div>
</template>
<script>
import CreateStore from './CreateStore';
import RegisterStore from './RegisterStore';
import ListProducts from './ListProducts';
import CreateProduct from './CreateProduct';
import ListSales from './ListSales';
import DetailProduct from './DetailProduct';
import DetailSale from './DetailSale';
import Axios from 'axios';

export default {
  components:{
    CreateStore,
    RegisterStore,
    ListProducts,
    CreateProduct,
    ListSales,
    DetailProduct,
    DetailSale
  },
  name:"DashboardPage",
  data() {
    return {
      view: null,
      idProduct: null,
      idSale: null,
    }
  },

  mounted(){
    console.log("[dashboard] created");
    const body = {
      idTienda : 0,
      rsocial: null,
      idusuario: parseInt(localStorage.getItem("idUsuario"))
    }
    Axios.post("https://capturaloservice.azurewebsites.net/Capturalo/buscarTienda", body).then( result => {
      console.log("Tiene tienda?",result);
      if (result.data){
        localStorage.setItem("idTienda", result.data[0].idTienda);
        this.view = "listproducts";
        console.log("listProducts", result.data)
      }else{
        this.view = "createstore";
        console.log("createstore", result.data)
      }
    })
  },

  methods: {
    showRegisterStore(){
      this.view = "registerstore";
    },
    showCreateProduct(){
      console.log("SHOW CREATE PRODUCT DASHBOARD FUNCTION");
      this.view = "createproduct";
    },
    showListProducts(){
      this.view = "listproducts";
    },
    showListSales(){
      this.view = "listsales";
    },
    showProductDetail(idProduct){
      this.view = "detailproduct";
      this.idProduct = idProduct;
    },
    showSaleDetail(idSale){
      this.view = "detailsale";
      this.idSale = idSale;
    },
    logout(){
      location.reload();
    }
  }
}
</script>
<style lang="scss" scoped>
  @import '../../../node_modules/bulma/bulma.sass';
</style>


