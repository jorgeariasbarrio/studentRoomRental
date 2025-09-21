<template>
  <ul class="nav nav-tabs" style="margin-top: 2%">
    <li class="nav-item">
      <a class="nav-link" href="/admin">Usuarios</a>
    </li>
    <li class="nav-item">
      <a class="nav-link active" aria-current="page" href="/admin/anuncios"
        >Anuncios</a
      >
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
          <th>Autor</th>
          <th>Título</th>
          <th>Fecha Publicación</th>
          <th>Detalle</th>
        </tr>
      </thead>
      <tbody>
        <tr
          id="tableTr"
          v-for="habitacion in filteredRows"
          :key="habitacion.id"
        >
          <td>{{ habitacion.id }}</td>
          <td>{{ habitacion.piso.usuario.email }}</td>
          <td>{{ habitacion.titulo }}</td>
          <td>{{ habitacion.fechaCreacion }}</td>
          <td>
            <router-link :to="'/' + habitacion.id"> Detalle</router-link>
          </td>
          <td>
            <button
              class="btn btn-danger"
              v-if="habitacion.estadoHabitacion === 'DISPONIBLE'"
              v-on:click="desactivarHabitacion(habitacion.id)"
            >
              Desactivar
            </button>
            <button
              class="btn btn-success"
              v-if="habitacion.estadoHabitacion === 'OCULTA'"
              v-on:click="desactivarHabitacion(habitacion.id)"
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
import HabitacionRepository from "@/repositories/HabitacionRepository";

export default {
  name: "AdminOptionsHabitaciones",
  props: {
    msg: String,
  },
  data() {
    return {
      habitaciones: [],
      filtros: "",
    };
  },
  computed: {
    filteredRows() {
      return this.habitaciones.filter((habitacion) => {
        const emails = habitacion.piso.usuario.email.toString().toLowerCase();
        const searchTerm = this.filtros.toLowerCase();
        return emails.includes(searchTerm);
      });
    },
  },
  mounted() {
    this.getHabitaciones();
  },
  methods: {
    async getHabitaciones() {
      await HabitacionRepository.findAll().then((response) => {
        this.habitaciones = response;
        this.filteredRows = this.habitaciones;
      });
    },
    async desactivarHabitacion(id) {
      await HabitacionRepository.cambiarEstadoHabitacion(id);
      await this.getHabitaciones();
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
