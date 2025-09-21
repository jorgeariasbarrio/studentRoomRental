<template>
  <div class="modal-backdrop">
    <div class="modal">
      <section class="modal-body">
        <slot name="body">
          <div class="container" id="mapContainer2" ref="mapContainer2"></div>
        </slot>
      </section>
      <footer class="modal-footer">
        <button type="button" class="btn-green" @click="close">
          Cerrar mapa
        </button>
      </footer>
    </div>
  </div>
</template>

<script>
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import HabitacionRepository from "@/repositories/HabitacionRepository";

export default {
  name: "MapaDetalle",
  data() {
    return {
      zoom: 12,
      iconWidth: 25,
      iconHeight: 40,
      markers: [],
      center: [],
      map: null,
      habitacion: null,
    };
  },
  mounted() {
    this.setupLeafletMap();
  },
  methods: {
    async emitirResize() {
      setTimeout(function () {
        window.dispatchEvent(new Event("resize"));
      }, 250);
    },
    close() {
      this.$emit("close");
    },
    setupLeafletMap: async function () {
      this.habitacion = await HabitacionRepository.findHabitacion(
        this.$route.params.idHabitacion
      );
      this.center = [
        this.habitacion.piso.posicion.coordinates[0],
        this.habitacion.piso.posicion.coordinates[1],
      ];
      this.map = L.map("mapContainer2", {
        zoomControl: true,
        zoom: 13,
        zoomAnimation: false,
        fadeAnimation: true,
        markerZoomAnimation: true,
      }).setView(this.center, 20);
      // Cambiar este tile
      //Leaflet provider demo para buscar el tile
      // cambiar tile size
      L.tileLayer(
        "https://{s}.tile-cyclosm.openstreetmap.fr/cyclosm/{z}/{x}/{y}.png",
        {
          maxZoom: 19,
          attribution:
            '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        }
      ).addTo(this.map);
      let marker = L.marker([
        this.habitacion.piso.posicion.coordinates[0],
        this.habitacion.piso.posicion.coordinates[1],
      ]);
      let portada = null;
      if (this.habitacion.imagenes.length > 0) {
        for (let z = 0; z < this.habitacion.imagenes.length; z++) {
          let x = this.habitacion.imagenes.at(z);
          if (x.portada === true)
            portada =
              "http://localhost:8080/api/habitaciones/" +
              this.habitacion.id +
              "/imagenes/" +
              x.idImagen;
        }
        // Si no establecemos la portada creando el habitacion, cogemos la primera imagen(en caso de que las tenga)
        portada =
          "http://localhost:8080/api/habitaciones/" +
          this.habitacion.id +
          "/imagenes/" +
          this.habitacion.imagenes[0].idImagen;
      }
      if (portada === null) {
        portada = "@/images/placeHolderHabitacion.png";
      }
      let habitacionesDisponibles = await HabitacionRepository.findAll(
        this.habitacion.piso.id
      );
      let inicioContrato = new Date(
        this.habitacion.inicioContrato
      ).toLocaleDateString();
      let finContrato = new Date(
        this.habitacion.finContrato
      ).toLocaleDateString();
      if (inicioContrato === new Date().toLocaleDateString()) {
        inicioContrato = "Ahora";
      }
      marker
        .bindPopup(
          "<img src=" +
            portada +
            " style='width:250px;height:150px;'</img><br>" +
            "<br /><a href=" +
            "/habitaciones/" +
            this.habitacion.id +
            " target='_blank'>" +
            "Habitacion en " +
            this.habitacion.piso.direccion +
            "</a><br/>" +
            "<h6>" +
            "Habitaciones disponibles: " +
            habitacionesDisponibles.length +
            "</h6>" +
            "<h6>" +
            this.habitacion.precio +
            "€" +
            "</h6>" +
            "<h6>" +
            "Disponibilidad: " +
            inicioContrato +
            " - " +
            finContrato +
            "</h6>"
        )
        .openPopup();
      this.map.addLayer(marker);
      this.map.invalidateSize();
    },
  },
};
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: #ffffff;
  box-shadow: 2px 2px 20px 1px;
  overflow-x: auto;
  display: flex;
  flex-direction: column;
}

.modal-header,
.modal-footer {
  padding: 15px;
  display: flex;
}

.modal-header {
  position: relative;
  border-bottom: 1px solid #eeeeee;
  color: #4aae9b;
  justify-content: space-between;
}

.modal-footer {
  border-top: 1px solid #eeeeee;
  flex-direction: column;
  justify-content: flex-end;
}

.modal-body {
  position: relative;
  padding: 20px 10px;
}

.btn-close {
  position: absolute;
  top: 0;
  right: 0;
  border: none;
  font-size: 20px;
  padding: 10px;
  cursor: pointer;
  font-weight: bold;
  color: #4aae9b;
  background: transparent;
}

.btn-green {
  color: white;
  background: #4aae9b;
  border: 1px solid #4aae9b;
  border-radius: 2px;
}
#mapContainer2 {
  width: 100%;
  height: 500px;
}
</style>
