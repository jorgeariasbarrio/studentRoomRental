import HTTP from "../common/http";

export default {
  async getFavoritos() {
    return (await HTTP.get("usuarios/favoritos")).data;
  },
  async getAllAccounts() {
    return (await HTTP.get("usuarios")).data;
  },
  async modificarFavorito(idHabitacion) {
    const params = new URLSearchParams();
    if (idHabitacion) params.append("idHabitacion", idHabitacion);
    const paramsStr = params.toString();
    let url = "usuarios/modificarFavorito";
    if (paramsStr) url += "?" + paramsStr;
    const response = await HTTP.put(url);
    return response.data;
  },
  async cambiarEstadoUsuario(id) {
    return (await HTTP.put("usuarios/cambiarEstado/" + id)).data;
  },
};
