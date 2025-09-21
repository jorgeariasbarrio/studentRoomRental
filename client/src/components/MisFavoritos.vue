<template>
  <h1 style="margin-top: 1%">Mis anuncios favoritos:</h1>
  <h6 v-if="habitacionesFavoritas.length !== 0">
    Numero de favoritos: {{ habitacionesFavoritas.length }}
  </h6>
  <h4 v-if="habitacionesFavoritas.length === 0">
    !No tienes anuncios favoritos en el momento!
  </h4>
  <div
    v-for="habitacion in habitacionesFavoritas"
    :key="habitacion.id"
    style="margin: 10px"
  >
    <HabitacionCard
      :habitacion="habitacion"
      :centro-estudios="null"
      @esFavorito="esAnuncioFavorito(habitacion)"
      style="margin-left: 10%; max-width: 70%"
    ></HabitacionCard>
  </div>
</template>

<script>
import HabitacionCard from "@/components/HabitacionCard";
import UsuarioRepository from "@/repositories/UsuarioRepository";
export default {
  name: "MisFavoritos",
  components: { HabitacionCard },
  data() {
    return {
      habitacionesFavoritas: [],
    };
  },
  mounted() {
    this.getHabitacionesFavoritas();
  },
  methods: {
    async getHabitacionesFavoritas() {
      this.habitacionesFavoritas = await UsuarioRepository.getFavoritos();
    },
    // Esta funcionalidad sirve para borrar los anuncios de la lista de favoritos que
    // quiten de favoritos.
    async esAnuncioFavorito(habitacion, esFavorito) {
      if (esFavorito === false) {
        await this.getHabitacionesFavoritas();
      }
    },
  },
};
</script>

<style scoped></style>
