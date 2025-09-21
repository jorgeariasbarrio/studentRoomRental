<template>
  <div
    class="card"
    style="width: 50%; margin: 1rem auto"
    v-if="usuarioRegistrado === true"
  >
    <div class="container">
      <h2>Review de {{ review.habitacion.titulo }}</h2>
      <div class="container">
        <div class="row" style="margin-bottom: 5%">
          <div class="col">
            <div class="form-floating">
              <input
                class="form-control"
                id="nombreReview"
                v-model="review.nombre"
              />
              <label for="comentario">Nombre para la review</label>
            </div>
          </div>
          <div class="col">
            <div class="form-floating">
              <select
                id="titulo"
                v-model="review.puntuacion"
                class="form-select"
                :class="{
                  error: errorPuntuacion,
                }"
                style="width: 50%"
              >
                <option value="1.0">1.0</option>
                <option value="1.5">1.5</option>
                <option value="2.0">2.0</option>
                <option value="2.5">2.5</option>
                <option value="3.0">3.0</option>
                <option value="3.5">3.5</option>
                <option value="4.0">4.0</option>
                <option value="4.5">4.5</option>
                <option value="5.0">5.0</option>
              </select>
              <label for="titulo">Puntuación</label>
            </div>
          </div>
          <div v-if="errorPuntuacion === true" style="color: red">
            La puntuación debe ser válida
          </div>
        </div>
      </div>
      <div class="container" style="margin-bottom: 10px">
        <div class="form-floating">
          <textarea
            class="form-control"
            id="comentario"
            v-model="review.comentario"
            style="height: 100px"
          ></textarea>
          <label for="comentario">Cometario</label>
        </div>
      </div>
    </div>
    <div class="btn-group" role="group" style="width: 50%; margin-left: 25%">
      <button class="btn btn-success" v-on:click="actualizarReview">
        Listo!
      </button>
      <button class="btn btn-danger" v-on:click="cancelarActualizacion">
        Cancelar
      </button>
    </div>
  </div>
</template>

<script>
import ReviewRepository from "@/repositories/ReviewRepository";
import AccountRepository from "@/repositories/AccountRepository";
import { useToast } from "vue-toastification";

export default {
  name: "CrearReview",
  data() {
    return {
      review: {
        nombre: null,
        comentario: null,
        puntuacion: null,
        completa: false,
        habitacion: Object,
        usuario: null,
      },
      errorPuntuacion: false,
      usuarioRegistrado: false,
    };
  },
  mounted() {
    this.getReview();
  },
  methods: {
    async getReview() {
      const toast = useToast();
      try {
        this.review = await ReviewRepository.getReviewByLink(
          this.$route.params.link
        );
      } catch (error) {
        if (error.response.status === 401) {
          let tieneCuenta = confirm(
            "Necesitas una cuenta para poder crear la review.¿Tienes una cuenta en esta página?"
          );
          if (tieneCuenta === true) {
            this.$router.push({
              name: "loginReview",
              params: { linkReview: this.$route.params.link },
            });
          } else {
            this.$router.push({
              name: "registerForReview",
              params: { linkReview: this.$route.params.link },
            });
          }
        } else {
          console.log(error);
        }
      }
      this.usuarioRegistrado = true;
      this.review.usuario = await AccountRepository.getAccount();
      if (this.review.usuario.id !== this.review.usuario.id) {
        toast.error("No puedes realizar esta review!", {
          position: "top-right",
          timeout: 10000,
          closeOnClick: true,
          pauseOnFocusLoss: true,
          pauseOnHover: true,
          draggable: true,
          draggablePercent: 0.6,
          showCloseButtonOnHover: false,
          hideProgressBar: true,
          closeButton: "button",
          icon: true,
          rtl: false,
        });
        this.$router.go(-1);
      }
    },
    async actualizarReview() {
      const toast = useToast();
      if (this.review.puntuacion <= 0) {
        this.errorPuntuacion = true;
        toast.error("Indique una puntuación válida para la review!", {
          position: "top-right",
          timeout: 10000,
          closeOnClick: true,
          pauseOnFocusLoss: true,
          pauseOnHover: true,
          draggable: true,
          draggablePercent: 0.6,
          showCloseButtonOnHover: false,
          hideProgressBar: true,
          closeButton: "button",
          icon: true,
          rtl: false,
        });
        window.scrollTo(0, 0);
      } else {
        let mensaje;
        if (this.review.completa === true) {
          mensaje = "Review modificada correctamente!";
        } else {
          mensaje = "Review creada correctamente!";
        }
        this.review.completa = true;
        await ReviewRepository.updateReview(this.review);
        toast.success(mensaje, {
          position: "top-right",
          timeout: 5000,
          closeOnClick: true,
          pauseOnFocusLoss: true,
          pauseOnHover: true,
          draggable: true,
          draggablePercent: 0.6,
          showCloseButtonOnHover: false,
          hideProgressBar: true,
          closeButton: "button",
          icon: true,
          rtl: false,
        });
        this.$router.push({ name: "miPerfil" });
      }
    },
    async cancelarActualizacion() {
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped></style>
