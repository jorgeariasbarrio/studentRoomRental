<template>
  <div class="container">
    <div>
      <input type="checkbox" v-model="tieneEmail" />
      El usuario alquilado está registrado en la aplicación
    </div>
    <div v-if="tieneEmail === true">
      <h6>
        Introduzca el email del usuario que ha alquilado la habitación (si está
        registrado en la aplicación)
      </h6>
      <div class="form-floating">
        <input
          type="text"
          class="form-control"
          id="emailAlquilado"
          v-model="emailAlquilado"
          style="margin-bottom: 10px"
          :class="{ errorEmail: emailInvalido }"
        />
        <label for="emailAlquilado">Email Usuario Alquilado</label>
      </div>
      <h6 v-if="emailInvalido === true" style="color: red">
        Email inválido, intentelo de nuevo
      </h6>
    </div>
    <h6>
      Link para que el usuario pueda realizar la review. Este link será de un
      único uso.
    </h6>
    <div class="form-floating">
      <input
        type="text"
        class="form-control"
        id="linkReview"
        v-model="linkReview"
        style="margin-bottom: 10px"
        disabled
      />
      <label for="linkReview">Link para review del usuario</label>
    </div>
    <button
      class="btn btn-success"
      type="button"
      v-on:click="confirmarAlquiler"
    >
      Listo!
    </button>
    <button class="btn btn-danger" type="button" v-on:click="cancelarReview">
      Cancelar alquiler
    </button>
  </div>
</template>

<script>
import HabitacionRepository from "@/repositories/HabitacionRepository";
import ReviewRepository from "@/repositories/ReviewRepository";
import { useToast } from "vue-toastification";

export default {
  name: "habitacionAlquilada",
  data() {
    return {
      emailAlquilado: null,
      linkReview: null,
      emailInvalido: false,
      tieneEmail: false,
      linkTerminacion: null,
      reviewCreada: null,
      alquilerCreado: null,
    };
  },
  mounted() {
    this.getLinkReview();
  },
  methods: {
    // Creamos un link para la review de dicha habitación.
    makeRandomString(length) {
      let result = "";
      const characters =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      const charactersLength = characters.length;
      let counter = 0;
      while (counter < length) {
        result += characters.charAt(
          Math.floor(Math.random() * charactersLength)
        );
        counter += 1;
      }
      return result;
    },
    getLinkReview() {
      let randomString = this.makeRandomString(6);
      this.linkTerminacion = randomString;
      this.linkReview = "http://localhost:1234/reviews/" + randomString;
      this.crearReviewInicial();
    },
    async crearReviewInicial() {
      this.alquilerCreado = await HabitacionRepository.crearAlquilerInicial(
        this.$route.params.idHabitacion,
        this.emailAlquilado
      );
      let review = {
        completa: false,
        link: this.linkTerminacion,
        habitacion: await HabitacionRepository.findHabitacion(
          this.$route.params.idHabitacion
        ),
      };
      // Ahora creamos la review inicial para guardar el link de la review.
      this.reviewCreada = await ReviewRepository.crearReviewInicial(review);
    },
    async confirmarAlquiler() {
      const toast = useToast();
      this.emailInvalido = false;
      let emailRegex =
        /^[-\w.%+]{1,64}@(?:[A-Z0-9-]{1,63}\.){1,125}[A-Z]{2,63}$/i;
      if (!emailRegex.test(this.emailAlquilado) && this.tieneEmail === true) {
        this.emailInvalido = true;
        //Falta verificar si el email corresponde a un usuario(no me parece muy seguro)
      } else {
        //Creamos el alquiler
        if (this.emailAlquilado !== null) {
          await ReviewRepository.asignarReviewACorreo(
            this.reviewCreada.id,
            this.emailAlquilado
          );
        }
        toast.info("Se ha marcado la habitación como alquilada!", {
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
        this.$router.push({ name: "misAnuncios" });
      }
    },
    async cancelarReview() {
      await HabitacionRepository.borrarAlquiler(
        this.alquilerCreado.id,
        this.$route.params.idHabitacion
      );
      await ReviewRepository.deleteById(this.reviewCreada.id);
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped>
input.errorEmail {
  background-color: #ffdddd;
}
</style>
