<template>
  <ul class="nav nav-tabs" style="margin-top: 3%">
    <li class="nav-item">
      <a class="nav-link active" aria-current="page" href="/miPerfil"
        >Información perfil</a
      >
    </li>
    <li class="nav-item">
      <a class="nav-link" href="/misReviews">Mis reviews</a>
    </li>
  </ul>
  <br />
  <form>
    <div class="container">
      <div class="form-floating">
        <input
          class="form-control"
          id="nombreUsuario"
          v-model="usuario.nombre"
          type="text"
          :class="{ errorsInput: errorNombre }"
          disabled
        />
        <label for="nombreUsuario">Nombre</label>
        <h6 v-if="errorNombre === true" style="color: red">
          Debe indicarse un nombre
        </h6>
      </div>
    </div>
    <div class="container">
      <div class="form-floating">
        <input
          id="emailUsuario"
          class="form-control"
          v-model="usuario.email"
          type="text"
          :class="{ errorsInput: errorEmail }"
          disabled
        />
        <label for="emailUsuario">Email</label>
        <h6 v-if="errorEmail === true" style="color: red">
          Debe indicarse un email válido
        </h6>
      </div>
    </div>
    <div class="container">
      <div class="form-floating">
        <input
          id="telefonoUsuario"
          class="form-control"
          v-model="usuario.telefono"
          type="number"
          :class="{ errorsInput: errorTelefono }"
          disabled
        />
        <label for="telefonoInput">Teléfono</label>
        <h6 v-if="errorTelefono === true" style="color: red">
          Debe indicarse un teléfono válido
        </h6>
      </div>
    </div>
    <div class="container">
      <div class="form-floating">
        <input
          class="form-control"
          id="loginUsuario"
          v-model="usuario.login"
          type="text"
          :class="{ errorsInput: errorLogin }"
          disabled
        />
        <label for="nombreUsuario">Login</label>
        <h6 v-if="errorLogin === true" style="color: red">
          Debe indicarse un login
        </h6>
      </div>
    </div>
    <div class="container" v-if="editandoPerfil === true">
      <div class="form-floating">
        <input
          class="form-control"
          id="contrasenaActual"
          v-model="contrasenaActual"
          type="password"
          :class="{ errorsInput: errorContrasenaActual }"
        />
        <label for="contrasenaActual">Contraseña Actual</label>
        <h6 v-if="errorContrasenaActual === true" style="color: red">
          La contraseña no coincide con la contraseña actual
        </h6>
      </div>
    </div>
    <div class="container" v-if="editandoPerfil === true">
      <div class="form-floating">
        <input
          class="form-control"
          id="nuevaContrasena"
          v-model="nuevaContrasena"
          type="password"
          :class="{ errorsInput: errorNuevaContrasena }"
        />
        <label for="nuevaContrasena">Nueva Contraseña</label>
        <h6 v-if="errorNuevaContrasena === true" style="color: red">
          La nueva contraseña no cumple los requisitos(mínimo 8 carácteres, 1
          mayúscula, una minúscula y un número)
        </h6>
      </div>
    </div>
    <div class="container">
      <button
        class="btn btn-secondary"
        v-on:click="editarPerfil"
        v-if="editandoPerfil === false"
      >
        Editar perfil
      </button>
    </div>
    <div class="btn-group">
      <button
        class="btn btn-success"
        type="button"
        v-if="editandoPerfil === true"
        v-on:click="guardarCambios"
      >
        Confirmar cambios
      </button>
      <button
        class="btn btn-danger"
        v-on:click="cancelarCambios"
        v-if="editandoPerfil === true"
      >
        Cancelar cambios
      </button>
    </div>
  </form>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";
import auth from "@/common/auth";

export default {
  name: "MiPerfil",
  data() {
    return {
      usuario: Object,
      contrasenaActual: null,
      nuevaContrasena: null,
      errorNuevaContrasena: false,
      errorEmail: false,
      errorNombre: false,
      errorTelefono: false,
      editandoPerfil: false,
      errorLogin: false,
      errorContrasenaActual: false,
    };
  },
  mounted() {
    this.getUsuario();
  },
  methods: {
    async getUsuario() {
      this.usuario = await AccountRepository.getAccount();
    },
    editarPerfil() {
      document.getElementById("nombreUsuario").disabled = false;
      document.getElementById("emailUsuario").disabled = false;
      document.getElementById("telefonoUsuario").disabled = false;
      document.getElementById("loginUsuario").disabled = false;
      this.editandoPerfil = true;
    },
    async guardarCambios() {
      this.errorEmail = false;
      this.errorLogin = false;
      this.errorNombre = false;
      this.errorTelefono = false;
      this.errorNuevaContrasena = false;
      this.errorContrasenaActual = false;
      let errores = false;
      if (String(this.usuario.telefono).length !== 9) {
        this.errorTelefono = true;
        errores = true;
      }
      if (this.usuario.login === "" || this.usuario.login === null) {
        this.errorLogin = true;
        errores = true;
      }
      if (this.usuario.nombre === "" || this.usuario.nombre === null) {
        this.errorNombre = true;
        errores = true;
      }
      if (
        this.usuario.email === "" ||
        !this.usuario.email.match(
          new RegExp("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        )
      ) {
        this.errorEmail = true;
        errores = true;
      }
      if (
        this.nuevaContrasena === null ||
        !this.nuevaContrasena.match(
          new RegExp("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}$")
        )
      ) {
        this.errorNuevaContrasena = true;
        errores = true;
      }
      if (errores === false) {
        if (this.nuevaContrasena !== null) {
          try {
            await AccountRepository.actualizarContrasena(
              this.usuario.id,
              this.contrasenaActual,
              this.nuevaContrasena
            );
          } catch (error) {
            console.log(error);
          }
        }
        await AccountRepository.actualizarAccount(this.usuario);
        auth.logout();
        this.$router.push({ name: "login" });
      }
    },
    async cancelarCambios() {
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped>
input {
  width: 30%;
  margin-top: 3%;
}
input.errorsInput {
  border-color: red;
}
h6 {
  float: left;
}
</style>
