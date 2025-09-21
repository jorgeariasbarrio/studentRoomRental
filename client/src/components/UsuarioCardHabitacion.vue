<template>
  <div class="card mb-2 w-75">
    <div class="col-md-18">
      <h5 class="card-title">
        <router-link class="card-link" :to="'/usuario/' + usuario.id">
          {{ usuario.nombre }}
        </router-link>
      </h5>
      <div class="card-body">
        <p class="card-text">
          <small class="text-muted">{{ usuario.telefono }}</small>
        </p>
        <p class="card-text">
          <small class="text-muted">{{ usuario.email }}</small>
        </p>
        <p class="card-text" v-if="!isNaN(mediaReviewsUsuario)">
          <small class="text-muted"
            >Media de reviews: {{ mediaReviewsUsuario }}/5</small
          >
        </p>
        <p class="card-text" v-else>
          <small class="text-muted">Sin reviews</small>
        </p>
        <router-link :to="'/usuario/' + usuario.id"
          >Habitaciones en alquiler: {{ otrasHabitaciones }}</router-link
        >
      </div>
    </div>
  </div>
</template>

<script>
import { getStore } from "@/common/store";

export default {
  name: "UsuarioCardHabitacion",
  props: {
    usuario: {
      required: true,
    },
    mediaReviewsUsuario: {
      type: Number,
    },
    otrasHabitaciones: {
      type: Number,
    },
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
  },
};
</script>

<style scoped>
.col-md-7 {
  align-content: center;
}
</style>
