<template>
  <div class="card" style="width: 80%; margin-left: 10%">
    <div class="card" style="width: 70%; margin: 1rem auto">
      <h2>Añadir habitación</h2>
      <div class="container">
        <div class="row">
          <div class="col-sm">
            <div class="form-floating">
              <input
                class="form-control"
                id="inicioContrato"
                v-model="habitacion.inicioContrato"
                type="date"
                style="margin-bottom: 1rem"
                :class="{
                  error: habitacionErrores.errorDateDisponible,
                }"
              />
              <label for="inicioContrato">Fecha Disponible</label>
            </div>
          </div>
          <div v-if="habitacionErrores.errorDateDisponible === true">
            <h6 style="color: red">
              La fecha de inicio de contrato debe ser correcta y superior al dia
              actual
            </h6>
          </div>
          <div class="col-sm">
            <div class="form-floating">
              <input
                class="form-control"
                id="finContrato"
                v-model="habitacion.finContrato"
                type="date"
                style="margin-bottom: 1rem"
                :class="{
                  error:
                    habitacionErrores.errorDateFinal ||
                    habitacion.fechaFinMInicio,
                }"
              />
              <label for="finContrato">Fecha Limite</label>
            </div>
          </div>
          <div v-if="habitacionErrores.errorDateFinal === true">
            <h6 style="color: red">
              La fecha de fin de contrato debe ser correcta y superior al dia
              actual
            </h6>
          </div>
          <div v-if="habitacion.fechaFinMInicio === true">
            <h6 style="color: red">
              La fecha de final de contrato debe ser superior a la de inicio
            </h6>
          </div>
        </div>
      </div>
      <div class="container">
        <div class="row" style="margin-bottom: 1rem">
          <div class="col-sm">
            <div class="input-group">
              <div class="form-floating">
                <input
                  class="form-control"
                  id="superficie"
                  v-model="habitacion.mCuadrados"
                  type="number"
                  :class="{
                    error: habitacionErrores.errorSuperficie,
                  }"
                />
                <label for="superficie">Superficie</label>
              </div>
              <span class="input-group-text">m²</span>
            </div>
          </div>
          <div v-if="habitacionErrores.errorSuperficie === true">
            <h6 style="color: red">
              La superficie de la habitacion debe ser mayor a 0
            </h6>
          </div>
          <div class="col-sm">
            <div class="input-group">
              <div class="form-floating">
                <input
                  class="form-control"
                  id="precio"
                  v-model="habitacion.precio"
                  type="number"
                  :class="{
                    error: habitacionErrores.errorPrecio,
                  }"
                />
                <label for="precio">Precio</label>
              </div>
              <span class="input-group-text">€</span>
            </div>
          </div>
          <div v-if="habitacionErrores.errorPrecio === true">
            <h6 style="color: red">
              El precio del alquiler debe ser superior a 0
            </h6>
          </div>
        </div>
      </div>
      <div class="container">
        <div class="row" style="margin-bottom: 1rem">
          <div class="col-sm">
            <div class="form-floating">
              <input
                class="form-control"
                id="gastosComp"
                v-model="habitacion.gastosCompartidos"
                type="number"
                :class="{
                  error: habitacionErrores.errorGastosComp,
                }"
                :disabled="habitacion.gastosCInc"
              />
              <label for="gastosComp">Gastos compartidos(Aproximados)</label>
            </div>
          </div>
          <div
            v-if="
              habitacionErrores.errorGastosComp === true &&
              habitacion.gastosCInc === false
            "
          >
            <h6 style="color: red">
              Los gastos compartidos deben ser superiores a 0 o seleccionar la
              opcion de incluirlos en el precion del alquiler
            </h6>
          </div>
          <div class="col-sm">
            <div class="form-check form-switch">
              <input
                class="form-check-input"
                type="checkbox"
                v-model="habitacion.gastosCInc"
                id="flexCheckDefault"
              />
              <label class="form-check-label" for="flexCheckDefault">
                Gastos incluidos en el alquiler
              </label>
            </div>
          </div>
        </div>
      </div>
      <div class="container">
        <div class="row" style="margin-bottom: 1rem">
          <div class="col-sm">
            <div class="form-floating">
              <input
                class="form-control"
                id="fianza"
                v-model="habitacion.fianza"
                type="number"
                :class="{
                  error: habitacionErrores.errorFianza,
                }"
                :disabled="habitacion.sFianza"
              />
              <label for="fianza">Fianza</label>
            </div>
            <div
              v-if="
                habitacionErrores.errorFianza === true &&
                habitacion.sFianza === false
              "
            >
              <h6 style="color: red">
                La fianza deben ser superior a 0 o seleccionar la opcion de no
                tener fianza
              </h6>
            </div>
          </div>
          <div class="col-sm">
            <div class="form-check form-switch">
              <input
                class="form-check-input"
                type="checkbox"
                v-model="habitacion.sFianza"
                id="flexCheckDefault"
              />
              <label class="form-check-label" for="flexCheckDefault">
                Sin fianza
              </label>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="card" style="width: 70%; margin: 1rem auto">
      <h2>Opciones Habitacion</h2>
      <div v-for="opcion in opcionesHabitacion" :key="opcion.id">
        <input
          class="form-check-input"
          type="checkbox"
          v-model="opcion.seleccionada"
          id="flexCheckDefault"
        />
        <label class="form-check-label" for="flexCheckDefault">
          {{ opcion.opcionObjeto.nombre }}
        </label>
      </div>
    </div>
    <div class="card" style="width: 70%; margin: 1rem auto">
      <h2>Descripción Habitacion</h2>
      <div class="container" style="margin-bottom: 1rem">
        <div class="form-floating">
          <textarea
            id="titulo"
            v-model="habitacion.titulo"
            class="form-control"
          />
          <label for="titulo">Titulo Anuncio</label>
        </div>
        <div v-if="habitacionErrores.errorTitulo === true" style="color: red">
          El titulo del anuncio no puede estar vacio
        </div>
      </div>
      <div class="container" style="margin-bottom: 10px">
        <div class="form-floating">
          <textarea
            class="form-control"
            id="descripcion"
            v-model="habitacion.descripcion"
            style="height: 100px"
            :class="{
              error: habitacionErrores.errorDescripcion,
            }"
          ></textarea>
          <label for="descripcion">Descripción</label>
        </div>
        <div
          v-if="habitacionErrores.errorDescripcion === true"
          style="color: red"
        >
          La descripcion del anuncio no puede estar vacia
        </div>
      </div>
    </div>
    <div class="card">
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
        <div
          class="col"
          v-for="imagen in imagenesPisoFR"
          :key="imagen.imagenFR"
        >
          <button
            class="btn btn-close"
            style="background-color: red"
            v-on:click="borrarImagen(imagen, imagenesPisoFR)"
          ></button>
          <img :src="imagen.imagenFR" width="200" height="200" />
        </div>
      </div>
    </div>
    <div class="card">
      <h2>Imagenes Habitacion</h2>
      <div>
        <button
          class="btn btn-secondary"
          @click.prevent="iniciarSubidaFicheroHabitacion"
        >
          Elegir imagen
        </button>
      </div>
      <input
        ref="inputOcultoHabitacion"
        type="file"
        class="d-none"
        multiple
        @change="actualizarSubidaFicheroHabitacion"
      />
      <div class="row">
        <div
          class="col"
          v-for="imagen in imagenesHabitacion"
          :key="imagen.imagenFR"
        >
          <button
            class="btn btn-close"
            style="background-color: red; position: absolute"
            v-on:click="borrarImagen(imagen, imagenesHabitacion)"
          ></button>
          <img
            :src="imagen.imagenFR"
            :class="{ selected: imagen.portada }"
            width="200"
            height="200"
            v-on:click="esPortada(imagen, imagenesHabitacion)"
          />
          <h6 v-if="imagen.portada === true" style="color: red">Portada</h6>
        </div>
      </div>
    </div>
    <button class="btn btn-secondary" v-on:click="cancelarAnadir">
      Cancelar
    </button>
    <button class="btn btn-success" v-on:click="anadirHabitacion">
      Añadir habitacion
    </button>
  </div>
</template>

<script>
import HabitacionRepository from "@/repositories/HabitacionRepository";
import PisoRepository from "@/repositories/PisoRepository";
import { useToast } from "vue-toastification";

export default {
  name: "FormularioHabitaciones",
  data() {
    return {
      habitacion: {
        inicioContrato: null,
        finContrato: null,
        mCuadrados: null,
        precio: null,
        gastosCompartidos: 0,
        gastosCInc: false,
        fianza: 0,
        sFianza: false,
        titulo: null,
        descripcion: null,
        opciones: [],
        piso: Object,
      },
      habitacionErrores: {
        errorSuperficie: false,
        errorDateDisponible: false,
        errorDateFinal: false,
        errorPrecio: false,
        errorGastosComp: false,
        errorFianza: false,
        errorTitulo: false,
        errorDescripcion: false,
        fechaFinMInicio: false,
      },
      opcionesHabitacion: [],
      imagenesPisoFR: [],
      imagenesHabitacion: [],
    };
  },
  mounted() {
    this.getOpcionesHabitacion();
    //Falla porque se renderiza en el html al principio, antes de que se pulse el boton de ir a crear habitaciones
    //deberia gestionarlo de manera dinamica y que se inserten en el momento que se modifica el valor de
  },
  methods: {
    async getOpcionesHabitacion() {
      let opciones = await HabitacionRepository.getOpcionesHabitacion();
      for (let i = 0; i < opciones.length; i++) {
        let obj = {
          opcionObjeto: opciones[i],
          seleccionada: false,
        };
        this.opcionesHabitacion.push(obj);
      }
    },
    cancelarAnadir() {
      this.$router.go(-1);
    },
    async anadirHabitacion() {
      const toast = useToast();
      let erroresFormulario = false;
      for (let i = 0; i < this.opcionesHabitacion.length; i++) {
        if (this.opcionesHabitacion[i].seleccionada === true) {
          this.habitacion.opciones.push(
            this.opcionesHabitacion[i].opcionObjeto
          );
        }
      }
      //Errores de cada habitacion del formulario
      this.habitacionErrores.errorSuperficie = false;
      this.habitacionErrores.errorFianza = false;
      this.habitacionErrores.errorGastosComp = false;
      this.habitacionErrores.errorPrecio = false;
      this.habitacionErrores.errorDateFinal = false;
      this.habitacionErrores.errorDescripcion = false;
      this.habitacionErrores.errorTitulo = false;
      this.habitacionErrores.errorDateDisponible = false;
      this.habitacionErrores.fechaFinMInicio = false;
      let fechaActual = new Date(
        new Date().getFullYear(),
        new Date().getUTCMonth(),
        new Date().getUTCDate()
      );
      //Date parse pasa la fecha de string (input) a milisegundos, y new Date(milisegundos) la pasamos al mismo formato
      //que fechaActual.
      if (new Date(Date.parse(this.habitacion.inicioContrato)) < fechaActual) {
        this.habitacionErrores.errorDateDisponible = true;
        erroresFormulario = true;
      }
      if (new Date(Date.parse(this.habitacion.finContrato)) < fechaActual) {
        this.habitacionErrores.errorDateFinal = true;
        erroresFormulario = true;
      }
      if (
        new Date(Date.parse(this.habitacion.inicioContrato)) >=
        new Date(Date.parse(this.habitacion.finContrato))
      ) {
        this.habitacionErrores.fechaFinMInicio = true;
        erroresFormulario = true;
      }
      if (this.habitacion.precio <= 0) {
        this.habitacionErrores.errorPrecio = true;
        erroresFormulario = true;
      }
      if (
        this.habitacion.gastosCompartidos <= 0 &&
        this.habitacion.gastosCInc === false
      ) {
        this.habitacionErrores.errorGastosComp = true;
        erroresFormulario = true;
      }
      if (this.habitacion.fianza <= 0 && this.habitacion.sFianza === false) {
        this.habitacionErrores.errorFianza = true;
        erroresFormulario = true;
      }
      if (this.habitacion.mCuadrados <= 0) {
        this.habitacionErrores.errorSuperficie = true;
        erroresFormulario = true;
      }
      if (this.habitacion.titulo.length === 0) {
        this.habitacionErrores.errorTitulo = true;
        erroresFormulario = true;
      }
      if (this.habitacion.descripcion.length === 0) {
        this.habitacionErrores.errorDescripcion = true;
        erroresFormulario = true;
      }
      if (erroresFormulario === true) {
        toast.info(
          "Corrija los errores del formulario antes de pasar a la eleccion de imagenes",
          {
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
          }
        );
        window.scrollTo(0, 0);
      } else {
        let piso = await PisoRepository.getPiso(this.$route.params.idPiso);
        this.habitacion.piso = piso;
        let habitacionCreada = await PisoRepository.anadirHabitacion(
          this.habitacion,
          this.$route.params.idPiso
        );
        await this.guardarImagenes(
          habitacionCreada.id,
          this.imagenesHabitacion
        );
        await this.guardarImagenes(habitacionCreada.id, this.imagenesPisoFR);
        toast.success(
          "Habitación añadida correctamente al piso " +
            this.habitacion.piso.direccion,
          {
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
          }
        );
        this.$router.push({ name: "misAnuncios" });
      }
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
    iniciarSubidaFicheroHabitacion() {
      this.$refs.inputOcultoHabitacion.click();
    },
    actualizarSubidaFicheroHabitacion() {
      for (let i = 0; i < this.$refs.inputOcultoHabitacion.files.length; i++) {
        const reader = new FileReader();
        reader.readAsDataURL(this.$refs.inputOcultoHabitacion.files[i]);
        reader.addEventListener("load", (e) => {
          let obj = {
            imagenFR: e.target.result, // imagen en formato base64 para img en html
            imagenFile: this.$refs.inputOcultoHabitacion.files[i], // archivo imagen
            portada: false, // si va ser la portada
          };
          this.imagenesHabitacion.push(obj);
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
    async guardarImagenes(habitacionId, imagenes) {
      //Cambiado de manera que la variable portada ya se envia al backend, la consulta
      //que despues miraba todas es innecesaria.
      for (let i = 0; i < imagenes.length; i++) {
        await HabitacionRepository.subirImagen(
          habitacionId,
          imagenes[i].imagenFile,
          imagenes[i].portada
        ).catch((err) => {
          console.log(err);
        });
      }
    },
    borrarImagen(imagen, arrayImagenes) {
      const index = arrayImagenes.indexOf(imagen);
      if (index > -1) {
        arrayImagenes.splice(index, 1);
      }
    },
  },
};
</script>

<style scoped>
.form-control.error {
  border-color: red;
}
textarea.error {
  border-color: red;
}
input.error {
  border-color: red;
}
img {
  margin: 3px;
}
img.selected {
  box-shadow: 0px 12px 22px 1px red;
}
</style>
