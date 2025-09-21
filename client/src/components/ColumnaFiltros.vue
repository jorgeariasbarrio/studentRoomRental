<template>
  <div class="card w-100 p-3" style="margin-top: 10px">
    <h3>Filtros</h3>
    <form aria-label="Búsqueda">
      <div class="form-floating">
        <select
          v-model="provincia"
          class="form-select"
          id="filtro-ciudad"
          style="margin-bottom: 10px"
        >
          <option
            v-for="provincia in this.provincias"
            :key="provincia"
            :value="provincia"
          >
            {{ provincia }}
          </option>
        </select>
        <label for="filtro-ciudad">Provincia</label>
      </div>
      <div class="form-floating">
        <select
          v-model="municipio"
          class="form-select"
          id="filtro-ciudad"
          style="margin-bottom: 10px"
          :disabled="isDisabledCampus"
        >
          <option
            v-for="municipio in this.municipios"
            :key="municipio"
            :value="municipio.municipio"
          >
            {{ municipio.municipio }}
          </option>
        </select>
        <label for="filtro-ciudad">Municipio</label>
      </div>
      <div class="form-floating">
        <select
          v-model="campusSeleccionado"
          class="form-select"
          id="filtro-campus"
          style="margin-bottom: 10px"
          :disabled="isDisabledCampus"
        >
          <option v-for="campus in campus" :key="campus" :value="campus">
            {{ campus.nombre }}
          </option>
        </select>
        <label for="filtro-campus">Campus</label>
      </div>
      <div class="form-floating">
        <input
          type="number"
          class="form-control"
          id="filtro-distancia"
          aria-label="Mínimo"
          v-model="filtrosReturn.distanciaMaxima"
          style="margin-bottom: 10px"
          :disabled="isDisabledDistancia"
        />
        <label for="filtro-distancia">Distancia máxima</label>
      </div>
      <div class="form-floating">
        <input
          type="number"
          class="form-control"
          id="filtro-precio"
          aria-label="De"
          v-model="filtrosReturn.precioMaximo"
          style="margin-bottom: 10px"
        />
        <label for="filtro-precio">Precio Máximo</label>
      </div>
      <div class="form-floating">
        <input
          type="number"
          class="form-control"
          id="habitaciones"
          aria-label="De"
          v-model="filtrosReturn.numHabitaciones"
          style="margin-bottom: 10px"
        />
        <label for="habitaciones">Habitaciones disponibles</label>
      </div>
      <div class="form-floating">
        <input
          type="date"
          class="form-control"
          id="inicioContrato"
          aria-label="De"
          v-model="filtrosReturn.inicioContrato"
          style="margin-bottom: 10px"
        />
        <label for="inicioContrato">Disponible desde:</label>
      </div>
      <div class="form-floating">
        <input
          type="date"
          class="form-control"
          id="finContrato"
          aria-label="De"
          v-model="filtrosReturn.finContrato"
          style="margin-bottom: 10px"
        />
        <label for="inicioContrato">Disponible hasta:</label>
      </div>
      <div class="container">
        <b>Convivencia:</b>
        <div
          class="form-check"
          v-for="opcion in generos"
          :key="opcion.norma.id"
        >
          <input
            class="form-check-input"
            type="checkbox"
            v-model="opcion.seleccionada"
            id="generosCheckBox"
          />
          <label
            for="generosCheckBox"
            class="form-check-label"
            style="float: left"
          >
            {{ opcion.norma.nombre }}
          </label>
        </div>
      </div>
      <div class="container">
        <div class="form-check" v-for="opcion in normas" :key="opcion.norma.id">
          <input
            class="form-check-input"
            type="checkbox"
            v-model="opcion.seleccionada"
            :id="opcion.norma.nombre"
          />
          <label
            class="form-check-label"
            :for="opcion.norma.nombre"
            style="float: left"
          >
            {{ opcion.norma.nombre }}
          </label>
        </div>
      </div>
      <br />
      <button
        v-on:click="pasarParametrosFiltro"
        type="button"
        class="btn btn-success"
      >
        Buscar
      </button>
      <button type="button" class="btn btn-danger" v-on:click="borrarFiltros">
        Borrar Filtros
      </button>
    </form>
  </div>
</template>

<script>
import PisoRepository from "@/repositories/PisoRepository";
import CentrosEstudiosRepository from "@/repositories/CentrosEstudiosRepository";
import { getStore } from "@/common/store";

export default {
  name: "FilterColumn",

  data() {
    return {
      provincias: [],
      municipios: [],
      campus: [],
      campusSeleccionado: null,
      campusObjeto: [],
      normas: [],
      generos: [],
      provincia: null,
      municipio: null,
      // Inicializamos las variables con el valor almacenado en localStorage.
      filtrosReturn: {
        provincia: null,
        municipio: null,
        campus: null,
        distanciaMaxima: null,
        precioMaximo: null,
        numHabitaciones: null,
        inicioContrato: null,
        finContrato: null,
        opciones: [],
      },
    };
  },
  props: {
    campusInicial: String,
    provinciaInicial: String,
    municipioInicial: String,
  },
  mounted() {
    this.getProvincias();
  },
  watch: {
    provincia: async function (value) {
      this.municipios = await PisoRepository.getMunicipios(value);
      this.hayProvinciaSeleccionada = true;
      this.campus = await CentrosEstudiosRepository.findAll(value);
    },
    campusSeleccionado: async function () {
      this.hayCampusSeleccionado = true;
    },
    campusInicial: async function (value) {
      this.campus = await CentrosEstudiosRepository.findAll(
        this.provinciaInicial
      );
      for (let i = 0; i < this.campus.length; i++) {
        if (this.campus.at(i).nombre === value) {
          this.campusSeleccionado = this.campus.at(i);
        }
      }
      this.pasarParametrosFiltro();
    },
    provinciaInicial: function (value) {
      this.provincia = value;
      this.pasarParametrosFiltro();
    },
    municipioInicial: function (value) {
      this.municipio = value;
      this.pasarParametrosFiltro();
    },
  },
  computed: {
    isDisabledCampus() {
      return this.provincia === null || this.provinciaInicial === "";
    },
    isDisabledDistancia() {
      return this.campusSeleccionado === null || this.campusInicial === "";
    },
  },
  methods: {
    async getProvincias() {
      let provinciasYMunicipios = await PisoRepository.getProvincias();

      for (let i = 0; i < provinciasYMunicipios.length; i++) {
        if (
          this.provincias.includes(provinciasYMunicipios[i].provincia) === false
        ) {
          this.provincias.push(provinciasYMunicipios[i].provincia);
        }
      }
      await this.getOpcionesFiltro();
    },
    async getOpcionesFiltro() {
      let allNormas = await PisoRepository.getOpcionesPisoNormas();
      for (let i = 0; i < allNormas.length; i++) {
        let obj = {
          norma: allNormas[i],
          seleccionada: false,
        };
        this.normas.push(obj);
      }
      await this.getGenerosFiltro();
    },
    async getGenerosFiltro() {
      let allGeneros = await PisoRepository.getOpcionesPisoGenero();
      for (let i = 0; i < allGeneros.length; i++) {
        let obj = {
          norma: allGeneros[i],
          seleccionada: false,
        };
        this.generos.push(obj);
      }
      await this.getLocalStorageFiltros();
    },
    async getLocalStorageFiltros() {
      if (localStorage.getItem("filtros") !== null) {
        if (JSON.parse(localStorage.getItem("filtros")).campus) {
          this.campusSeleccionado = JSON.parse(
            localStorage.getItem("filtros")
          ).campus;
        }
        if (JSON.parse(localStorage.getItem("filtros")).provincia) {
          this.provincia = JSON.parse(
            localStorage.getItem("filtros")
          ).provincia;
        }
        if (JSON.parse(localStorage.getItem("filtros")).municipio) {
          this.municipio = JSON.parse(
            localStorage.getItem("filtros")
          ).municipio;
        }
        if (JSON.parse(localStorage.getItem("filtros")).distanciaMaxima) {
          this.filtrosReturn.distanciaMaxima = JSON.parse(
            localStorage.getItem("filtros")
          ).distanciaMaxima;
        }
        if (
          (this.filtrosReturn.precioMaximo = JSON.parse(
            localStorage.getItem("filtros")
          ).precioMaximo)
        ) {
          this.filtrosReturn.precioMaximo = JSON.parse(
            localStorage.getItem("filtros")
          ).precioMaximo;
        }
        if (JSON.parse(localStorage.getItem("filtros")).numHabitaciones) {
          this.filtrosReturn.numHabitaciones = JSON.parse(
            localStorage.getItem("filtros")
          ).numHabitaciones;
        }
        if (JSON.parse(localStorage.getItem("filtros")).inicioContrato) {
          this.filtrosReturn.inicioContrato = JSON.parse(
            localStorage.getItem("filtros")
          ).inicioContrato;
        }
        if (JSON.parse(localStorage.getItem("filtros")).finContrato) {
          this.filtrosReturn.finContrato = JSON.parse(
            localStorage.getItem("filtros")
          ).finContrato;
        }
        if (JSON.parse(localStorage.getItem("filtros")).opciones) {
          let opcionesMarcadas = JSON.parse(
            localStorage.getItem("filtros")
          ).opciones;
          for (let i = 0; i < this.normas.length; i++) {
            for (let j = 0; j < opcionesMarcadas.length; j++) {
              if (this.normas[i].norma.id === opcionesMarcadas[j]) {
                this.normas[i].seleccionada = true;
              }
            }
          }
          for (let i = 0; i < this.generos.length; i++) {
            for (let j = 0; j < opcionesMarcadas.length; j++) {
              if (this.generos[i].norma.id === opcionesMarcadas[j]) {
                this.generos[i].seleccionada = true;
              }
            }
          }
        }
      }
    },
    pasarParametrosFiltro() {
      this.filtrosReturn.opciones = [];
      for (let i = 0; i < this.normas.length; i++) {
        if (this.normas[i].seleccionada === true) {
          this.filtrosReturn.opciones.push(this.normas[i].norma.id);
        }
      }
      for (let i = 0; i < this.generos.length; i++) {
        if (this.generos[i].seleccionada === true) {
          this.filtrosReturn.opciones.push(this.generos[i].norma.id);
        }
      }
      this.filtrosReturn.provincia = this.provincia;
      this.filtrosReturn.municipio = this.municipio;
      this.filtrosReturn.campus = this.campusSeleccionado;
      getStore().state.storeFiltros = this.filtrosReturn;
      this.$emit("filtros", this.filtrosReturn);
      window.scrollTo(0, 0);
    },
    borrarFiltros() {
      this.provincia = null;
      this.municipio = null;
      this.campusSeleccionado = null;
      this.filtrosReturn.campus = null;
      this.filtrosReturn.provincia = "";
      this.filtrosReturn.municipio = "";
      this.filtrosReturn.precioMaximo = "";
      this.filtrosReturn.distanciaMaxima = "";
      this.filtrosReturn.numHabitaciones = "";
      this.filtrosReturn.inicioContrato = null;
      this.filtrosReturn.finContrato = null;
      this.filtrosReturn.opciones = [];
      for (let i = 0; i < this.normas.length; i++) {
        this.normas.at(i).seleccionada = false;
      }
      for (let i = 0; i < this.generos.length; i++) {
        this.generos.at(i).seleccionada = false;
      }
      this.$emit("filtros", this.filtrosReturn);
      window.scrollTo(0, 0);
    },
  },
};
</script>

<style scoped></style>
