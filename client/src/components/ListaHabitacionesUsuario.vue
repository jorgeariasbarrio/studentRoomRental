<template>
  <div v-if="empty">
    <h3>No existe el usuario con ID {{ this.$route.params.id }}</h3>
    <router-link class="card-link" :to="'/'"> Ver anuncios </router-link>
  </div>
  <div v-else>
    <div id="usuario" class="user">
      <UsuarioCardPerfil :usuario="usuario"></UsuarioCardPerfil>
    </div>
    <div v-if="usuarioSinAnuncio === false">
      <h2>Habitaciones publicadas por {{ usuario.nombre }}</h2>
      <div class="main">
        <div class="columnafiltros">
          <ColumnaFiltros @filtros="filtros"></ColumnaFiltros>
        </div>
        <div id="anuncios" class="row-cols-1">
          <div
            class="row-cols-1"
            v-for="habitacion in habitaciones"
            :key="habitacion.id"
          >
            <HabitacionCard
              :habitacion="habitacion"
              :centro-estudios="null"
            ></HabitacionCard>
          </div>
          <h6
            v-if="filtradoVacio === true"
            style="margin-right: auto; margin-left: auto; margin-top: 20%"
          >
            No existen anuncios con esas caracteristicas, modifica alguno de los
            filtros!
          </h6>
        </div>
      </div>
    </div>
    <div v-if="usuarioSinAnuncio === true">
      <h3>El usuario {{ usuario.nombre }} no tiene anuncios publicados</h3>
      <router-link to="/">Ver todos los anuncios</router-link>
    </div>
  </div>
</template>

<script>
import HabitacionCard from "@/components/HabitacionCard";
import ColumnaFiltros from "@/components/ColumnaFiltros";
import AccountRepository from "@/repositories/AccountRepository";
import HabitacionRepository from "@/repositories/HabitacionRepository";
import PisoRepository from "@/repositories/PisoRepository";
import UsuarioCardPerfil from "@/components/UsuarioCardPerfil";
export default {
  name: "ListaHabitacionesUsuario",
  data() {
    return {
      filtrosCopia: "",
      habitaciones: [],
      usuario: Object,
      empty: false,
      usuarioSinAnuncio: false,
      filtradoVacio: false,
    };
  },
  components: {
    UsuarioCardPerfil,
    HabitacionCard,
    ColumnaFiltros,
  },
  mounted() {
    this.getUsuario();
  },
  methods: {
    async getUsuario() {
      try {
        this.usuario = await AccountRepository.findById(this.$route.params.id);
        await this.getHabitaciones();
      } catch (e) {
        this.empty = true;
      }
    },
    async getHabitaciones() {
      let pisos = await PisoRepository.getPisosUsuario(this.$route.params.id);
      for (let i = 0; i < pisos.length; i++) {
        this.habitaciones = this.habitaciones.concat(pisos.at(i).habitaciones);
      }
      if (this.habitaciones.length === 0) {
        this.usuarioSinAnuncio = true;
      }
    },
    async filtros(filtros) {
      this.filtradoVacio = false;
      this.copiaFiltros = filtros;
      if (filtros.campus !== null) {
        this.campus = filtros.campus;
        this.habitaciones = await HabitacionRepository.findAllFilter(
          filtros.provincia,
          filtros.distanciaMaxima,
          filtros.campus.id,
          filtros.precioMaximo,
          filtros.numHabitaciones,
          filtros.inicioContrato,
          filtros.finContrato,
          filtros.opciones,
          null,
          this.$route.params.id
        );
      } else {
        this.campus = null;
        this.habitaciones = await HabitacionRepository.findAllFilter(
          filtros.provincia,
          filtros.distanciaMaxima,
          null,
          filtros.precioMaximo,
          filtros.numHabitaciones,
          filtros.inicioContrato,
          filtros.finContrato,
          filtros.opciones,
          null,
          this.$route.params.id
        );
      }
      if (this.habitaciones.length === 0) {
        this.filtradoVacio = true;
      }
    },
  },
};
</script>

<style scoped>
.user {
  margin-left: 20%;
  margin-top: 1%;
}
.main {
  display: flex;
}
.columnafiltros {
  max-width: 30%;
  margin-left: 0;
  margin-right: 5%;
  margin-top: -3%;
}
#anuncios {
  max-width: 70%;
  margin-top: 0;
  margin-bottom: 0;
}
</style>
