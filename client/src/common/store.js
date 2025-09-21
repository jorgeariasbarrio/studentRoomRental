import { ref } from "vue";

const store = ref({
  state: {
    user: {
      authority: "",
      login: "",
      logged: false,
    },
    busquedaInicial: {
      mostrar: true,
    },
    storeFiltros: {
      filtros: {
        provincia: null,
        municipio: null,
        campus: null,
        distanciaMaxima: null,
        numeroHabitacionesVacias: null,
        opciones: null,
      },
    },
  },
});

export { getStore };

function getStore() {
  return store.value;
}
