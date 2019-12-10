<template>
  <div id="page">
    <div class="card">

      <div class="card-title">
        <h1 class="capturalo">CAPTURALO</h1>
      </div>

      <div class="content">
        <form v-on:submit.prevent="onSubmit">

          <input
						type="text"
						name="usuario"
						title="usuario"
						placeholder="Usuario"
						v-model="usuario"
						maxlength="20"
						required autofocus
					>
          <input
						maxlength="20"
						id="password"
						type="password"
						name="password"
						title="password"
						placeholder="Contraseña"
						v-model="contrasena"
						required
					>
					<span class="error">{{ errorMessage }}</span>
          <button type="submit" class="btn btn-primary">Ingresar</button>
        </form>
				<button class="btn btn-primary" @click="showRegisterPage">Registrar</button>
      </div>
    </div>
  </div>
</template>

<script>
import Axios from 'axios';

export default {
  name: 'LoginPage',
  props: {
	},
	data(){
		return{
			usuario: null,
			contrasena: null,
			errorMessage: "",
			error: false
		}
	},

	methods: {
		showRegisterPage() {
			this.$emit( "showRegisterPage" );
		},

		async onSubmit(){
			const {
				usuario,
				contrasena
			} = this;

			const body = {
				usuario,
				contrasena
			}

			let response = await Axios.post("https://capturaloservice.azurewebsites.net/Capturalo/ValidarCredenciales", body)
			console.log("Autenticación", response);
			console.log("idUsuario", response.data.idUsuario);
			localStorage.setItem("idUsuario", response.data.idUsuario);
			if(response.data.idUsuario == 0 ){
				this.errorMessage = "usuario o contraseña inválidos";
				this.error = true;
			}else{
				this.$emit("showDashboardPage");
			}
		}
	}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" >
@import '../../node_modules/bulma/bulma.sass';
@import '../sass/index.scss';

.capturalo {
	font-size: 20px;
	font-weight: bold;
}

#page {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 100vh;
	background: #F7F7F7;

	.card {
		background: #fff;
		min-width: 24rem;
		box-shadow: 0 0 7px 0 rgba(0, 0, 0, 0.11);

		.card-title {
			background-color: darken($primary, 5%);
			padding: 2rem;

			h1 {
				color: #fff;
				text-align: center;
				font-size: 1.2rem;
			}
		}

		.content {
			padding: 3rem 2.5rem 5rem;
		}

		input {
			display: block;
			width: 100%;
			font-size: 1rem;
			margin-bottom: 20px;
			padding: 0.25rem 0;
			border: none;
			border-bottom: 1px solid $grey-lighter;
			transition: all .5s;

			&:hover {
				border-color: $grey;
			}

			&:active, &:focus {
				border-color: $primary;
			}
		}

		a {
			font-size: 0.8rem;
		}

		button {
			cursor: pointer;
			font-size: 1.2rem;
			color: $primary;
			border-radius: 4rem;
			display: block;
			width: 100%;
			background: transparent;
			border: 2px solid $primary;
			padding: 0.9rem 0 1.1rem;
			transition: color .5s, border-color .5s;
      margin-bottom: 15px;

			&:hover, &:focus {
				color: darken($primary, 10%);
				border-color: darken($primary, 10%);
			}

			&:active {
				transform: translateY(1px);
			}
		}
	}
}

label {
	cursor: pointer;
}

.regular-checkbox {
	display: none;
}

.regular-checkbox + label {
	background-color: #fafafa;
	border: 1px solid $grey-lighter;
	box-shadow: 0 1px 2px rgba(0,0,0,0.05);
	padding: 7px;
	border-radius: 3px;
	display: inline-block;
	position: relative;
}

.regular-checkbox:checked + label {
	background-color: #e9ecee;
}

.regular-checkbox:checked + label:after {
	content: '\2714';
	font-size: 11px;
	position: absolute;
	top: 0;
	left: 3px;
	color: $grey-light;
}

input:focus,
select:focus,
textarea:focus,
button:focus {
	outline: none;
}
</style>
