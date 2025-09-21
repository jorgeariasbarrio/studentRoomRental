<template>
  <div
    class="card mb-4"
    :class="{
      alquilada:
        habitacion.estadoHabitacion === 'ALQUILADA' ||
        habitacion.estadoHabitacion === 'OCULTA',
    }"
  >
    <div class="row g-0">
      <div class="col-4">
        <img :src="cargarPortada(habitacion)" class="card-img" alt="..." />
      </div>
      <div
        class="col-8"
        :class="{
          alquilada:
            habitacion.estadoHabitacion === 'ALQUILADA' ||
            habitacion.estadoHabitacion === 'OCULTA',
        }"
      >
        <div class="card-body">
          <h5
            class="card-title"
            style="float: left"
            v-if="habitacion.estadoHabitacion === 'DISPONIBLE'"
          >
            <a :href="'/' + habitacion.id" style="color: green"
              >Habitación en {{ habitacion.piso.direccion }}</a
            >
          </h5>
          <div
            class="row"
            v-if="
              habitacion.estadoHabitacion === 'ALQUILADA' ||
              habitacion.estadoHabitacion === 'OCULTA'
            "
          >
            <div class="col">
              <h5 class="card-title" style="float: left">
                Habitación en {{ habitacion.piso.direccion }}
              </h5>
            </div>
            <div class="col">
              <div
                v-if="habitacion.estadoHabitacion === 'ALQUILADA'"
                class="card"
                style="background-color: orange"
              >
                ALQUILADA
              </div>
              <div
                v-if="habitacion.estadoHabitacion === 'OCULTA'"
                class="card"
                style="background-color: yellow"
              >
                OCULTA
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
              <div class="col" v-if="mediaReviews !== null">
                <p class="card-text" style="margin: 4%; float: right">
                  Reviews: {{ mediaReviews }}/5
                </p>
              </div>
              <div class="col" v-if="mediaReviews === null">
                <p class="card-text" style="margin: 4%; float: right">
                  Sin reviews
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
              <div class="col-sm">
                <p
                  class="card-text"
                  v-if="centroEstudios !== null"
                  style="float: left"
                >
                  <b class="text-muted"
                    >Distancia al campus:
                    {{ Math.trunc(distanciaCEstudios / 1000) }} km</b
                  >
                </p>
              </div>
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
          <div class="card-footer" style="display: flex; margin-bottom: -3%">
            <div v-on:click="modificarFavorito" style="margin-left: 85%">
              <h4 v-if="esFavorito === true">
                <font-awesome-icon icon="fa-solid fa-heart" />
              </h4>
              <h4 v-if="esFavorito === false">
                <font-awesome-icon icon="fa-regular fa-heart" />
              </h4>
            </div>
            <div
              class="col-1"
              v-if="
                loginUser === habitacion.piso.usuario.login &&
                this.$route.fullPath === '/misAnuncios'
              "
            >
              <h4
                v-on:click="
                  this.$router.push({
                    name: 'modificarHabitacion',
                    params: { id: habitacion.id },
                  })
                "
              >
                <font-awesome-icon icon="fa-solid fa-pen-to-square" />
              </h4>
            </div>
            <div
              class="col"
              v-if="
                loginUser === habitacion.piso.usuario.login &&
                this.$route.fullPath === '/misAnuncios'
              "
            >
              <button
                class="btn btn-close"
                style="background-color: red; margin-right: 60%"
                v-on:click="borrarHabitacion"
              ></button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getStore } from "@/common/store";
import HabitacionRepository from "@/repositories/HabitacionRepository";
import CentrosEstudiosRepository from "@/repositories/CentrosEstudiosRepository";
import UsuarioRepository from "@/repositories/UsuarioRepository";
import ReviewRepository from "@/repositories/ReviewRepository";
export default {
  name: "AnuncioCard",
  data() {
    return {
      distanciaCEstudios: 0,
      esFavorito: false,
      habitacionesLibresPiso: 0,
      mediaReviews: null,
    };
  },
  props: {
    habitacion: {
      type: Object,
      required: true,
    },
    centroEstudios: Object,
  },
  mounted() {
    this.habitacionesMismoPiso();
    this.esAnuncioFavorito();
    this.calcularDistanciaCEstudiosPiso();
    this.getMediaReviews();
  },
  watch: {
    centroEstudios: async function () {
      // var input = document.getElementById("selectMunicipio");
      //input.removeAttribute("disabled");
      await this.calcularDistanciaCEstudiosPiso();
    },
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
    async calcularDistanciaCEstudiosPiso() {
      if (this.centroEstudios !== null || undefined) {
        this.distanciaCEstudios =
          await CentrosEstudiosRepository.calcularDistancia(
            this.centroEstudios.localizacion.coordinates,
            this.habitacion.piso.posicion.coordinates
          );
      }
    },
    async esAnuncioFavorito() {
      if (getStore().state.user.logged === true) {
        this.esFavorito = false;
        let favoritos = [];
        favoritos = await UsuarioRepository.getFavoritos();
        for (let i = 0; i < favoritos.length; i++) {
          if (favoritos[i].id === this.habitacion.id) {
            this.esFavorito = true;
          }
        }
        if (this.esFavorito === false) {
          this.$emit("esFavorito", this.esFavorito);
        }
      }
    },
    async modificarFavorito() {
      if (getStore().state.user.logged === true) {
        await UsuarioRepository.modificarFavorito(this.habitacion.id);
        await this.esAnuncioFavorito();
      } else {
        this.$router.push({ name: "login" });
      }
    },
    async borrarHabitacion() {
      if (confirm("¿Está seguro que quiere borrar la habitacion?") === true) {
        await HabitacionRepository.borrarHabitacion(this.habitacion.id);
        this.$router.go();
      }
    },
    async getMediaReviews() {
      let reviews = await ReviewRepository.getReviewsByHabitacion(
        this.habitacion.id
      );
      let sumaReviews = 0;
      let contadorReview = 0;
      if (reviews.length !== 0) {
        for (let i = 0; i < reviews.length; i++) {
          if (reviews.at(i).completa === true) {
            sumaReviews += reviews.at(i).puntuacion;
            contadorReview += 1;
          }
        }
        if (sumaReviews !== 0) {
          this.mediaReviews = sumaReviews / contadorReview;
        }
      } else {
        this.mediaReviews = null;
      }
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
