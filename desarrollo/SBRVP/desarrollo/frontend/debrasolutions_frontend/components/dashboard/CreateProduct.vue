<template>
  <div id="page">
    <div class="card">
      <div class="card-title">
        <h1>Crear Producto</h1>
      </div>
      <div class="content">
        <form v-on:submit.prevent="onSubmit">
          <input maxlength="20" type="text" placeholder="Nombre"  v-model="nombre" required autofocus>
          <div class="field">
            <div class="control">
              <textarea class="textarea is-primary" v-model="caracteristica" placeholder="Características"></textarea>
            </div>
          </div>
          <div class="select is-rounded">
            <select v-model="category_selected" >
              <option disabled value=0 >Categoría</option>
              <option :value="category.idCategoria" v-for="(category, index) in categories">{{ category.descripcion }}</option>
            </select>
          </div>
          <input type="text"  placeholder="Link de imagen" v-model="ruta_imagen" required>
          <div class="file has-name">
            <label class="file-label">
              <input class="file-input" type="file" ref="file" id="file" name="resume" v-on:change="handleFileUpload($event)">
              <span class="file-cta">
                <span class="file-icon">
                  <i class="fas fa-upload"></i>
                </span>
                <span class="file-label">
                  Choose a file…
                </span>
              </span>
              <span class="file-name">
                  {{ nameFile }}
              </span>
            </label>
          </div>
          <input type="tel"  placeholder="Stock" v-model="stock" required>
          <input type="number"  placeholder="Precio" v-model="precio" required>
          <input type="number"  placeholder="Descuento" v-model="descuento" required>
          <button type="submit" class="btn btn-primary">Crear</button>
        </form>
          <button @click="showListProducts"  class="btn btn-primary">Regresar</button>
      </div>
    </div>
  </div>
</template>
<script>
import Axios from 'axios';
import AzureStorage from 'azure-storage';
export default {
  name: "CreateProductView",

  data(){
    return {
      nombre: "",
      caracteristica: "",
      ruta_imagen: "",
      stock: null,
      precio: null,
      descuento: null,
      categories: null,
      category_selected: 0,
      file: '',
      nameFile: ''
    }
  },

  mounted(){
    Axios.get('https://capturaloservice.azurewebsites.net/Capturalo/listarCategoria', {
      params: {
        idCategoria: 0,
        descripcion: ""
      }
    }).then( result => {
      console.log("listar categorías", result);
      this.categories = result.data;
    })
  },

  methods: {
    onSubmit(){

      let formData = new FormData();
      formData.append('archivo', this.file);
      formData.append('nombreArchivo', this.nameFile);

      Axios.post( 'https://capturaloservice.azurewebsites.net/Capturalo/UploadImage_URL',
        formData,
        {
          headers: {
              'Content-Type': 'multipart/form-data'
          }
        }
      ).then(function(){
        console.log('SUCCESS!!');
      })
      .catch(function(){
        console.log('FAILURE!!');
      });

      const {
        ruta_imagen,
        stock,
        precio,
        descuento
      } = this;

      const body = {
        descripcion: this.nombre,
        caracteristicas: this.caracteristica,
        ruta_imagen,
        idcategoria: this.category_selected,
        estado: 1,
        idTienda: parseInt(localStorage.getItem("idTienda")),
        stock,
        precio,
        descuento
      }

      Axios.post("https://capturaloservice.azurewebsites.net/Capturalo/guardarArticulo", body).then( result => {
        console.log("producto guardado");
        this.$emit("showListProducts");
      })
      console.log("crear")
    },
    handleFileUpload(event){
      this.file = this.$refs.file.files[0];
      this.nameFile = this.$refs.file.value;
      console.log(this.file);
    },
    showListProducts(){
      this.$emit("showListProducts");
    }
  }
}
</script>
<style lang="scss" scoped>
@import '../../../node_modules/bulma/bulma.sass';
</style>
