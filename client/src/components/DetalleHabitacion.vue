<template>
  <div v-if="empty">
    <h3>No existe el anuncio con ID {{ this.$route.params.id }}</h3>
    <router-link class="card-link" :to="'/'"> Ver habitaciones </router-link>
  </div>
  <div class="imagen-usuario" id="imagen-usuario" v-if="noimage === true">
    <div id="carouselExampleControls">
      <img
        src="../images/placeHolderHabitacion.png"
        class="d-block w-100"
        alt="foto"
        style="width: 600px; height: 400px; max-width: 600px; max-height: 400px"
      />
    </div>
    <div id="usuario" class="usuario">
      <UsuarioCardHabitacion
        :usuario="usuario"
        :media-reviews-usuario="mediaReviewsUsuario"
        :otras-habitaciones="otrasHabitacionesUsuario"
      ></UsuarioCardHabitacion>
    </div>
  </div>
  <div class="imagen-usuario" id="imagen-usuario" v-if="noimage === false">
    <div
      id="carouselExampleControls"
      class="carousel slide"
      data-bs-ride="carousel"
      data-interval="false"
    >
      <div class="carousel-inner">
        <div
          v-for="(link, idx) in imagenesLink"
          :key="link"
          class="carousel-item"
          :class="{ active: idx === 0 }"
        >
          <img :src="link" class="d-block w-100" alt="foto" />
        </div>
      </div>
      <button
        class="carousel-control-prev"
        type="button"
        data-bs-target="#carouselExampleControls"
        data-bs-slide="prev"
      >
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button
        class="carousel-control-next"
        type="button"
        data-bs-target="#carouselExampleControls"
        data-bs-slide="next"
      >
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>
    <div id="usuario" class="usuario">
      <UsuarioCardHabitacion
        :usuario="usuario"
        :media-reviews-usuario="mediaReviewsUsuario"
        :otras-habitaciones="otrasHabitacionesUsuario"
      ></UsuarioCardHabitacion>
    </div>
  </div>
  <div>
    <div
      id="caracteristicas"
      class="characteristics"
      style="width: 80%; margin-left: 8%"
    >
      <CaracteristicasHabitacion
        :habitacion="habitacion"
        :equipamiento-piso="equipamientoPiso"
        :normas-piso="normasPiso"
        :reviews="reviews"
        :otras-habitaciones="otrasHabitaciones"
        :media-reviews="mediaReviews"
      ></CaracteristicasHabitacion>
    </div>
  </div>
</template>

<script>
import UsuarioCardHabitacion from "@/components/UsuarioCardHabitacion";
import CaracteristicasHabitacion from "@/components/CaracteristicasHabitacion";
import HabitacionRepository from "@/repositories/HabitacionRepository";
import ReviewRepository from "@/repositories/ReviewRepository";
import PisoRepository from "@/repositories/PisoRepository";

export default {
  name: "AnuncioDetalle",

  data() {
    return {
      habitacion: Object,
      usuario: Object,
      imagenes: [],
      imagenesLink: [],
      empty: false,
      noimage: false,
      normasPiso: [],
      equipamientoPiso: [],
      reviews: [],
      otrasHabitaciones: [],
      otrasHabitacionesUsuario: null,
      mediaReviews: null,
      mediaReviewsUsuario: null,
    };
  },
  components: { CaracteristicasHabitacion, UsuarioCardHabitacion },
  mounted() {
    this.getHabitacion();
  },

  methods: {
    async getHabitacion() {
      this.habitacion = await HabitacionRepository.findHabitacion(
        this.$route.params.idHabitacion
      );
      this.usuario = this.habitacion.piso.usuario;
      await this.getImagenes();
    },
    async getImagenes() {
      try {
        this.imagenes = await HabitacionRepository.cargarImagenes(
          this.$route.params.idHabitacion
        );
        if (this.imagenes.length === 0) {
          this.noimage = true;
        } else {
          for (let i = 0; i < this.imagenes.length; i++) {
            this.imagenesLink.push(
              "http://localhost:8080/api/habitaciones/" +
                this.$route.params.idHabitacion +
                "/imagenes/" +
                this.imagenes[i].idImagen
            );
          }
        }
      } catch (e) {
        console.log(e);
      }
      this.dividirOpcionesPiso();
    },
    dividirOpcionesPiso() {
      for (let i = 0; i < this.habitacion.piso.opciones.length; i++) {
        let opcion = this.habitacion.piso.opciones?.at(i);
        if (
          opcion.discriminante === "GENERO" ||
          opcion.discriminante === "NORMAS"
        ) {
          this.normasPiso.push(opcion);
        } else {
          this.equipamientoPiso.push(opcion);
        }
      }
      this.getReviewsHabitacion();
    },
    async getReviewsHabitacion() {
      this.reviews = await ReviewRepository.getReviewsByHabitacion(
        this.habitacion.id
      );
      let sumaReviews = 0;
      for (let i = 0; i < this.reviews.length; i++) {
        sumaReviews += this.reviews.at(i).puntuacion;
      }
      this.mediaReviews = sumaReviews / this.reviews.length;
      await this.getOtrasHabitaciones();
    },
    async getOtrasHabitaciones() {
      let habitaciones = await HabitacionRepository.findAll(
        this.habitacion.piso.id
      );
      for (let i = 0; i < habitaciones.length; i++) {
        if (habitaciones.at(i).id !== this.habitacion.id) {
          this.otrasHabitaciones.push(habitaciones.at(i));
        }
      }
      await this.getMediaReviewsUsuario();
    },
    async getMediaReviewsUsuario() {
      let reviewsUsuario = await ReviewRepository.getReviewsByUser(
        this.habitacion.piso.usuario.id
      );
      let sumaReviews = 0;
      for (let i = 0; i < reviewsUsuario.length; i++) {
        sumaReviews += reviewsUsuario.at(i).puntuacion;
      }
      this.mediaReviewsUsuario = sumaReviews / reviewsUsuario.length;
      await this.getOtrasHabitacionesUsuario();
    },
    async getOtrasHabitacionesUsuario() {
      let pisos = await PisoRepository.getPisosUsuario(
        this.habitacion.piso.usuario.id
      );
      for (let x = 0; x < pisos.length; x++) {
        for (let i = 0; i < pisos.at(x).habitaciones.length; i++) {
          this.otrasHabitacionesUsuario += 1;
        }
      }
    },
  },
};
</script>

<style scoped>
#carouselExampleControls {
  margin-top: 40px;
  margin-left: 40px;
  width: 600px;
  height: 400px;
  max-width: 600px;
  max-height: 400px;
  background: no-repeat center center scroll;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
  float: left;
  position: relative;
}

.carousel-inner img {
  width: 600px;
  height: 400px;
  max-width: 600px;
  max-height: 400px;
}

#usuario {
  width: 40%;
  height: 50%;
  margin-top: 8%;
  float: left;
  margin-left: 7%;
}

#usuario-sin-imagenes {
  width: 40%;
  height: 50%;
  margin-top: 2%;
  margin-inside: 20%;
  float: left;
  margin-left: 35%;
  position: relative;
}

#caracteristicas {
  clear: both;
}
</style>
