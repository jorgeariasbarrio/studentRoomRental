<template>
  <div class="container">
    <div
      v-if="centroEstudios != null"
      class="btn-group"
      role="group"
      aria-label="Basic radio toggle button group"
    >
      <span style="margin: 5px">Ordenacion:</span>
      <input
        type="radio"
        class="btn-check"
        name="btnradio"
        id="ordenRecientes"
        autocomplete="off"
        v-on:click="enviarOrdenacion('Mas recientes')"
        checked
      />
      <label class="btn btn-outline-success" for="ordenRecientes"
        >Más recientes</label
      >
      <input
        type="radio"
        class="btn-check"
        name="btnradio"
        id="ordenDistancia"
        autocomplete="off"
        v-on:click="enviarOrdenacion('Menor distancia')"
      />
      <label class="btn btn-outline-success" for="ordenDistancia"
        >Menor distancia</label
      >
      <input
        type="radio"
        class="btn-check"
        name="btnradio"
        id="ordenBaratos"
        autocomplete="off"
        v-on:click="enviarOrdenacion('Mas baratos')"
      />
      <label class="btn btn-outline-success" for="ordenBaratos"
        >Más baratos</label
      >
      <input
        type="radio"
        class="btn-check"
        name="btnradio"
        id="ordenCaros"
        autocomplete="off"
        v-on:click="enviarOrdenacion('Mas caros')"
      />
      <label class="btn btn-outline-success" for="ordenCaros">Más caros</label>
    </div>
    <div
      v-if="centroEstudios === null"
      class="btn-group"
      role="group"
      aria-label="Basic radio toggle button group"
    >
      <span style="margin: 5px">Ordenacion:</span>
      <input
        type="radio"
        class="btn-check"
        name="btnradio"
        id="ordenRecientes"
        autocomplete="off"
        v-on:click="enviarOrdenacion('Mas recientes')"
        checked
      />
      <label class="btn btn-outline-success" for="ordenRecientes"
        >Más recientes</label
      >
      <input
        type="radio"
        class="btn-check"
        name="btnradio"
        id="ordenBaratos"
        autocomplete="off"
        v-on:click="enviarOrdenacion('Mas baratos')"
      />
      <label class="btn btn-outline-success" for="ordenBaratos"
        >Más baratos</label
      >
      <input
        type="radio"
        class="btn-check"
        name="btnradio"
        id="ordenCaros"
        autocomplete="off"
        v-on:click="enviarOrdenacion('Mas caros')"
      />
      <label class="btn btn-outline-success" for="ordenCaros">Más caros</label>
    </div>
  </div>
</template>

<script>
export default {
  name: "ColumnaOrdenacion",
  props: {
    centroEstudios: Object,
  },
  methods: {
    enviarOrdenacion(orden) {
      this.$emit("orden", orden);
    },
  },
};
</script>

<style scoped></style>
