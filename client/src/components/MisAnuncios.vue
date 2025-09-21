<template>
  <h1 style="margin-top: 1%">Mis anuncios</h1>
  <h6>{{ pisos.length }} pisos disponibles</h6>
  <div v-for="piso in pisos" :key="piso.id" class="container">
    <div class="row">
      <div class="col-6">
        <h3 style="font-weight: bold; padding-right: 40%">
          Piso en {{ piso.direccion }}
        </h3>
      </div>
      <div class="col-2">
        <button class="btn btn-dark" v-on:click="editarPiso(piso.id)">
          Editar Piso
        </button>
      </div>
      <div class="col-2">
        <button
          class="btn btn-success"
          v-on:click="anadirHabitacion(piso.id, piso)"
        >
          Añadir Habitacion
        </button>
      </div>
    </div>
    <div v-if="piso.habitaciones.length === 0" style="margin: 5%">
      <h3>No hay habitaciones en alquiler para este piso</h3>
    </div>
    <div
      v-for="habitacion in piso.habitaciones"
      :key="habitacion.id"
      id="anuncios"
    >
      <HabitacionCard
        :habitacion="habitacion"
        :centro-estudios="null"
        style="margin-left: 5%; max-width: 80%"
      ></HabitacionCard>
    </div>
  </div>
</template>

<script>
import PisoRepository from "@/repositories/PisoRepository";
import AccountRepository from "@/repositories/AccountRepository";
import HabitacionCard from "@/components/HabitacionCard";
import HabitacionRepository from "@/repositories/HabitacionRepository";
export default {
  name: "MisAnuncios",
  components: { HabitacionCard },
  data() {
    return {
      pisos: [],
    };
  },
  mounted() {
    this.getPisos();
  },
  methods: {
    async getPisos() {
      let usuario = await AccountRepository.getAccount();
      this.pisos = await PisoRepository.getPisosUsuario(usuario.id);
    },
    async editarPiso(idPiso) {
      this.$router.push({ name: "editarPiso", params: { idPiso: idPiso } });
    },
    async anadirHabitacion(idPiso, piso) {
      let habitacionesPiso = await HabitacionRepository.findAll(idPiso);
      if (habitacionesPiso.length === piso.nHabitaciones) {
        if (
          confirm(
            "Este piso no tiene más habitaciones libres. Quieres modificar el número de habitaciones?"
          ) === true
        ) {
          this.$router.push({
            name: "editarPiso",
            params: { idPiso: piso.id },
          });
        }
      } else {
        this.$router.push({
          name: "anadirHabitacion",
          params: { idPiso: idPiso },
        });
      }
    },
  },
};
</script>

<style scoped></style>
