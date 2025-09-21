import HTTP from "../common/http";

export default {
  async findAll(provincia) {
    const params = new URLSearchParams();
    if (provincia) params.append("provincia", provincia);
    const paramsStr = params.toString();
    let url = "cEstudios";
    if (paramsStr) url += "?" + paramsStr;
    const response = await HTTP.get(url);
    return response.data;
  },
  async calcularDistancia(ubicacionCEstudios, ubicacionPiso) {
    const params = new URLSearchParams();
    if (ubicacionCEstudios)
      params.append("ubicacionCEstudios", ubicacionCEstudios);
    if (ubicacionCEstudios) params.append("ubicacionPiso", ubicacionPiso);
    const paramsStr = params.toString();
    let url = "cEstudios/distancia";
    if (paramsStr) url += "?" + paramsStr;
    const response = await HTTP.get(url);
    return response.data;
  },
};
