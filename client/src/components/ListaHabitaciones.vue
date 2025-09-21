<template>
  <div :class="{ ocultar: mostrarBusquedaInicial === 'false' }">
    <BusquedaInicial @busquedaInicial="busquedaInicial"></BusquedaInicial>
  </div>
  <div :class="{ ocultar: mostrarBusquedaInicial === 'true' }">
    <a href="#" v-on:click="volverABusquedaInicial" style="margin-top: 10%"
      >Volver a la búsqueda inicial</a
    >
    <div class="btn-group" style="float: right; margin: 1%">
      <a class="btn btn-primary active" aria-current="page"
        >Lista Habitaciones</a
      >
      <a href="/mapaHabitaciones" class="btn btn-primary">Mapa Habitaciones</a>
    </div>
    <div id="ordenacion" style="padding-left: 50%; margin-top: 3%">
      <ColumnaOrdenacion
        :centro-estudios="campus"
        @orden="orden"
      ></ColumnaOrdenacion>
    </div>
    <div id="main">
      <div class="col-3" style="margin-top: -6%">
        <ColumnaFiltros
          @filtros="filtros"
          :provincia-inicial="provinciaInicial"
          :municipio-inicial="municipio"
          :campus-inicial="campusInicial"
        ></ColumnaFiltros>
      </div>
      <div id="anuncios" class="col">
        <h5 v-if="habitacionesPaginadas.length === 0" style="margin-top: 10%">
          Ups! Parece que no hay habitaciones disponibles con esas
          caracteristicas. Intenta cambiar los parametros de los filtros!
        </h5>
        <div
          v-for="habitacion in habitacionesPaginadas"
          :key="habitacion.id"
          style="margin: 10px"
        >
          <HabitacionCard
            :habitacion="habitacion"
            :centro-estudios="campus"
          ></HabitacionCard>
        </div>
        <div v-if="habitacionesPorPagina < habitaciones.length" id="paginacion">
          <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
              <li class="page-item" v-if="paginaActual !== 1">
                <a class="page-link" v-on:click="previousPagina">Previous</a>
              </li>
              <li class="page-item disabled" v-if="paginaActual === 1">
                <a class="page-link" v-on:click="previousPagina">Previous</a>
              </li>
              <li
                v-for="numero_pagina in this.numeroPaginas"
                :key="numero_pagina"
                class="page-item"
              >
                <a
                  class="page-link"
                  :class="{ active: paginaActual === numero_pagina }"
                  v-on:click="pasarPagina(numero_pagina)"
                  >{{ numero_pagina }}</a
                >
              </li>
              <li
                class="page-item"
                v-if="
                  paginaActual !== numeroPaginas.at(numeroPaginas.length - 1)
                "
              >
                <a class="page-link" v-on:click="nextPagina">Next</a>
              </li>
              <li
                class="page-item disabled"
                v-if="
                  paginaActual === numeroPaginas.at(numeroPaginas.length - 1)
                "
              >
                <a class="page-link" v-on:click="nextPagina">Next</a>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import HabitacionRepository from "@/repositories/HabitacionRepository";
import HabitacionCard from "@/components/HabitacionCard.vue";
import ColumnaFiltros from "@/components/ColumnaFiltros.vue";
import ColumnaOrdenacion from "@/components/ColumnaOrdenacion";
import BusquedaInicial from "@/components/BusquedaInicial";

export default {
  components: {
    BusquedaInicial,
    HabitacionCard,
    ColumnaFiltros,
    ColumnaOrdenacion,
  },
  name: "ListaHabitaciones",
  data() {
    return {
      paginaActual: 0,
      habitaciones: [],
      campus: null,
      copiaFiltros: JSON.parse(localStorage.getItem("filtros")),
      numeroPaginas: [],
      habitacionesPaginadas: [],
      habitacionesPorPagina: 10, // se cambia para indicar el numero de anuncios por pagina
      provinciaInicial: null,
      municipio: null,
      campusInicial: null,
      copiaOrden: null,
      mostrarBusquedaInicial: localStorage.getItem("busquedaInicial"),
    };
  },
  computed: {
    isBusquedaInicialUsed() {
      return localStorage.getItem("busquedaInicial") === "true";
    },
  },
  mounted() {
    this.getHabitaciones();
  },
  methods: {
    volverABusquedaInicial() {
      localStorage.setItem("busquedaInicial", "true");
      this.mostrarBusquedaInicial = localStorage.getItem("busquedaInicial");
    },
    busquedaInicial(busquedaInicial) {
      this.provinciaInicial = busquedaInicial.provincia;
      if (busquedaInicial.tipo === "campus") {
        this.campusInicial = busquedaInicial.nombre;
      }
      if (busquedaInicial.tipo === "municipio") {
        this.municipio = busquedaInicial.nombre;
      }
      localStorage.setItem("busquedaInicial", "false");
      this.mostrarBusquedaInicial = localStorage.getItem("busquedaInicial");
    },
    async getHabitaciones() {
      if (this.copiaFiltros !== null) {
        await this.filtros(this.copiaFiltros);
      }
      await this.orden("Mas recientes");
    },
    pasarPagina(numero_pagina) {
      this.paginaActual = numero_pagina;
      this.habitacionesPaginadas = [];
      if (this.habitaciones.length < this.habitacionesPorPagina) {
        this.habitacionesPaginadas = this.habitaciones;
      } else if (
        numero_pagina ===
          this.numeroPaginas.at(this.numeroPaginas.length - 1) &&
        this.habitaciones.length % this.habitacionesPorPagina !== 0
      ) {
        for (
          let x = this.habitaciones.length - 1;
          x >
          this.habitaciones.length -
            1 -
            (this.habitaciones.length % this.habitacionesPorPagina);
          x--
        ) {
          this.habitacionesPaginadas.push(this.habitaciones.at(x));
        }
        this.paginaActual = numero_pagina;
      } else {
        for (
          let i =
            numero_pagina * this.habitacionesPorPagina -
            this.habitacionesPorPagina;
          i < numero_pagina * this.habitacionesPorPagina;
          i++
        ) {
          this.habitacionesPaginadas.push(this.habitaciones.at(i));
        }
      }
      this.paginaActual = numero_pagina;
      window.scrollTo(0, 0);
    },
    nextPagina() {
      this.pasarPagina(this.paginaActual + 1);
    },
    previousPagina() {
      this.pasarPagina(this.paginaActual - 1);
    },

    async filtros(filtros) {
      let filtrosToJSON = JSON.stringify(filtros);
      localStorage.setItem("filtros", filtrosToJSON);
      this.copiaFiltros = JSON.parse(localStorage.getItem("filtros"));
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
          this.copiaOrden,
          null,
          filtros.municipio
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
          this.copiaOrden,
          null,
          filtros.municipio
        );
      }
      this.numeroPaginas = [];
      for (
        let i = 0;
        i < this.habitaciones.length / this.habitacionesPorPagina;
        i++
      ) {
        this.numeroPaginas.push(i + 1);
      }
      this.pasarPagina(1);
    },
    async orden(ordenacion) {
      if (this.copiaFiltros !== null && this.copiaFiltros.campus !== null) {
        this.campus = this.copiaFiltros.campus;
        this.habitaciones = await HabitacionRepository.findAllFilter(
          this.copiaFiltros.provincia,
          this.copiaFiltros.distanciaMaxima,
          this.copiaFiltros.campus.id,
          this.copiaFiltros.precioMaximo,
          this.copiaFiltros.numHabitaciones,
          this.copiaFiltros.inicioContrato,
          this.copiaFiltros.finContrato,
          this.copiaFiltros.opciones,
          ordenacion,
          null,
          this.copiaFiltros.municipio
        );
      } else if (
        this.copiaFiltros !== null &&
        this.copiaFiltros.campus === null
      ) {
        this.campus = null;
        this.habitaciones = await HabitacionRepository.findAllFilter(
          this.copiaFiltros.provincia,
          this.copiaFiltros.distanciaMaxima,
          null,
          this.copiaFiltros.precioMaximo,
          this.copiaFiltros.numHabitaciones,
          this.copiaFiltros.inicioContrato,
          this.copiaFiltros.finContrato,
          this.copiaFiltros.opciones,
          ordenacion,
          null,
          this.copiaFiltros.municipio
        );
      } else {
        this.habitaciones = await HabitacionRepository.findAllFilter(
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          null,
          ordenacion,
          null,
          null
        );
      }
      this.numeroPaginas = [];
      for (
        let i = 0;
        i < this.habitaciones.length / this.habitacionesPorPagina;
        i++
      ) {
        this.numeroPaginas.push(i + 1);
      }
      this.pasarPagina(1);
      this.copiaOrden = ordenacion;
    },
  },
};
</script>

<style scoped>
#main {
  display: flex;
}
.page-link.active {
  background-color: green;
  color: white;
}
.page-link {
  border-color: green;
}
div.ocultar {
  display: none;
}
</style>
