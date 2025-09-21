import HTTP from "../common/http";

export default {
  async getPiso(id) {
    return (await HTTP.get(`pisos/` + id)).data;
  },
  async getPisosUsuario(idUsuario) {
    const params = new URLSearchParams();
    if (idUsuario) params.append("idUsuario", idUsuario);
    const paramsStr = params.toString();
    let url = "pisos";
    if (paramsStr) url += "?" + paramsStr;
    const response = await HTTP.get(url);
    return response.data;
  },
  async crearPiso(piso) {
    return (await HTTP.post("pisos", piso)).data;
  },
  async actualizarPiso(piso) {
    return (await HTTP.post("pisos/" + piso.id, piso)).data;
  },
  async anadirHabitacion(habitacion, idPiso) {
    return (await HTTP.post("pisos/anadirHabitacion/" + idPiso, habitacion))
      .data;
  },
  async getOpcionesPisoNormas() {
    return (await HTTP.get("opciones" + "/NORMAS")).data;
  },
  async getProvincias() {
    return (await HTTP.get("pisos/provincias")).data;
  },
  async getMunicipios(provincia) {
    return (await HTTP.get("pisos/provincias/" + provincia)).data;
  },
  async getOpcionesPiso() {
    return (await HTTP.get("opciones" + "/PISO")).data;
  },
  async getOpcionesPisoGenero() {
    return (await HTTP.get("opciones" + "/GENERO")).data;
  },
};
