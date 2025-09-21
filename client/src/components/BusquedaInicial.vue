<template>
  <h2 style="margin-top: 3%">Busca habitación</h2>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
  <label for="exampleDataList" class="form-label"
    >¿En donde quieres encontrar habitación?</label
  >
  <div style="width: 60%; margin-left: 20%">
    <div>
      <VueMultiselect
        v-model="seleccionado"
        :options="listaBusquedas"
        :close-on-select="true"
        :clear-on-select="true"
        :custom-label="nameWithLang"
        placeholder="Elija una opción"
      >
      </VueMultiselect>
    </div>
  </div>
  <button
    class="btn btn-success"
    style="margin-top: 3%; width: 10%"
    v-on:click="realizarBusqueda"
  >
    Buscar
  </button>
</template>

<script>
import CentrosEstudiosRepository from "@/repositories/CentrosEstudiosRepository";
import PisoRepository from "@/repositories/PisoRepository";
import VueMultiselect from "vue-multiselect";
import "vue-multiselect/dist/vue-multiselect.css";

export default {
  name: "BusquedaInicial",
  components: { VueMultiselect },
  emits: ["busquedaInicial"],
  data() {
    return {
      listaBusquedas: [],
      provincias: [],
      campus: null,
      seleccionado: null,
    };
  },
  mounted() {
    this.getBusquedas();
  },
  methods: {
    nameWithLang({ provincia, nombre }) {
      return `${provincia} - ${nombre}`;
    },
    async getBusquedas() {
      if (localStorage.getItem("busquedaInicial") === null) {
        localStorage.setItem("busquedaInicial", "true");
      }
      let allProvincias = await PisoRepository.getProvincias();
      for (let i = 0; i < allProvincias.length; i++) {
        if (this.provincias.includes(allProvincias[i].provincia) === false) {
          this.provincias.push(allProvincias[i].provincia);
        }
      }
      for (let i = 0; i < this.provincias.length; i++) {
        let municipios = await PisoRepository.getMunicipios(
          this.provincias.at(i)
        );
        for (let x = 0; x < municipios.length; x++) {
          let obj = {
            provincia: this.provincias.at(i),
            nombre: municipios.at(x).municipio,
            tipo: "municipio",
          };
          this.listaBusquedas.push(obj);
        }
      }
      let campusPorProvincia = await CentrosEstudiosRepository.findAll();
      for (let z = 0; z < campusPorProvincia.length; z++) {
        let obj = {
          provincia: campusPorProvincia.at(z).provincia,
          nombre: campusPorProvincia.at(z).nombre,
          tipo: "campus",
        };
        this.listaBusquedas.push(obj);
      }
    },
    realizarBusqueda() {
      this.$emit("busquedaInicial", this.seleccionado);
    },
  },
};
</script>

<style scoped src="vue-multiselect/dist/vue-multiselect.css"></style>
