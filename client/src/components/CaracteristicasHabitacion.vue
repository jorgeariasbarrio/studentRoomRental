<template>
  <div class="card" style="margin-top: 3%">
    <div class="card-title">
      <h2
        style="float: left; margin-top: 1%; margin-bottom: 1%; margin-right: 0%"
      >
        {{ habitacion.titulo }}
      </h2>
    </div>
    <MapaDetalle
      v-show="isModalVisible"
      @close="closeModal"
      ref="mostrarMapa"
    />
    <div>
      <h6 style="margin-left: 3%; float: left">
        <a href="#" v-on:click="showModal">Ver en el mapa</a>
        <i class="bi bi-geo-alt"></i>
      </h6>
    </div>
    <div class="card-text">
      <div class="row">
        <div class="col">
          <h3>{{ habitacion.precio }}€/mes</h3>
        </div>
        <div
          class="col"
          v-if="habitacion.inicioContrato > new Date().toLocaleDateString()"
        >
          <h6>
            Inicio del contrato:
            {{ new Date(habitacion.inicioContrato).toLocaleDateString() }}
          </h6>
        </div>
        <div class="col" v-else>
          <h6>Inicio del contrato: Inmediato</h6>
        </div>
        <div class="col">
          <h6>
            Creado el
            {{ new Date(habitacion.fechaCreacion).toLocaleDateString() }}
          </h6>
        </div>
      </div>
      <div class="row">
        <div class="col" v-if="habitacion.fechaModificacion !== null"></div>
        <div class="col">
          <h6>
            Fin del contrato:
            {{ new Date(habitacion.finContrato).toLocaleDateString() }}
          </h6>
        </div>
        <div class="col" v-if="habitacion.fechaModificacion !== null">
          <h6>
            Modificado el
            {{ new Date(habitacion.fechaModificacion).toLocaleDateString() }}
          </h6>
        </div>
      </div>
      <div class="card" style="width: 95%; margin-left: 1%; margin-top: 1%">
        <div class="card-title" style="text-align: left; margin-left: 2%">
          <b>Descripción</b>
        </div>
        <p style="word-wrap: break-word; text-align: left; margin-left: 2%">
          {{ habitacion.descripcion }}
        </p>
      </div>
      <br />
      <div class="card" style="width: 50%; float: left; margin-left: 1%">
        <div style="float: left">
          <b>Equipamiento habitación</b>
        </div>
        <div>
          <i style="margin: 5%" class="bi bi-rulers"
            >{{ habitacion.mCuadrados }}m2</i
          >
          <i
            style="margin: 5%"
            v-for="opcion in habitacion.opciones"
            :key="opcion.id"
            :class="opcion.icono"
          >
            {{ opcion.nombre }}
          </i>
        </div>
      </div>
      <div class="card" style="width: 45%; float: right; margin-right: 1%">
        <div style="float: left">
          <b>Normas del piso</b>
        </div>
        <div>
          <i
            style="margin: 5%"
            v-for="opcion in normasPiso"
            :key="opcion.id"
            :class="opcion.icono"
          >
            {{ opcion.nombre }}
          </i>
        </div>
      </div>
      <div
        class="card"
        style="width: 100%; float: left; margin-right: 5%; margin-top: 2%"
      >
        <div style="float: left">
          <b>Equipamiento piso</b>
        </div>
        <div>
          <i style="margin: 3%" class="bi bi-rulers"
            >{{ habitacion.piso?.mCuadrados }}m2</i
          >
          <i style="margin: 3%" class="bi bi-droplet-half"
            >{{ habitacion.piso?.nBanos }} baños</i
          >
          <i style="margin: 3%" class="bi bi-house-door-fill"
            >{{ habitacion.piso?.piso }}º piso</i
          >
          <i style="margin: 3%" class="bi bi-door-open"
            >{{ habitacion.piso?.nHabitaciones }} habitaciones</i
          >
          <i
            style="margin: 3%"
            v-for="opcion in equipamientoPiso"
            :key="opcion.id"
            :class="opcion.icono"
          >
            {{ opcion.nombre }}
          </i>
        </div>
      </div>
      <div
        v-if="reviews.length !== 0"
        class="container"
        style="margin-top: 15%"
      >
        <div>
          <b> Reviews de la habitacion de otros usuarios:</b>
        </div>
        <div v-if="!isNaN(mediaReviews)">
          <h6>Media de las reviews: {{ mediaReviews }}/5</h6>
        </div>
        <div v-for="review in reviews" :key="review.id">
          <div class="card" style="width: 60%; margin-left: 20%">
            <div class="card-text">
              <div class="row">
                <div class="col-3">
                  <h6>{{ review.nombre }}</h6>
                </div>
                <div class="col">
                  <h6>
                    Creado el
                    {{ new Date(review.fechaCreacion).toLocaleDateString() }}
                  </h6>
                </div>
                <div class="col">
                  <h6>Puntuación: {{ review.puntuacion }}/5</h6>
                </div>
              </div>
              <div class="card-text">
                <h6 style="word-wrap: break-word">{{ review.comentario }}</h6>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <br />
    <div class="container" v-if="otrasHabitaciones.length !== 0">
      <b> Otras habitaciones disponibles en el mismo piso:</b>
    </div>
    <div v-for="habitacion in otrasHabitaciones" :key="habitacion.id">
      <HabitacionCard
        :habitacion="habitacion"
        :centro-estudios="null"
      ></HabitacionCard>
    </div>
  </div>
</template>

<script>
import HabitacionCard from "@/components/HabitacionCard";
import MapaDetalle from "@/components/MapaDetalle";
export default {
  name: "CaracteristicasAnuncio",
  components: { HabitacionCard, MapaDetalle },
  props: {
    habitacion: {
      required: true,
    },
    normasPiso: {
      type: Array,
      required: true,
    },
    equipamientoPiso: {
      type: Array,
      required: true,
    },
    reviews: {
      type: Array,
    },
    otrasHabitaciones: {
      type: Array,
    },
    mediaReviews: {
      type: Number,
    },
  },
  data() {
    return {
      isModalVisible: false,
    };
  },
  methods: {
    showModal() {
      this.isModalVisible = true;
      this.$refs.mostrarMapa.emitirResize();
    },
    closeModal() {
      this.isModalVisible = false;
    },
  },
};
</script>

<style scoped></style>
