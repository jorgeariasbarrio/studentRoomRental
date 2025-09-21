<template>
  <div class="card" style="width: 60rem">
    <h2>Imagenes Piso</h2>
    <div>
      <button
        class="btn btn-secondary"
        @click.prevent="iniciarSubidaFicheroPiso()"
      >
        Elegir imagen
      </button>
    </div>
    <input
      ref="inputOcultoPiso"
      type="file"
      class="d-none"
      multiple
      @change="actualizarSubidaFicheroPiso()"
    />
    <div class="row">
      <div class="col" v-for="imagen in imagenesPisoFR" :key="imagen.imagenFR">
        <button
          class="btn btn-close"
          style="background-color: red"
          v-on:click="borrarImagen(imagen, imagenesPisoFR)"
        ></button>
        <img :src="imagen.imagenFR" width="200" height="200" />
      </div>
    </div>
  </div>
  <div
    class="card"
    style="width: 60rem"
    v-for="(habitacion, index) in habitaciones"
    :key="habitacion"
  >
    <h2>Imagenes Habitacion {{ index + 1 }}</h2>
    <div>
      <button
        class="btn btn-secondary"
        @click.prevent="iniciarSubidaFicheroHabitacionGenerico(index)"
      >
        Elegir imagen
      </button>
    </div>
    <input
      :ref="'inputOcultoHabitacion' + index.toString()"
      type="file"
      class="d-none"
      multiple
      @change="actualizarSubidaFicheroHabitacion(index)"
    />
    <div class="row">
      <div
        class="col"
        v-for="imagen in habitacionesCopy[index].imagenes"
        :key="imagen.imagenFR"
      >
        <button
          class="btn btn-close"
          style="background-color: red; position: absolute"
          v-on:click="borrarImagen(imagen, habitacionesCopy[index].imagenes)"
        ></button>
        <img
          :src="imagen.imagenFR"
          :class="{ selected: imagen.portada }"
          width="200"
          height="200"
          v-on:click="esPortada(imagen, habitacionesCopy[index].imagenes)"
        />
        <h6 v-if="imagen.portada === true" style="color: red">Portada</h6>
      </div>
    </div>
  </div>
  <button class="btn btn-secondary" v-on:click="mostrarHabitaciones">
    Volver a habitaciones
  </button>
  <button class="btn btn-success" v-on:click="FinalizarFormulario">
    Finalizar
  </button>
</template>

<script>
export default {
  name: "FormularioImagenes",
  props: {
    habitacionesLibres: Number,
    habitaciones: Array,
  },
  emits: ["volverHabitaciones", "finalizar"],
  watch: {
    habitacionesLibres: function (value) {
      this.habitacionesLibresWatch = value;
    },
    habitaciones: function (value) {
      this.habitacionesCopy = value;
    },
  },
  data() {
    return {
      habitacionesCopy: [],
      habitacionesLibresWatch: 0,
      imagenesPisoFR: [],
      imagenesHabitacion: [],
      imagenesHabitacion1FR: [],
      imagenesHabitacion2FR: [],
      imagenesHabitacion3FR: [],
      imagenesHabitacion4FR: [],
      imagenesHabitacion5FR: [],
    };
  },
  methods: {
    mostrarHabitaciones() {
      this.$emit("volverHabitaciones");
    },
    iniciarSubidaFicheroPiso() {
      this.$refs.inputOcultoPiso.click();
    },
    actualizarSubidaFicheroPiso() {
      for (let i = 0; i < this.$refs.inputOcultoPiso.files.length; i++) {
        const reader = new FileReader();
        reader.readAsDataURL(this.$refs.inputOcultoPiso.files[i]);
        reader.addEventListener("load", (e) => {
          //Tuve que crear un objecto nuevo, si no la portada se seleccionaba en todas las habitaciones
          let obj = {
            imagenFR: e.target.result, // imagen en formato base64 para img en html
            imagenFile: this.$refs.inputOcultoPiso.files[i], // archivo imagen
            portada: false, // si va ser la portada
          };
          //Solo insertamos las imagenes en el array de las del piso, no creo que tenga
          //sentido poder seleccionar una imagen del piso como portada.
          this.imagenesPisoFR.push(obj);
        });
      }
    },
    //Cuando recuperamos $refs[...], nos recupera un array, por lo que tenemos que recuperar siempre el input en la posicion 0 del array
    iniciarSubidaFicheroHabitacionGenerico(index) {
      let nombreInput = "inputOcultoHabitacion" + index.toString();
      this.$refs[nombreInput][0].click();
    },
    actualizarSubidaFicheroHabitacion(index) {
      let nombreInput = "inputOcultoHabitacion" + index.toString();
      for (let i = 0; i < this.$refs[nombreInput][0].files.length; i++) {
        const reader = new FileReader();
        reader.readAsDataURL(this.$refs[nombreInput][0].files[i]);
        reader.addEventListener("load", (e) => {
          let obj = {
            imagenFR: e.target.result, // imagen en formato base64 para img en html
            imagenFile: this.$refs[nombreInput][0].files[i], // archivo imagen
            portada: false, // si va ser la portada
          };
          this.habitacionesCopy[index].imagenes.push(obj);
        });
      }
    },
    esPortada(imagen, imagenes) {
      for (let i = 0; i < imagenes.length; i++) {
        if (imagenes[i] === imagen) {
          if (imagenes[i].portada === true) {
            imagenes[i].portada = false;
          } else {
            imagenes[i].portada = true;
          }
        } else {
          imagenes[i].portada = false;
        }
      }
    },
    borrarImagen(imagen, arrayImagenes) {
      const index = arrayImagenes.indexOf(imagen);
      if (index > -1) {
        arrayImagenes.splice(index, 1);
      }
    },

    FinalizarFormulario() {
      this.$emit("finalizar", this.habitacionesCopy, this.imagenesPisoFR);
    },
  },
};
</script>

<style scoped>
img {
  margin: 3px;
}
img.selected {
  box-shadow: 0px 12px 22px 1px red;
}
</style>
