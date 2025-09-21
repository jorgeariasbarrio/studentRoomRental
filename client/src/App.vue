<template>
  <nav
    class="navbar navbar-expand-lg navbar-dark"
    style="background-color: #132e13"
  >
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/">
        PisosParaEstudiantes
      </router-link>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link
              to="/"
              class="nav-link"
              aria-current="page"
              active-class="active"
            >
              Home
            </router-link>
          </li>
          <li class="nav-item" v-if="!isLogged">
            <router-link class="nav-link" to="/login" active-class="active">
              Iniciar sesion
            </router-link>
          </li>
          <li class="nav-item" v-if="!isLogged">
            <router-link class="nav-link" to="/register" active-class="active">
              Registrarse
            </router-link>
          </li>
          <li class="nav-item" v-if="isLogged">
            <router-link class="nav-link" to="/crearPiso" active-class="active">
              Crear Habitaciones
            </router-link>
          </li>
          <li class="nav-item" v-if="isLogged">
            <router-link class="nav-link" to="/favoritos" active-class="active">
              Anuncios Favoritos
            </router-link>
          </li>
          <li class="nav-item" v-if="isLogged">
            <router-link
              class="nav-link"
              to="/misAnuncios"
              active-class="active"
            >
              Mis anuncios
            </router-link>
          </li>
          <li class="nav-item" v-if="isLogged">
            <router-link class="nav-link" to="/miPerfil" active-class="active">
              Mi perfil
            </router-link>
          </li>
          <li class="nav-item" v-if="isLogged && isAdmin">
            <router-link class="nav-link" to="/admin" active-class="active">
              Opciones Administrador
            </router-link>
          </li>
          <li class="nav-item" v-if="isLogged">
            <a class="nav-link" @click="logout()"> Desautenticarme </a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <router-view />
</template>

<script>
import auth from "./common/auth";
import { getStore } from "./common/store";

export default {
  data() {
    return {
      store: getStore(),
    };
  },
  computed: {
    isLogged() {
      return this.store.state.user.logged;
    },
    isAdmin() {
      return this.store.state.user.authority === "ADMIN";
    },
  },
  methods: {
    logout() {
      auth.logout();
      // Después de hacer logout nos vamos a home
      if (this.$router.currentRoute.name !== "home") {
        this.$router.push({ name: "home" });
      }
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: darkgreen;
  cursor: pointer;
}

/* fixing popper warning in bootstrap 5.2: */
.dropdown-menu {
  margin-top: 0px !important;
}
</style>
