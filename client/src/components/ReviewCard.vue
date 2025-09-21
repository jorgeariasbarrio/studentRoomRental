<template>
  <div class="card mb-4">
    <div class="row g-0">
      <div class="col-4">
        <img :src="cargarPortada(habitacion)" class="card-img" alt="..." />
      </div>
      <div class="col-8">
        <div class="card-body">
          <h5
            class="card-title"
            style="float: left"
            v-if="habitacion.estadoHabitacion === 'DISPONIBLE'"
          >
            Habitación en {{ habitacion.piso.direccion }}
          </h5>
          <div class="row" v-if="habitacion.estadoHabitacion === 'ALQUILADA'">
            <div class="col">
              <h5 class="card-title" style="float: left">
                Habitación en {{ habitacion.piso.direccion }}
              </h5>
            </div>
            <div class="col">
              <div
                class="card"
                style="background-color: red; color: white"
                v-if="review.completa === false"
              >
                Pendiente de review
              </div>
              <div
                class="card"
                style="background-color: green; color: white"
                v-if="review.completa === true"
              >
                Review completa
              </div>
            </div>
          </div>
          <div class="container" style="margin-top: 10px; float: left">
            <div class="row">
              <div class="col-sm">
                <h3 class="card-text" style="font-weight: bold; float: left">
                  {{ habitacion.precio }}€/mes
                </h3>
              </div>
              <div class="col">
                <p class="card-text" style="margin: 4%; float: right">
                  Reviews: 4.5/5
                </p>
              </div>
            </div>
          </div>
          <div class="container">
            <div class="row" style="margin-top: 5%">
              <div class="col-4">
                <p class="card-text" style="float: left">
                  Habitaciones :{{ habitacionesLibresPiso }}
                </p>
              </div>
              <div class="col">
                <p
                  class="card-text"
                  style="float: right"
                  v-if="
                    new Date(habitacion.inicioContrato).toLocaleDateString() >
                    new Date().toLocaleDateString()
                  "
                >
                  Disponibilidad:
                  {{ new Date(habitacion.inicioContrato).toLocaleDateString() }}
                  - {{ new Date(habitacion.finContrato).toLocaleDateString() }}
                </p>
                <p class="card-text" v-else style="float: right">
                  Disponibilidad: Ahora -
                  {{ new Date(habitacion.finContrato).toLocaleDateString() }}
                </p>
              </div>
            </div>
          </div>
          <div class="container" style="margin-top: 3%">
            <div class="row">
              <div class="col-sm"></div>
              <div class="col-sm">
                <div class="card-text" style="float: right">
                  <small v-if="habitacion.estadoHabitacion === 'ALQUILADA'">
                    Creado el
                    {{
                      new Date(habitacion.fechaCreacion).toLocaleDateString()
                    }}</small
                  >
                  <small
                    class="text-muted"
                    v-if="habitacion.estadoHabitacion === 'DISPONIBLE'"
                  >
                    Creado el
                    {{
                      new Date(habitacion.fechaCreacion).toLocaleDateString()
                    }}</small
                  >
                </div>
              </div>
            </div>
          </div>
          <div style="float: end">
            <button
              class="btn btn-success"
              v-on:click="crearReview"
              v-if="review.completa === false"
            >
              Realizar review
            </button>
            <button
              class="btn btn-secondary"
              v-on:click="crearReview"
              v-if="review.completa === true"
            >
              Modificar review
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getStore } from "@/common/store";
import HabitacionRepository from "@/repositories/HabitacionRepository";
export default {
  name: "AnuncioCard",
  data() {
    return {
      distanciaCEstudios: 0,
      habitacionesLibresPiso: 0,
    };
  },
  props: {
    habitacion: {
      type: Object,
      required: true,
    },
    review: Object,
  },
  mounted() {
    this.habitacionesMismoPiso();
  },
  computed: {
    isLogged() {
      return getStore().state.user.logged;
    },
    loginUser() {
      return getStore().state.user.login;
    },
  },
  methods: {
    cargarPortada(habitacion) {
      if (habitacion.imagenes.length > 0) {
        for (let i = 0; i < habitacion.imagenes.length; i++) {
          let x = habitacion.imagenes[i];
          if (x.portada === true)
            return `http://localhost:8080/api/habitaciones/${this.habitacion.id}/imagenes/${x.idImagen}`;
        }
        // Si no establecemos la portada creando el habitacion, cogemos la primera imagen(en caso de que las tenga)
        return `http://localhost:8080/api/habitaciones/${this.habitacion.id}/imagenes/${habitacion.imagenes[0].idImagen}`;
      }
      return require("@/images/placeHolderHabitacion.png");
    },
    async habitacionesMismoPiso() {
      let habitacionesLibres = await HabitacionRepository.findAll(
        this.habitacion.piso.id
      );
      this.habitacionesLibresPiso = habitacionesLibres.length;
    },
    crearReview() {
      this.$router.push({
        name: "crearReview",
        params: { link: this.review.link },
      });
    },
  },
};
</script>

<style scoped>
img {
  width: 100%;
  height: 220px;
}
.col-md-7.alquilada {
  background-color: darkgray;
}
.card.mb-3.alquilada {
  background-color: darkgray;
}
.card-link {
  color: green;
}
.card.mb-4.alquilada {
  background-color: gray;
}
.card-text.alquilada {
  color: black;
}
</style>
