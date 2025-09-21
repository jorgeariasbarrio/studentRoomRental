<template>
  <link
    rel="stylesheet"
    href="https://unpkg.com/leaflet@1.9.3/dist/leaflet.css"
    integrity="sha256-kLaT2GOSpHechhsozzB+flnD+zUyjE2LlfWPgU04xyI="
    crossorigin=""
  />
  <div
    class="card"
    style="width: 80%"
    id="pisoForm"
    v-if="isPisoUsuario === true"
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
                v-model="piso.emailUsuario"
                :class="{ error: errorEmail }"
                type="text"
              />
              <label for="emailUsuario">Email</label>
            </div>
            <h6 v-if="errorEmail" style="color: red">
              Debe indicarse un email válido
            </h6>
          </div>
          <div class="col-sm">
            <div class="form-floating">
              <input
                class="form-control"
                id="nombreUsuario"
                v-model="piso.nombreUsuario"
                :class="{ error: errorNombre }"
                type="text"
              />
              <label for="nombreUsuario">Nombre</label>
            </div>
            <h6 v-if="errorNombre" style="color: red">
              Debe indicarse un nombre
            </h6>
          </div>
          <div class="col-sm">
            <div class="form-floating">
              <input
                id="telefonoInput"
                class="form-control"
                v-model="piso.telefonoUsuario"
                :class="{ error: errorTelefono }"
                type="number"
                style="margin-bottom: 1rem"
              />
              <label for="telefonoInput">Teléfono</label>
            </div>
            <h6 v-if="errorTelefono" style="color: red">
              Debe indicarse un telefono válido
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
              v-model="piso.edadMinima"
              type="number"
            />
            <label for="edadMinima"> Edad mímina</label>
          </div>
          <span class="input-group-text">-</span>
          <div class="form-floating">
            <input
              id="edadMaxima"
              class="form-control"
              v-model="piso.edadMaxima"
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
                  v-model="piso.mCuadrados"
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
                  v-model="piso.piso"
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
                v-model="piso.nHabitaciones"
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
                v-model="piso.nBanos"
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
        </div>
      </div>
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
                v-model="piso.codigoPostal"
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
                    v-model="piso.provincia"
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
                    v-model="piso.municipio"
                    :class="{
                      errorsSelect: municipioVacio,
                    }"
                  >
                    <option v-for="municipio in municipios" :key="municipio.id">
                      {{ municipio.municipio }}
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
          v-for="opcion in opciones"
          :key="opcion.norma.id"
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
    </div>
    <div id="botonCrear" style="margin-top: 20px">
      <button class="btn btn-success" v-on:click="actualizarPiso">
        Confirmar Cambios
      </button>
      <button class="btn btn-danger" v-on:click="this.$router.go(-1)">
        Cancelar
      </button>
    </div>
  </div>
</template>

<script>
import PisoRepository from "@/repositories/PisoRepository";
import "leaflet/dist/leaflet.css";
import "leaflet/dist/images/marker-shadow.png";
import "@vueform/vueform";
import L, { LatLng } from "leaflet";
import { getStore } from "@/common/store";
import { useToast } from "vue-toastification";
export default {
  name: "EditarPiso",
  mounted() {
    this.opcionesPiso();
  },
  watch: {
    provincia: async function (value) {
      let input = document.getElementById("selectMunicipio");
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
      isPisoUsuario: true,
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
      piso: {
        piso: 0,
        edadMinima: 18,
        edadMaxima: 99,
        normasElegidas: [],
        mCuadrados: 0,
        nBanos: 0,
        nHabitaciones: 0,
        habitacionesLibres: 0,
        codigoPostal: 0,
        provincia: "",
        municipio: "",
        direccion: "",
        opciones: [],
        posicion: null,
        emailUsuario: "",
        nombreUsuario: "",
        telefonoUsuario: "",
        usuario: Object,
      },
      email: "",
      nombre: "",
      telefono: "",
      opcionesPisoNormas: [],
      opcionesGenero: [],
      generoElegido: "",
      normasPiso: [],
      direccionPartida: {
        tipoVia: "",
        nombreVia: "",
        numeroVia: 0,
      },
      zoom: 12,
      iconWidth: 25,
      iconHeight: 40,
      altura: 0,
      opciones: [], //Opciones elegidas del piso
      opcionesPisoVar: [
        {
          opcion: Object,
          seleccionada: false,
        },
      ],
      provincias: [],
      municipios: [],
      marker: null,
      map: null,
    };
  },
  methods: {
    setupLeafletMap: function () {
      let center = [
        this.piso.posicion.coordinates[0],
        this.piso.posicion.coordinates[1],
      ];
      this.map = L.map("mapContainer").setView(center, 13);
      L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
        attribution:
          '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }).addTo(this.map);
      this.marker = L.marker(
        [this.piso.posicion.coordinates[0], this.piso.posicion.coordinates[1]],
        {
          draggable: true,
        }
      ).addTo(this.map);
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
          this.piso.codigoPostal +
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
    async esPisoUsuario() {
      const toast = useToast();
      if (this.piso.usuario.login !== getStore().state.user.login) {
        toast.info("No tienes permiso para modificar este anuncio", {
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
        this.$router.go(-1);
        return false;
      } else return true;
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
      for (let i = 0; i < this.opcionesPisoVar.length; i++) {
        let norma = {
          norma: this.opcionesPisoVar.at(i),
          seleccionada: false,
        };
        this.opciones.push(norma);
      }
      await this.getPiso();
    },
    async getPiso() {
      const toast = useToast();
      this.piso = await PisoRepository.getPiso(this.$route.params.idPiso);
      if (this.piso.usuario.login !== getStore().state.user.login) {
        this.isPisoUsuario = false;
        toast.info("No tiene permiso para modificar este anuncio", {
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
        this.$router.go(-1);
      }
      this.municipios = await PisoRepository.getMunicipios(this.piso.provincia);
      this.opcionesGenero = await PisoRepository.getOpcionesPisoGenero();
      for (let i = 0; i < this.piso.opciones.length; i++) {
        if (this.piso.opciones.at(i).discriminante === "GENERO") {
          this.generoElegido = this.piso.opciones.at(i);
        }
      }
      let direccion = this.piso.direccion;
      if (direccion.includes("Rua")) {
        this.direccionPartida.tipoVia = "Rua";
        this.direccionPartida.nombreVia = direccion.substring(
          4,
          direccion.indexOf(",")
        );
        this.direccionPartida.numeroVia = direccion.substring(
          direccion.indexOf(",") + 1
        );
      } else if (direccion.includes("Avenida")) {
        this.direccionPartida.tipoVia = "Avenida";
        this.direccionPartida.nombreVia = direccion.substring(
          8,
          direccion.indexOf(",")
        );
        this.direccionPartida.numeroVia = direccion.substring(
          direccion.indexOf(",") + 1
        );
      } else {
        this.direccionPartida.tipoVia = "Calle";
        this.direccionPartida.nombreVia = direccion.substring(
          6,
          direccion.indexOf(",")
        );
        this.direccionPartida.numeroVia = direccion.substring(
          direccion.indexOf(",") + 1
        );
      }
      for (let x = 0; x < this.piso.opciones.length; x++) {
        for (let z = 0; z < this.normasPiso.length; z++) {
          if (this.piso.opciones.at(x).id === this.normasPiso.at(z).norma.id) {
            this.normasPiso.at(z).seleccionada = true;
          }
        }
      }
      // Hacer lo mismo pero con el bucle de las opciones
      for (let x = 0; x < this.piso.opciones.length; x++) {
        for (let z = 0; z < this.opciones.length; z++) {
          if (this.piso.opciones.at(x).id === this.opciones.at(z).norma.id) {
            this.opciones.at(z).seleccionada = true;
          }
        }
      }
      this.setupLeafletMap();
    },
    async actualizarPiso() {
      const toast = useToast();
      this.piso.opciones = [];
      if (this.comprobarPiso() !== false) {
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
        this.piso.opciones.push(this.generoElegido);
        for (let i = 0; i < this.normasPiso.length; i++) {
          if (this.normasPiso.at(i).seleccionada === true) {
            this.piso.opciones.push(this.normasPiso.at(i).norma);
          }
        }
        for (let i = 0; i < this.opciones.length; i++) {
          if (this.opciones.at(i).seleccionada === true) {
            this.piso.opciones.push(this.opciones.at(i).norma);
          }
        }
        const posicion = {
          type: "Point",
          coordinates: [
            this.marker.getLatLng().lat,
            this.marker.getLatLng().lng,
          ],
        };
        const piso = {
          id: this.piso.id,
          emailUsuario: this.piso.emailUsuario,
          nombreUsuario: this.piso.nombreUsuario,
          telefonoUsuario: this.piso.telefonoUsuario,
          posicion: posicion,
          provincia: this.piso.provincia,
          codigoPostal: this.piso.codigoPostal,
          municipio: this.piso.municipio,
          direccion:
            this.direccionPartida.tipoVia +
            " " +
            this.direccionPartida.nombreVia +
            "," +
            this.direccionPartida.numeroVia,
          piso: this.piso.piso,
          nHabitaciones: this.piso.nHabitaciones,
          nBanos: this.piso.nBanos,
          mCuadrados: this.piso.mCuadrados,
          edadMinima: this.piso.edadMinima,
          edadMaxima: this.piso.edadMaxima,
          opciones: this.piso.opciones,
        };
        await PisoRepository.actualizarPiso(piso);

        toast.info("Se ha actualizado correctamente la información del piso", {
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
      } else {
        window.scrollTo(0, 0);
      }
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
      if (this.piso.mCuadrados === "" || this.piso.mCuadrados <= 0) {
        this.erroresSuperficie = true;
        this.errores = true;
      }
      if (this.generoElegido === "") {
        this.generoVacio = true;
        this.errores = true;
      }
      if (this.piso.piso === "" || this.piso.piso <= 0) {
        this.plantaVacia = true;
        this.errores = true;
      }
      if (this.piso.nHabitaciones === 0) {
        this.numeroHabitacionesVacias = true;
        this.errores = true;
      }
      if (this.piso.nBanos === 0) {
        this.numeroBanosVacios = true;
        this.errores = true;
      }
      if (this.piso.provincia === "") {
        this.provinciaVacia = true;
        this.errores = true;
      }
      if (this.piso.municipio === "") {
        this.municipioVacio = true;
        this.errores = true;
      }
      if (
        !this.piso.emailUsuario.match(
          /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        )
      ) {
        this.errorEmail = true;
        this.errores = true;
      }
      if (this.piso.nombreUsuario === "") {
        this.errorNombre = true;
        this.errores = true;
      }
      if (this.piso.telefonoUsuario.length !== 9) {
        this.errorTelefono = true;
        this.errores = true;
      }
      if (this.comprobarDireccion() === true) {
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
    comprobarDireccion() {
      this.codigoPostalValido = false;
      this.numeroViaVacio = false;
      this.nombreViaVacio = false;
      this.tipoViaVacio = false;
      let error = false;
      const codigoPostalRegex = /^(?:0[1-9]\d{3}|[1-4]\d{4}|5[0-2]\d{3})$/;
      if (
        this.piso.codigoPostal <= 0 ||
        this.piso.codigoPostal.toString().length !== 5 ||
        !codigoPostalRegex.test(this.piso.codigoPostal)
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
select.errorsSelect {
  border-color: red;
}
.form-control.error {
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
</style>
