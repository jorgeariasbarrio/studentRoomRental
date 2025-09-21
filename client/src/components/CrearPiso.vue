<template>
  <link
    rel="stylesheet"
    href="https://unpkg.com/leaflet@1.9.3/dist/leaflet.css"
    integrity="sha256-kLaT2GOSpHechhsozzB+flnD+zUyjE2LlfWPgU04xyI="
    crossorigin=""
  />
  <!-- progressbar -->
  <ul id="progressbar" style="margin-top: 1%">
    <li :class="{ active: campoPisosActivo }">Pisos</li>
    <li :class="{ active: campoHabitacionesActivo }">Habitacion/es</li>
    <li :class="{ active: campoImagenActivo }">Imagenes</li>
  </ul>
  <div
    class="card"
    style="width: 80%"
    id="pisoForm"
    :class="{ ocultar: ocultarFormularioPiso }"
  >
    <form class="card" style="width: 70%">
      <h2>Datos de contacto</h2>
      <div class="container">
        <div class="row">
          <div class="col-sm">
            <div class="form-floating">
              <input
                id="emailUsuario"
                class="form-control"
                v-model="email"
                type="text"
                :class="{ errorsSelect: errorEmail }"
              />
              <label for="emailUsuario">Email</label>
            </div>
            <h6 v-if="errorEmail === true" style="color: red">
              Debe indicarse un email válido
            </h6>
          </div>
          <div class="col-sm">
            <div class="form-floating">
              <input
                class="form-control"
                id="nombreUsuario"
                v-model="nombre"
                type="text"
                :class="{ errorsSelect: errorNombre }"
              />
              <label for="nombreUsuario">Nombre</label>
            </div>
            <h6 v-if="errorNombre === true" style="color: red">
              Debe indicarse un nombre
            </h6>
          </div>
          <div class="col-sm">
            <div class="form-floating">
              <input
                id="telefonoInput"
                class="form-control"
                v-model="telefono"
                type="number"
                :class="{ errorsSelect: errorTelefono }"
                style="margin-bottom: 1rem"
              />
              <label for="telefonoInput">Teléfono</label>
            </div>
            <h6 v-if="errorTelefono === true" style="color: red">
              Debe indicarse un teléfono válido
            </h6>
          </div>
        </div>
      </div>
    </form>
    <form class="card" style="width: 70%">
      <h2>Normas de convivencia</h2>
      <div class="container">
        <div class="form-floating">
          <select
            id="selectGenero"
            class="form-select"
            aria-label="Default select example"
            v-model="generoElegido"
            style="margin-bottom: 1rem"
            :class="{ errorsSelect: provinciaVacia }"
          >
            <option
              v-for="genero in opcionesGenero"
              :key="genero.id"
              :value="genero"
            >
              {{ genero.nombre }}
            </option>
          </select>
          <label for="selectGenero">Genero</label>
          <h6 v-if="generoVacio === true" style="color: red">
            Se debe seleccionar alguna opcion para el género
          </h6>
        </div>
      </div>
      <div class="container">
        <h4>Rango de edades</h4>
        <div class="input-group mb-3">
          <div class="form-floating">
            <input
              id="edadMinima"
              class="form-control"
              v-model="edadMinima"
              type="number"
            />
            <label for="edadMinima"> Edad mímina</label>
          </div>
          <span class="input-group-text">-</span>
          <div class="form-floating">
            <input
              id="edadMaxima"
              class="form-control"
              v-model="edadMaxima"
              type="number"
            />
            <label for="edadMaxima"> Edad máxima</label>
          </div>
        </div>
      </div>
      <div class="container">
        <h4>Otras normas</h4>
        <div
          v-for="opcion in normasPiso"
          :key="opcion.id"
          class="form-check form-check-inline"
        >
          <input
            class="form-check-input"
            type="checkbox"
            v-model="opcion.seleccionada"
            id="flexCheckDefault"
          />
          <label class="form-check-label" for="flexCheckDefault">
            {{ opcion.norma.nombre }}
          </label>
        </div>
      </div>
    </form>
    <form
      class="card"
      style="
        width: 70%;
        margin-left: auto;
        margin-right: auto;
        margin-top: 1rem;
      "
    >
      <h2>Caracteristicas del piso</h2>
      <div class="container">
        <div class="row g-2">
          <div class="col-sm">
            <div class="input-group">
              <div class="form-floating">
                <input
                  id="superficie"
                  class="form-control"
                  v-model="superficie"
                  type="number"
                  :class="{ errorsInput: erroresSuperficie }"
                  min="0"
                />
                <label for="superficie">Superficie</label>
              </div>
              <span class="input-group-text">m2</span>
            </div>
          </div>
          <h6 v-if="erroresSuperficie" style="color: red">
            Se debe indicar la superficie, y debe ser mayor que 0m2
          </h6>
          <div class="col-sm">
            <div class="input-group">
              <div class="form-floating">
                <input
                  id="pisoInput"
                  class="form-control"
                  v-model="piso"
                  type="number"
                  :class="{ errorsInput: plantaVacia }"
                  min="0"
                />
                <label for="pisoInput">Altura</label>
              </div>
              <span class="input-group-text">º</span>
            </div>
          </div>
        </div>
      </div>
      <h6 v-if="plantaVacia === true" style="color: red">
        Se debe indicar la planta en la que se encuentra el piso.
      </h6>
      <div class="container" style="margin-top: 10px; margin-bottom: 10px">
        <div class="row g-3">
          <div class="col-sm">
            <div class="form-floating">
              <select
                id="numeroHabitaciones"
                class="form-select"
                aria-label="Default select example"
                v-model="nHabitaciones"
                :class="{
                  errorsSelect:
                    numeroHabitacionesVacias || habitacionesLibresMtotales,
                }"
              >
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
              <label for="numeroHabitaciones">Habitaciones</label>
            </div>
          </div>
          <h6 v-if="numeroHabitacionesVacias" style="color: red">
            Se debe indicar el numero total de habitaciones
          </h6>
          <div class="col-sm">
            <div class="form-floating">
              <select
                id="banos"
                class="form-select"
                aria-label="Default select example"
                v-model="nBanos"
                :class="{ errorsSelect: numeroBanosVacios }"
              >
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
              <label for="banos">Baños</label>
            </div>
          </div>
          <h6 v-if="numeroBanosVacios === true" style="color: red">
            Se debe indicar el numero de baños
          </h6>
          <div class="col-sm">
            <div class="form-floating">
              <select
                id="habitacionesLibres"
                class="form-select"
                aria-label="Default select example"
                v-model="habitacionesLibres"
                :class="{
                  errorsSelect:
                    habitacionesLibresVacio || habitacionesLibresMtotales,
                }"
              >
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
              <label for="habitacionesLibres">Habitaciones libres</label>
            </div>
          </div>
        </div>
      </div>
      <h6 v-if="habitacionesLibresVacio === true" style="color: red">
        Se debe indicar el numero de habitaciones libres
      </h6>
      <h6 v-if="habitacionesLibresMtotales === true" style="color: red">
        El numero de habitaciones libres no puede ser superior al numero de
        habitaciones totales
      </h6>
    </form>
    <div
      class="card"
      style="
        width: 70%;
        margin-left: auto;
        margin-right: auto;
        margin-top: 1rem;
      "
    >
      <div>
        <h2>Localizacion del piso</h2>
        <form>
          <div class="container">
            <div class="form-floating">
              <input
                id="codigoPostal"
                v-model="codigoPostal"
                class="form-control"
                type="number"
                :class="{ errorsInput: codigoPostalValido }"
                min="0"
              />
              <label for="codigoPostal">Codigo Postal</label>
            </div>
          </div>
          <h6 v-if="codigoPostalValido === true" style="color: red">
            Se debe indicar el codigo postal y tiene que ser válido(entre 01000
            y 52999)
          </h6>
          <div class="container" style="margin-top: 10px">
            <div class="row">
              <div class="col-sm">
                <div class="form-floating">
                  <select
                    class="form-select"
                    id="selectProvincia"
                    aria-label="Default select example"
                    v-model="provincia"
                    :class="{ errorsSelect: provinciaVacia }"
                  >
                    <option v-for="provincia in provincias" :key="provincia">
                      {{ provincia }}
                    </option>
                  </select>
                  <label for="selectProvincia">Provincia</label>
                </div>
              </div>
              <h6 v-if="provinciaVacia === true" style="color: red">
                Se debe indicar la provincia
              </h6>
              <div class="col-sm">
                <div class="form-floating">
                  <select
                    id="selectMunicipio"
                    class="form-select"
                    aria-label="Default select example"
                    v-model="municipio"
                    :class="{
                      errorsSelect: municipioVacio,
                    }"
                    disabled
                  >
                    <option v-for="municipio in municipios" :key="municipio">
                      {{ municipio }}
                    </option>
                  </select>
                  <label for="selectMunicipio">Municipio</label>
                </div>
              </div>
            </div>
          </div>
          <h6 v-if="municipioVacio === true" style="color: red">
            Se debe indicar la municipio
          </h6>
          <div class="container" style="margin-top: 10px">
            <div class="row g-3">
              <div class="col-sm">
                <div class="form-floating">
                  <select
                    id="tipoVia"
                    class="form-select"
                    aria-label="Default select example"
                    v-model="direccionPartida.tipoVia"
                    :class="{
                      errorsSelect: tipoViaVacio,
                    }"
                  >
                    <option value="Avenida">Avenida</option>
                    <option value="Rua">Rua</option>
                    <option value="Calle">Calle</option>
                  </select>
                  <label for="tipoVia">Tipo de vía</label>
                </div>
              </div>
              <div class="col-sm">
                <div class="form-floating">
                  <input
                    id="nombreVia"
                    class="form-control"
                    v-model="direccionPartida.nombreVia"
                    type="text"
                    :class="{ errorsInput: nombreViaVacio }"
                  />
                  <label for="nombreVia">Nombre de la vía</label>
                </div>
              </div>
              <div class="col-sm">
                <div class="form-floating">
                  <input
                    id="numeroVia"
                    class="form-control"
                    v-model="direccionPartida.numeroVia"
                    type="number"
                    min="1"
                    :class="{ errorsInput: numeroViaVacio }"
                  />
                  <label for="numeroVia">Número de la vía</label>
                </div>
                <h6 v-if="direccionVacia === true" style="color: red">
                  Se debe indicar la direccion
                </h6>
              </div>
            </div>
          </div>
          <div>
            <button
              class="btn btn-info"
              type="button"
              v-on:click="colocarMakerPorDireccion"
              style="margin-top: 10px; margin-bottom: 10px"
            >
              Buscar en el mapa
            </button>
          </div>
          <h4 v-if="errorBusquedaDireccion === true" style="color: red">
            No se encuentran resultados para esa búsqueda. Cambie los datos de
            búsqueda o marque manualmente la dirección
          </h4>
        </form>
      </div>
      <div id="container">
        <div id="mapContainer"></div>
      </div>
    </div>
    <div class="card" style="width: 70%">
      <h4>Equipamiento opcional</h4>
      <div class="container">
        <div
          class="form-check form-check-inline"
          v-for="opcion in opcionesPisoVar"
          :key="opcion.id"
        >
          <input
            class="form-check-input"
            type="checkbox"
            v-model="opcion.seleccionada"
            id="flexCheckDefault"
          />
          <label class="form-check-label" for="flexCheckDefault">
            {{ opcion.nombre }}
          </label>
        </div>
      </div>
    </div>
    <div id="botonCrear" style="margin-top: 20px">
      <button class="btn btn-success" v-on:click="mostrarHabitaciones">
        Crear Habitaciones
      </button>
      <button class="btn btn-danger" v-on:click="this.$router.go(-1)">
        Cancelar
      </button>
    </div>
  </div>
  <div
    class="card"
    style="width: 80%"
    id="habitacionForm"
    :class="{ ocultar: ocultarFormularioHabitacion }"
  >
    <FormularioHabitaciones
      :habitaciones-libres="habitacionesLibres"
      @volverPiso="volverAPiso"
      @pasarAImagenes="pasarAImagenes"
    ></FormularioHabitaciones>
  </div>
  <div
    class="card"
    style="width: 70%"
    :class="{ ocultar: ocultarFormularioImagen }"
  >
    <FormularioImagenes
      :habitaciones-libres="habitacionesLibres"
      :habitaciones="habitaciones"
      @volverHabitaciones="mostrarHabitaciones"
      @finalizar="finalizarFormulario"
    ></FormularioImagenes>
  </div>
</template>

<script>
import AccountRepository from "@/repositories/AccountRepository";
import PisoRepository from "@/repositories/PisoRepository";
import "leaflet/dist/leaflet.css";
import "leaflet/dist/images/marker-shadow.png";
import "@vueform/vueform";
import HabitacionRepository from "@/repositories/HabitacionRepository";
import FormularioHabitaciones from "@/components/FormularioHabitaciones.vue";
import FormularioImagenes from "@/components/FormularioImagenes.vue";
import L, { LatLng } from "leaflet";
import { useToast } from "vue-toastification";
export default {
  name: "CrearPiso",
  components: {
    FormularioImagenes,
    FormularioHabitaciones,
  },
  emits: [
    "pasarAImagenes",
    "volverPiso",
    "volverAHabitaciones",
    "habitacionesLibres",
    "habitaciones",
  ],
  mounted() {
    this.getUsuarioLogueado();
    this.setupLeafletMap();
    //Falla porque se renderiza en el html al principio, antes de que se pulse el boton de ir a crear habitaciones
    //deberia gestionarlo de manera dinamica y que se inserten en el momento que se modifica el valor de
  },
  watch: {
    provincia: async function (value) {
      var input = document.getElementById("selectMunicipio");
      input.removeAttribute("disabled");
      let allMunicipios = await PisoRepository.getMunicipios(value);
      this.municipios = [];
      for (let i = 0; i < allMunicipios.length; i++) {
        this.municipios.push(allMunicipios[i].municipio);
      }
    },
  },
  data() {
    return {
      //En estas variables guardamos las imagenes en formato base64, el file y si es la portada
      //variables para la barra de navegabilidad
      //true, se muestra como completo el campo,
      //false, falta por completar
      campoPisosActivo: true,
      campoHabitacionesActivo: false,
      campoImagenActivo: false,
      //variables para mostrar el formulario correspondiente a la barra de navegabilidad
      //false:no se oculta, true: se oculta
      ocultarFormularioPiso: false,
      ocultarFormularioHabitacion: true,
      ocultarFormularioImagen: true,
      //Errores en el formulario del piso
      provinciaVacia: false,
      provinciaNoSeleccionada: true,
      numeroHabitacionesVacias: false,
      plantaVacia: false,
      erroresSuperficie: false,
      generoVacio: false,
      numeroBanosVacios: false,
      habitacionesLibresVacio: false,
      habitacionesLibresMtotales: false,
      municipioVacio: false,
      direccionVacia: false,
      tipoViaVacio: false,
      nombreViaVacio: false,
      numeroViaVacio: false,
      codigoPostalValido: false,
      errorBusquedaDireccion: false,
      errorNombre: false,
      errorEmail: false,
      errorTelefono: false,
      errores: false, //variable para ver si hay errores y no mandar la peticion al backend
      //errores en el formulario de creacion de la o las habitaciones
      habitacion1Error: {
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
      habitacion2Error: {
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
      habitacion3Error: {
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
      habitacion4Error: {
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
      habitacion5Error: {
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
      erroresHabitacion: false, //variable que evita la llamada al backend en caso de haber errores
      email: "",
      nombre: "",
      telefono: "",
      opcionesPisoNormas: [],
      generoElegido: "",
      opcionesGenero: [],
      normasPiso: [],
      edadMinima: 18,
      edadMaxima: 99,
      normasElegidas: [],
      superficie: 0,
      nBanos: 0,
      nHabitaciones: 0,
      habitacionesLibres: 0,
      codigoPostal: 0,
      provincia: "",
      municipio: "",
      direccion: "",
      direccionPartida: {
        tipoVia: "",
        nombreVia: "",
        numeroVia: "",
      },
      zoom: 12,
      iconWidth: 25,
      iconHeight: 40,
      piso: 0,
      opciones: [], //Opciones elegidas del piso
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
      provincias: [],
      municipios: [],
      marker: null,
      center: [43.36561279159338, -8.411826105940095],
      habitaciones: [],
      map: null,
    };
  },
  methods: {
    setupLeafletMap: function () {
      this.map = L.map("mapContainer").setView(this.center, 13);
      L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
        attribution:
          '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }).addTo(this.map);
      L.marker([50.505, 30.57]).addTo(this.map);
      this.marker = L.marker([43.36561279159338, -8.411826105940095], {
        draggable: true,
      }).addTo(this.map);
      this.map.on("click", this.moveMarker);
    },
    moveMarker(event) {
      this.marker.setLatLng(event.latlng);
    },
    async colocarMakerPorDireccion() {
      this.errorBusquedaDireccion = false;
      if (this.comprobarDireccion() === false) {
        let linkBusqueda =
          "https://nominatim.openstreetmap.org/?street=" +
          this.direccionPartida.numeroVia +
          "+" +
          this.direccionPartida.tipoVia +
          "+" +
          this.direccionPartida.nombreVia +
          "&country=Spain" +
          "&postalcode=" +
          this.codigoPostal +
          "&format=json";
        let busquedaOSM = await fetch(linkBusqueda).then((response) =>
          response.json()
        );
        if (busquedaOSM.length === 0 || busquedaOSM.length > 1) {
          this.errorBusquedaDireccion = true;
        } else {
          this.marker.setLatLng([busquedaOSM[0].lat, busquedaOSM[0].lon]);
          this.map.setView(
            new LatLng(busquedaOSM[0].lat, busquedaOSM[0].lon),
            13
          );
        }
      }
    },
    async getUsuarioLogueado() {
      let account = await AccountRepository.getAccount();
      let usuario = await AccountRepository.findById(account.id);
      this.email = usuario.email;
      this.nombre = usuario.nombre;
      this.telefono = usuario.telefono;
      await this.opcionesPiso();
    },
    async opcionesPiso() {
      this.opcionesPisoNormas = await PisoRepository.getOpcionesPisoNormas();
      for (let i = 0; i < this.opcionesPisoNormas.length; i++) {
        let norma = {
          norma: this.opcionesPisoNormas.at(i),
          seleccionada: false,
        };
        this.normasPiso.push(norma);
      }
      this.opcionesGenero = await PisoRepository.getOpcionesPisoGenero();
      await this.getProvincias();
    },
    async getProvincias() {
      let provinciasYMunicipios = await PisoRepository.getProvincias();

      for (let i = 0; i < provinciasYMunicipios.length; i++) {
        if (
          this.provincias.includes(provinciasYMunicipios[i].provincia) === false
        ) {
          this.provincias.push(provinciasYMunicipios[i].provincia);
        }
      }
      await this.getOpcionesPiso();
    },
    async getOpcionesPiso() {
      this.opcionesPisoVar = await PisoRepository.getOpcionesPiso();
      await this.getOpcionesHabitacion();
    },
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
    async crearPiso() {
      this.habitacionesLibresMtotales = false;
      this.provinciaVacia = false;
      this.numeroHabitacionesVacias = false;
      this.erroresSuperficie = false;
      this.generoVacio = false;
      this.plantaVacia = false;
      this.numeroBanosVacios = false;
      this.habitacionesLibresVacio = false;
      this.codigoPostalValido = false;
      this.municipioVacio = false;
      this.direccionVacia = false;
      this.errores = false;
      this.erroresSuperficie = false;
      const posicion = {
        type: "Point",
        coordinates: [this.marker.getLatLng().lat, this.marker.getLatLng().lng],
      };
      for (let i = 0; i < this.normasPiso.length; i++) {
        if (this.normasPiso.at(i).seleccionada === true) {
          this.opciones.push(this.normasPiso.at(i).norma);
        }
      }
      for (let i = 0; i < this.opcionesPisoVar.length; i++) {
        if (this.opcionesPisoVar.at(i).seleccionada === true) {
          this.opciones.push(this.opcionesPisoVar.at(i));
        }
      }
      const piso = {
        posicion: posicion,
        provincia: this.provincia,
        codigoPostal: this.codigoPostal,
        municipio: this.municipio,
        direccion:
          this.direccionPartida.tipoVia +
          " " +
          this.direccionPartida.nombreVia +
          "," +
          this.direccionPartida.numeroVia,
        piso: this.piso,
        nHabitaciones: this.nHabitaciones,
        nBanos: this.nBanos,
        mCuadrados: this.superficie,
        edadMinima: this.edadMinima,
        edadMaxima: this.edadMaxima,
        opciones: this.opciones,
        nombreUsuario: this.nombre,
        emailUsuario: this.email,
        telefonoUsuario: this.telefono,
      };
      return await PisoRepository.crearPiso(piso);
    },
    log(a) {
      console.log(a);
    },
    mostrarHabitaciones() {
      if (this.comprobarPiso() === true) {
        this.ocultarFormularioPiso = true;
        this.ocultarFormularioImagen = true;
        this.ocultarFormularioHabitacion = false;
        this.campoHabitacionesActivo = true;
        window.scrollTo(0, 0);
      } else {
        window.scrollTo(0, 0);
      }
    },
    pasarAImagenes(habitaciones) {
      this.habitaciones = habitaciones;
      this.ocultarFormularioPiso = true;
      this.ocultarFormularioHabitacion = true;
      this.ocultarFormularioImagen = false;
      this.campoHabitacionesActivo = true;
      this.campoImagenActivo = true;
    },
    comprobarPiso() {
      const toast = useToast();
      this.habitacionesLibresMtotales = false;
      this.provinciaVacia = false;
      this.numeroHabitacionesVacias = false;
      this.erroresSuperficie = false;
      this.generoVacio = false;
      this.plantaVacia = false;
      this.numeroBanosVacios = false;
      this.habitacionesLibresVacio = false;
      this.codigoPostalValido = false;
      this.municipioVacio = false;
      this.tipoViaVacio = false;
      this.numeroViaVacio = false;
      this.nombreViaVacio = false;
      this.errores = false;
      this.erroresSuperficie = false;
      this.errorTelefono = false;
      this.errorNombre = false;
      this.errorEmail = false;
      if (this.superficie === "" || this.superficie <= 0) {
        this.erroresSuperficie = true;
        this.errores = true;
      }
      if (this.generoElegido === "") {
        this.generoVacio = true;
        this.errores = true;
      }
      if (this.piso === "" || this.piso <= 0) {
        this.plantaVacia = true;
        this.errores = true;
      }
      if (this.nHabitaciones === 0) {
        this.numeroHabitacionesVacias = true;
        this.errores = true;
      }
      if (this.nBanos === 0) {
        this.numeroBanosVacios = true;
        this.errores = true;
      }
      if (this.habitacionesLibres === 0) {
        this.habitacionesLibresVacio = true;
        this.errores = true;
      }
      if (this.nHabitaciones < this.habitacionesLibres) {
        this.habitacionesLibresMtotales = true;
        this.errores = true;
      }
      if (this.provincia === "") {
        this.provinciaVacia = true;
        this.errores = true;
      }
      if (this.municipio === "") {
        this.municipioVacio = true;
        this.errores = true;
      }
      if (this.comprobarDireccion() === true) {
        this.errores = true;
      }
      if (
        !this.email.match(
          /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        )
      ) {
        this.errorEmail = true;
        this.errores = true;
      }
      if (this.nombre === "") {
        this.errorNombre = true;
        this.errores = true;
      }
      if (String(this.telefono).length !== 9) {
        this.errorTelefono = true;
        this.errores = true;
      }
      if (this.errores === true) {
        toast.error(
          "Corrija los errores del formulario antes de crear el piso",
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
        return false;
      }
      return true;
    },
    volverAPiso() {
      this.ocultarFormularioPiso = false;
      this.ocultarFormularioHabitacion = true;
      this.campoHabitacionesActivo = false;
      window.scrollTo(0, 0);
    },
    async finalizarFormulario(habitaciones, imagenesPiso) {
      let piso = await this.crearPiso();
      for (let i = 0; i < habitaciones.length; i++) {
        const habitacion = {
          inicioContrato: this.habitaciones[i].inicioContrato,
          finContrato: this.habitaciones[i].finContrato,
          titulo: this.habitaciones[i].titulo,
          descripcion: this.habitaciones[i].descripcion,
          mCuadrados: this.habitaciones[i].mCuadrados,
          precio: this.habitaciones[i].precio,
          gastosCompartidos: this.habitaciones[i].gastosCompartidos,
          fianza: this.habitaciones[i].fianza,
          piso: Object,
          opciones: [],
        };
        for (let x = 0; x < habitaciones[i].opciones.length; x++) {
          if (habitaciones[i].opciones[x].seleccionada)
            habitacion.opciones.push(habitaciones[i].opciones[x].opcionObjeto);
        }
        habitacion.piso = piso;
        let habitacion1Creada = await HabitacionRepository.crearHabitacion(
          habitacion
        );
        await this.guardarImagenes(
          habitacion1Creada.id,
          this.habitaciones[i].imagenes
        );
        await this.guardarImagenes(habitacion1Creada.id, imagenesPiso);
      }
      this.$router.push("/");
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
    comprobarDireccion() {
      this.codigoPostalValido = false;
      this.numeroViaVacio = false;
      this.nombreViaVacio = false;
      this.tipoViaVacio = false;
      let error = false;
      const codigoPostalRegex = /^(?:0[1-9]\d{3}|[1-4]\d{4}|5[0-2]\d{3})$/;
      if (
        this.codigoPostal <= 0 ||
        this.codigoPostal.toString().length !== 5 ||
        !codigoPostalRegex.test(this.codigoPostal)
      ) {
        this.codigoPostalValido = true;
        error = true;
      }
      if (this.direccionPartida.numeroVia === "") {
        this.numeroViaVacio = true;
        error = true;
      }
      if (this.direccionPartida.nombreVia === "") {
        this.nombreViaVacio = true;
        error = true;
      }
      if (this.direccionPartida.tipoVia === "") {
        this.tipoViaVacio = true;
        error = true;
      }
      return error;
    },
  },
};
</script>

<style scoped>
.card {
  width: 70%;
  margin: 1rem auto;
}
.card.ocultar {
  display: none;
}
select.errorsSelect {
  border-color: red;
}
.form-control.errorsSelect {
  border-color: red;
}
textarea.errorsInput {
  border-color: red;
}
input.errorsInput {
  border-color: red;
}
#mapContainer {
  width: 100%;
  height: 50vh;
}
/*progressbar*/
#progressbar {
  margin-bottom: 30px;
  overflow: hidden;
  /*CSS counters to number the steps*/
  counter-reset: step;
}

#progressbar li {
  list-style-type: none;
  color: darkslategray;
  text-transform: uppercase;
  font-size: 9px;
  width: 33.33%;
  float: left;
  position: relative;
  letter-spacing: 1px;
}

#progressbar li:before {
  content: counter(step);
  counter-increment: step;
  width: 24px;
  height: 24px;
  line-height: 26px;
  display: block;
  font-size: 12px;
  color: white;
  background: black;
  border-radius: 25px;
  margin: 0 auto 10px auto;
}

/*progressbar connectors*/
#progressbar li:after {
  content: "";
  width: 100%;
  height: 2px;
  background: green;
  position: absolute;
  left: -50%;
  top: 9px;
  z-index: -1; /*put it behind the numbers*/
}

#progressbar li:first-child:after {
  /*connector not needed before the first step*/
  content: none;
}

/*marking active/completed steps green*/
/*The number of the step and the connector before it = green*/
#progressbar li.active:before {
  background: green;
  color: white;
}
#progressbar li.active:after {
  background: green;
  color: white;
}
img {
  margin: 3px;
}
img.selected {
  box-shadow: 0px 12px 22px 1px red;
}
.remove-image {
  display: none;
  position: absolute;
  top: -10px;
  right: -10px;
  border-radius: 10em;
  padding: 2px 6px 3px;
  text-decoration: none;
  font: 700 21px/20px sans-serif;
  background: #555;
  border: 3px solid #fff;
  color: #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.5), inset 0 2px 4px rgba(0, 0, 0, 0.3);
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
  -webkit-transition: background 0.5s;
  transition: background 0.5s;
}
.remove-image:hover {
  background: #e54e4e;
  padding: 3px 7px 5px;
  top: -11px;
  right: -11px;
}
.remove-image:active {
  background: #e54e4e;
  top: -10px;
  right: -11px;
}
</style>
