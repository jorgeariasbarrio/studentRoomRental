<template>
  <form v-for="(habitacion, index) in habitaciones2" :key="habitacion">
    <div class="card" style="width: 70%; margin: 1rem auto">
      <h2>Habitacion {{ index + 1 }}</h2>
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
                  errorsInput: habitaciones2Errores[index].fechaFinMInicio,
                }"
              />
              <label for="inicioContrato">Fecha Disponible</label>
            </div>
          </div>
          <div v-if="habitaciones2Errores[index].errorDateDisponible === true">
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
                  errorsInput:
                    habitaciones2Errores[index].errorDateFinal ||
                    habitaciones2Errores[index].fechaFinMInicio,
                }"
              />
              <label for="finContrato">Fecha Limite</label>
            </div>
          </div>
          <div v-if="habitaciones2Errores[index].errorDateFinal === true">
            <h6 style="color: red">
              La fecha de fin de contrato debe ser correcta y superior al dia
              actual
            </h6>
          </div>
          <div v-if="habitaciones2Errores[index].fechaFinMInicio === true">
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
                    errorsInput: habitaciones2Errores[index].errorSuperficie,
                  }"
                />
                <label for="superficie">Superficie</label>
              </div>
              <span class="input-group-text">m²</span>
            </div>
          </div>
          <div v-if="habitaciones2Errores[index].errorSuperficie === true">
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
                    errorsInput: habitaciones2Errores[index].errorPrecio,
                  }"
                />
                <label for="precio">Precio</label>
              </div>
              <span class="input-group-text">€</span>
            </div>
          </div>
          <div v-if="habitaciones2Errores[index].errorPrecio === true">
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
                  errorsInput: habitaciones2Errores[index].errorGastosComp,
                }"
                :disabled="habitacion.gastosCInc === true"
              />
              <label for="gastosComp">Gastos compartidos(Aproximados)</label>
            </div>
          </div>
          <div
            v-if="
              habitaciones2Errores[index].errorGastosComp === true &&
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
                  errorsInput: habitaciones2Errores[index].errorFianza,
                }"
                :disabled="habitacion.sFianza === true"
              />
              <label for="fianza">Fianza</label>
            </div>
            <div
              v-if="
                habitaciones2Errores[index].errorFianza === true &&
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
      <h2>Opciones Habitacion{{ index + 1 }}</h2>
      <div v-for="opcion in habitacion.opciones" :key="opcion.id">
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
      <h2>Descripción Habitacion {{ index + 1 }}</h2>
      <div class="container" style="margin-bottom: 1rem">
        <div class="form-floating">
          <textarea
            id="titulo"
            v-model="habitacion.titulo"
            class="form-control"
          />
          <label for="titulo">Titulo Anuncio</label>
        </div>
        <div
          v-if="habitaciones2Errores[index].errorTitulo === true"
          style="color: red"
        >
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
              errorsInput: habitaciones2Errores[index].errorDescripcion,
            }"
          ></textarea>
          <label for="descripcion">Descripción</label>
        </div>
        <div
          v-if="habitaciones2Errores[index].errorDescripcion === true"
          style="color: red"
        >
          La descripcion del anuncio no puede estar vacia
        </div>
      </div>
    </div>
  </form>
  <button class="btn btn-secondary" v-on:click="volverAPiso">
    Volver al piso
  </button>
  <button class="btn btn-success" v-on:click="comprobarHabitacionesGenerico">
    Añadir imagenes
  </button>
</template>

<script>
import HabitacionRepository from "@/repositories/HabitacionRepository";
import { useToast } from "vue-toastification";

export default {
  name: "FormularioHabitaciones",
  props: {
    habitacionesLibres: Number,
  },
  emits: ["volverPiso", "pasarAImagenes"],
  watch: {
    habitacionesLibres: function (value) {
      // Si cambia el valor de habitacionesLibres, value contendrá el nuevo valor
      while (this.habitaciones2.length !== parseInt(value)) {
        if (this.habitaciones2.length < value) {
          let habitacion = {
            finContrato: new Date(
              new Date().getFullYear(),
              new Date().getUTCMonth(),
              new Date().getUTCDate() - 1
            ),
            inicioContrato: new Date(
              new Date().getFullYear(),
              new Date().getUTCMonth(),
              new Date().getUTCDate() - 1
            ),
            mCuadrados: 0,
            precio: 0,
            gastosCompartidos: 0,
            gastosCInc: false,
            fianza: 0,
            sFianza: false,
            opciones: [],
            imagenes: [],
            descripcion: "",
            titulo: "",
          };
          for (let i = 0; i < this.opcionesHabitacion1Var.length; i++) {
            let opcion = {
              opcionObjeto: Object,
              seleccionada: false,
            };
            opcion.opcionObjeto = this.opcionesHabitacion1Var[i];
            habitacion.opciones.push(opcion);
          }
          let erroresHabitacion = {
            errorSuperficie: false,
            errorDateDisponible: false,
            errorDateFinal: false,
            errorPrecio: false,
            errorGastosComp: false,
            errorFianza: false,
            errorTitulo: false,
            errorDescripcion: false,
            fechaFinMInicio: false,
          };
          this.habitaciones2.push(habitacion);
          this.habitaciones2Errores.push(erroresHabitacion);
        } else if (this.habitaciones2.length > value) {
          this.opcionesHabitacion.pop();
          this.habitaciones2.pop();
          this.habitaciones2Errores.pop();
        }
      }
    },
  },
  data() {
    return {
      habitaciones2: [],
      habitaciones2Errores: [],
      opcionesHabitacion: [],
      opcionesHabitacion1: [], // Opciones elegidas de la habitacion 1
      opcionesHabitacion2: [],
      opcionesHabitacion3: [],
      opcionesHabitacion4: [],
      opcionesHabitacion5: [],
      opcionesPisoVar: [
        {
          opcion: Object,
          seleccionada: false,
        },
      ],
      opcionesHabitacion1Var: [
        {
          opcion: Object,
          seleccionada: false,
        },
      ],
      opcionesHabitacion2Var: [
        {
          opcion: Object,
          seleccionada: false,
        },
      ],
      opcionesHabitacion3Var: [
        {
          opcion: Object,
          seleccionada: false,
        },
      ],
      opcionesHabitacion4Var: [
        {
          opcion: Object,
          seleccionada: false,
        },
      ],
      opcionesHabitacion5Var: [
        {
          opcion: Object,
          seleccionada: false,
        },
      ],
    };
  },
  mounted() {
    this.getOpcionesHabitacion();
    //Falla porque se renderiza en el html al principio, antes de que se pulse el boton de ir a crear habitaciones
    //deberia gestionarlo de manera dinamica y que se inserten en el momento que se modifica el valor de
  },
  methods: {
    async getOpcionesHabitacion() {
      this.opcionesHabitacion1Var =
        await HabitacionRepository.getOpcionesHabitacion();
      this.opcionesHabitacion2Var =
        await HabitacionRepository.getOpcionesHabitacion();
      this.opcionesHabitacion3Var =
        await HabitacionRepository.getOpcionesHabitacion();
      this.opcionesHabitacion4Var =
        await HabitacionRepository.getOpcionesHabitacion();
      this.opcionesHabitacion5Var =
        await HabitacionRepository.getOpcionesHabitacion();
    },
    volverAPiso() {
      this.$emit("volverPiso");
    },
    comprobarHabitacionesGenerico() {
      const toast = useToast();
      let erroresFormulario = false;
      for (let i = 0; i < this.habitacionesLibres; i++) {
        //Errores de cada habitacion del formulario
        this.habitaciones2Errores[i].errorSuperficie = false;
        this.habitaciones2Errores[i].errorFianza = false;
        this.habitaciones2Errores[i].errorGastosComp = false;
        this.habitaciones2Errores[i].errorPrecio = false;
        this.habitaciones2Errores[i].errorDateFinal = false;
        this.habitaciones2Errores[i].errorDescripcion = false;
        this.habitaciones2Errores[i].errorTitulo = false;
        this.habitaciones2Errores[i].errorDateDisponible = false;
        this.habitaciones2Errores[i].fechaFinMInicio = false;
        let fechaActual = new Date(
          new Date().getFullYear(),
          new Date().getUTCMonth(),
          new Date().getUTCDate()
        );
        //Date parse pasa la fecha de string (input) a milisegundos, y new Date(milisegundos) la pasamos al mismo formato
        //que fechaActual.
        if (
          new Date(Date.parse(this.habitaciones2[i].inicioContrato)) <
          fechaActual
        ) {
          this.habitaciones2Errores[i].errorDateDisponible = true;
          erroresFormulario = true;
        }
        if (
          new Date(Date.parse(this.habitaciones2[i].finContrato)) < fechaActual
        ) {
          this.habitaciones2Errores[i].errorDateFinal = true;
          erroresFormulario = true;
        }
        if (
          new Date(Date.parse(this.habitaciones2[i].inicioContrato)) >=
          new Date(Date.parse(this.habitaciones2[i].finContrato))
        ) {
          this.habitaciones2Errores[i].fechaFinMInicio = true;
          erroresFormulario = true;
        }
        if (this.habitaciones2[i].precio <= 0) {
          this.habitaciones2Errores[i].errorPrecio = true;
          erroresFormulario = true;
        }
        if (
          this.habitaciones2[i].gastosCompartidos <= 0 &&
          this.habitaciones2[i].gastosCInc === false
        ) {
          this.habitaciones2Errores[i].errorGastosComp = true;
          erroresFormulario = true;
        }
        if (
          this.habitaciones2[i].fianza <= 0 &&
          this.habitaciones2[i].sFianza === false
        ) {
          this.habitaciones2Errores[i].errorFianza = true;
          erroresFormulario = true;
        }
        if (this.habitaciones2[i].mCuadrados <= 0) {
          this.habitaciones2Errores[i].errorSuperficie = true;
          erroresFormulario = true;
        }
        if (this.habitaciones2[i].titulo.length === 0) {
          this.habitaciones2Errores[i].errorTitulo = true;
          erroresFormulario = true;
        }
        if (this.habitaciones2[i].descripcion.length === 0) {
          this.habitaciones2Errores[i].errorDescripcion = true;
          erroresFormulario = true;
        }
      }
      if (erroresFormulario === true) {
        toast.info("Corrija los errores del formulario!", {
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
        window.scrollTo(0, 0);
      } else {
        this.$emit("pasarAImagenes", this.habitaciones2);
      }
    },
  },
};
</script>

<style scoped>
select.errorsSelect {
  border-color: red;
}
textarea.errorsInput {
  border-color: red;
}
input.errorsInput {
  border-color: red;
}
</style>
