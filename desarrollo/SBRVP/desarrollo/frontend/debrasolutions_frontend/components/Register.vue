<template>
  <div id="page">
    <div class="card">
      <div class="card-title">
        <h1>Registrar cuenta</h1>
      </div>

      <div class="content">
        <form  v-on:submit.prevent="onSubmit">
          <input
            maxlength="20"
            id="usuario"
            type="text"
            name="usuario"
            title="usuario"
            placeholder="Usuario"
            v-model="usuario"
            required
            autofocus
          >
          <input
            v-validate="'required|email'"
            id="correo"
            type="correo"
            name="email"
						title="email"
            placeholder="Email"
            v-model="correo"
            required
            autofocus
          >
          <span class="error">{{ errors.first('email') }}</span>
          <input v-validate="'required'" name="password" type="password" placeholder="Password" ref="password">
          <input v-validate="'required|confirmed:password'" name="password_confirmation" type="password" placeholder="Password, Again" data-vv-as="password">
          <div class="error" v-if="errors.has('password_confirmation')">
            {{ errors.first('password_confirmation') }}
          </div>
          <input id="perfil" type="hidden" name="perfil" title="perfil" v-model="perfil" required>
          <button type="submit" class="btn btn-primary">Registrar</button>
        </form>
        <button type="submit" class="btn btn-primary" @click="backToLogin">Cancelar</button>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';
export default {
  name: "RegisterPage",
  data() {
    return {
      usuario: null,
      correo: null,
      contrasena: null,
      recontrasena: null,
      perfil: 1,
    }
  },
	methods: {
		backToLogin() {
			this.$emit( "backToLogin" );
    },
    changePassword(){

    },
    onSubmit() {

      const {
        usuario,
        correo,
        contrasena,
        perfil
      } = this;

      const body = {
        usuario,
        correo,
        contrasena,
        perfil
      }
      axios.post("https://capturaloservice.azurewebsites.net/Capturalo/InsertarUsuario", body ).then( result => {
        this.$emit("showLoginPage");
      })
    }
	}
}
</script>

<style lang="scss" >
@import '../../node_modules/bulma/bulma.sass';
</style>



