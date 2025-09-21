<template>
  <ul class="nav nav-tabs" style="margin-top: 2%">
    <li class="nav-item">
      <a class="nav-link active" aria-current="page" href="/admin">Usuarios</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" href="/admin/anuncios">Anuncios</a>
    </li>
  </ul>

  <form
    class="d-flex"
    role="search"
    style="
      width: 30%;
      float: left;
      margin-left: 7%;
      margin-bottom: 2%;
      margin-top: 2%;
    "
  >
    <input
      class="form-control me-2"
      type="search"
      placeholder="Introduzca email"
      aria-label="Search"
      v-model="filtros"
    />
  </form>
  <div class="container">
    <table id="userData" class="table table-striped table-bordered">
      <thead>
        <tr>
          <th>ID</th>
          <th>Email</th>
          <th>Nombre</th>
          <th>Teléfono</th>
          <th>Nº anuncios</th>
          <th>Estado</th>
        </tr>
      </thead>
      <tbody>
        <tr id="tableTr" v-for="user in filteredRows" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.nombre }}</td>
          <td>{{ user.telefono }}</td>
          <td>5</td>
          <td v-if="user.estado === true">Activo</td>
          <td v-else>Desactivado</td>
          <td>
            <button
              v-if="user.estado === true"
              type="button"
              class="btn btn-danger"
              @click="cambiarEstado(user.id)"
            >
              Desactivar
            </button>
            <button
              v-if="user.estado === false"
              type="button"
              class="btn btn-success"
              @click="cambiarEstado(user.id)"
            >
              Activar
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import UsuarioRepository from "@/repositories/UsuarioRepository";

export default {
  name: "AdminOptions",
  props: {
    msg: String,
  },
  data() {
    return {
      users: [],
      filtros: "",
    };
  },
  computed: {
    filteredRows() {
      return this.users.filter((user) => {
        const emails = user.email.toString().toLowerCase();
        const searchTerm = this.filtros.toLowerCase();
        return emails.includes(searchTerm);
      });
    },
  },
  mounted() {
    this.getUsers();
  },
  methods: {
    async getUsers() {
      UsuarioRepository.getAllAccounts().then((response) => {
        this.users = response;
        this.filteredRows = this.users;
      });
    },
    async cambiarEstado(id) {
      await UsuarioRepository.cambiarEstadoUsuario(id);
      await this.getUsers();
    },
  },
};
</script>
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
