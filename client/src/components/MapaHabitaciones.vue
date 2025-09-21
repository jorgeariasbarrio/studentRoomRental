<template>
  <link
    rel="stylesheet"
    href="https://unpkg.com/leaflet@1.9.3/dist/leaflet.css"
    integrity="sha256-kLaT2GOSpHechhsozzB+flnD+zUyjE2LlfWPgU04xyI="
    crossorigin=""
  />
  <div class="btn-group" style="margin: 1%; padding-left: 72%">
    <a href="/" class="btn btn-primary" aria-current="page"
      >Lista Habitaciones</a
    >
    <a class="btn btn-primary active">Mapa Habitaciones</a>
  </div>
  <div class="container">
    <div
      class="col-3"
      style="margin-right: 3%; margin-left: -5%; margin-top: -3%"
    >
      <ColumnaFiltros @filtros="filtros"></ColumnaFiltros>
    </div>
    <div class="col-10">
      <div class="container" id="mapContainer"></div>
    </div>
  </div>
</template>

<script>
import "leaflet/dist/leaflet.css";
import "leaflet/dist/images/marker-shadow.png";
import "@vueform/vueform";
import L from "leaflet";
import HabitacionRepository from "@/repositories/HabitacionRepository";
import ColumnaFiltros from "@/components/ColumnaFiltros";
import CentrosEstudiosRepository from "@/repositories/CentrosEstudiosRepository";
import universidadIcono from "@/images/icons8-university-48.png";
export default {
  name: "MapaHabitaciones",
  mounted() {
    this.getHabitaciones();
  },
  components: { ColumnaFiltros },
  data() {
    return {
      zoom: 12,
      iconWidth: 25,
      iconHeight: 40,
      markers: [],
      center: [43.36561279159338, -8.411826105940095],
      habitaciones: [],
      map: null,
      campus: null,
      distanciaCEstudios: null,
      copiaFiltros: JSON.parse(localStorage.getItem("filtros")),
    };
  },
  methods: {
    async getHabitaciones() {
      if (localStorage.getItem("filtros") !== null) {
        await this.setupLeafletMap();
        await this.filtros(JSON.parse(localStorage.getItem("filtros")));
      } else {
        this.habitaciones = await HabitacionRepository.findAll();
        await this.setupLeafletMap();
      }
    },
    async filtros(filtros) {
      if (localStorage.getItem("filtros").constructor.name === "String") {
        localStorage.removeItem("filtros");
      }
      let filtrosToJSON = JSON.stringify(filtros);
      localStorage.setItem("filtros", filtrosToJSON);
      this.copiaFiltros = JSON.parse(localStorage.getItem("filtros"));
      if (filtros.campus !== null) {
        this.campus = filtros.campus;
        this.habitaciones = await HabitacionRepository.findAllFilter(
          filtros.provincia,
          filtros.distanciaMaxima,
          filtros.campus.id,
          filtros.precioMaximo,
          filtros.numHabitaciones,
          filtros.inicioContrato,
          filtros.finContrato,
          filtros.opciones,
          null,
          null,
          filtros.municipio
        );
      } else {
        this.campus = null;
        this.habitaciones = await HabitacionRepository.findAllFilter(
          filtros.provincia,
          filtros.distanciaMaxima,
          null,
          filtros.precioMaximo,
          filtros.numHabitaciones,
          filtros.inicioContrato,
          filtros.finContrato,
          filtros.opciones,
          null,
          null,
          filtros.municipio
        );
      }
      await this.markersWithFilters();
    },
    async calcularDistanciaCEstudiosPiso(habitacion) {
      if (this.campus !== null || undefined) {
        this.distanciaCEstudios =
          await CentrosEstudiosRepository.calcularDistancia(
            this.campus.localizacion.coordinates,
            habitacion.piso.posicion.coordinates
          );
      }
    },
    setupLeafletMap: async function () {
      this.map = L.map("mapContainer", {
        zoomControl: true,
        zoom: 1,
        zoomAnimation: false,
        fadeAnimation: true,
        markerZoomAnimation: true,
      }).setView(this.center, 13);
      L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
        attribution:
          '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }).addTo(this.map);
      this.markers = L.markerClusterGroup();
      for (let i = 0; i < this.habitaciones.length; i++) {
        let marker = L.marker([
          this.habitaciones.at(i).piso.posicion.coordinates[0],
          this.habitaciones.at(i).piso.posicion.coordinates[1],
        ]);
        this.markers.addLayer(marker);
        let portada = null;
        if (this.habitaciones.at(i).imagenes.length > 0) {
          for (let z = 0; z < this.habitaciones.at(i).imagenes.length; z++) {
            let x = this.habitaciones.at(i).imagenes.at(z);
            if (x.portada === true)
              portada =
                "http://localhost:8080/api/habitaciones/" +
                this.habitaciones.at(i).id +
                "/imagenes/" +
                x.idImagen;
          }
          // Si no establecemos la portada creando el habitacion, cogemos la primera imagen(en caso de que las tenga)
          portada =
            "http://localhost:8080/api/habitaciones/" +
            this.habitaciones.at(i).id +
            "/imagenes/" +
            this.habitaciones.at(i).imagenes[0].idImagen;
        }
        if (portada === null) {
          portada = "@/images/placeHolderHabitacion.png";
        }
        let habitacionesDisponibles = await HabitacionRepository.findAll(
          this.habitaciones.at(i).piso.id
        );
        let inicioContrato = new Date(
          this.habitaciones.at(i).inicioContrato
        ).toLocaleDateString();
        let finContrato = new Date(
          this.habitaciones.at(i).finContrato
        ).toLocaleDateString();
        if (inicioContrato === new Date().toLocaleDateString()) {
          inicioContrato = "Ahora";
        }
        marker.bindPopup(
          "<img src=" +
            portada +
            " style='width:250px;height:150px;'</img><br>" +
            "<br /><a href=" +
            "/" +
            this.habitaciones.at(i).id +
            " target='_blank'>" +
            "Habitacion en " +
            this.habitaciones.at(i).piso.direccion +
            "</a><br/>" +
            "<h6>" +
            "Habitaciones disponibles: " +
            habitacionesDisponibles.length +
            "</h6>" +
            "<h6>" +
            this.habitaciones.at(i).precio +
            "€" +
            "</h6>" +
            "<h6>" +
            "Disponibilidad: " +
            inicioContrato +
            " - " +
            finContrato +
            "</h6>"
        );
      }
      this.map.addLayer(this.markers);
    },
    async markersWithFilters() {
      this.map.removeLayer(this.markers);
      this.markers = L.markerClusterGroup();
      if (this.campus !== null) {
        var myIcon = L.icon({
          iconUrl: universidadIcono,
          iconSize: [48, 48],
          iconAnchor: [48, 48],
          popupAnchor: [-25, -30],
        });
        // No consigo que sea el icon de la carpeta images
        let marker = L.marker(
          [
            this.campus.localizacion.coordinates[0],
            this.campus.localizacion.coordinates[1],
          ],
          { icon: myIcon }
        );
        marker.bindPopup(this.campus.nombre);
        marker.setPopupContent(this.campus.nombre);
        this.markers.addLayer(marker);
      }
      for (let i = 0; i < this.habitaciones.length; i++) {
        let marker = L.marker([
          this.habitaciones.at(i).piso.posicion.coordinates[0],
          this.habitaciones.at(i).piso.posicion.coordinates[1],
        ]);
        this.markers.addLayer(marker);
        let portada = null;
        if (this.habitaciones.at(i).imagenes.length > 0) {
          for (let z = 0; z < this.habitaciones.at(i).imagenes.length; z++) {
            let x = this.habitaciones.at(i).imagenes.at(z);
            if (x.portada === true)
              portada =
                "http://localhost:8080/api/habitaciones/" +
                this.habitaciones.at(i).id +
                "/imagenes/" +
                x.idImagen;
          }
          // Si no establecemos la portada creando el habitacion, cogemos la primera imagen(en caso de que las tenga)
          portada =
            "http://localhost:8080/api/habitaciones/" +
            this.habitaciones.at(i).id +
            "/imagenes/" +
            this.habitaciones.at(i).imagenes[0].idImagen;
        }
        if (portada === null) {
          portada = "@/images/placeHolderHabitacion.png";
        }
        let habitacionesDisponibles = await HabitacionRepository.findAll(
          this.habitaciones.at(i).piso.id
        );

        let inicioContrato = new Date(
          this.habitaciones.at(i).inicioContrato
        ).toLocaleDateString();
        let finContrato = new Date(
          this.habitaciones.at(i).finContrato
        ).toLocaleDateString();
        if (inicioContrato === new Date().toLocaleDateString()) {
          inicioContrato = "Ahora";
        }
        await this.calcularDistanciaCEstudiosPiso(this.habitaciones.at(i));
        let popup =
          "<img src=" +
          portada +
          " style='width:250px;height:150px;'</img><br>" +
          "<br /><a href=" +
          "/habitaciones/" +
          this.habitaciones.at(i).id +
          " target='_blank'>" +
          "Habitacion en " +
          this.habitaciones.at(i).piso.direccion +
          "</a><br/>" +
          "<h6>" +
          "Habitaciones disponibles: " +
          habitacionesDisponibles.length +
          "</h6>" +
          "<h6>" +
          this.habitaciones.at(i).precio +
          "€" +
          "</h6>" +
          "<h6>" +
          "Disponibilidad: " +
          inicioContrato +
          " - " +
          finContrato +
          "</h6>";
        if (this.distanciaCEstudios !== null) {
          popup +=
            "<h6>" +
            "Distancia al centro de estudios: " +
            Math.trunc(this.distanciaCEstudios / 1000) +
            " km" +
            "</h6>";
        }
        marker.bindPopup(popup);
      }
      this.map.addLayer(this.markers);
    },
  },
};
</script>

<style scoped>
#mapContainer {
  width: 100%;
  height: 100%;
}
.container {
  display: flex;
}
</style>
