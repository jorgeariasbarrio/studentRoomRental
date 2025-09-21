import HTTP from "../common/http";

export default {
  async findAll(idPiso) {
    const params = new URLSearchParams();
    if (idPiso) params.append("idPiso", idPiso);
    const paramsStr = params.toString();
    let url = "habitaciones";
    if (paramsStr) url += "?" + paramsStr;
    const response = await HTTP.get(url);
    return response.data;
  },
  async findHabitacion(idHabitacion) {
    return (await HTTP.get("habitaciones/" + idHabitacion)).data;
  },
  async findAllFilter(
    provincia,
    distanciaMaxima,
    idCentroEstudios,
    precioMaximo,
    numHabitaciones,
    inicioContrato,
    finContrato,
    opciones,
    ordenacion,
    idUsuario,
    municipio
  ) {
    const params = new URLSearchParams();
    if (provincia) params.append("provincia", provincia);
    if (distanciaMaxima) params.append("distanciaMaxima", distanciaMaxima);
    if (idCentroEstudios) params.append("idCentroEstudios", idCentroEstudios);
    if (precioMaximo) params.append("precioMaximo", precioMaximo);
    if (numHabitaciones) params.append("numHabitaciones", numHabitaciones);
    if (inicioContrato) params.append("inicioContrato", inicioContrato);
    if (finContrato) params.append("finContrato", finContrato);
    if (opciones !== null) {
      if (opciones.length !== 0) {
        params.append("opciones", opciones.join(","));
      }
    }
    if (ordenacion) params.append("ordenacion", ordenacion);
    if (idUsuario) params.append("idUsuario", idUsuario);
    if (municipio) params.append("municipio", municipio);
    const paramsStr = params.toString();
    let url = "habitaciones/filter";
    if (paramsStr) url += "?" + paramsStr;
    const response = await HTTP.get(url);
    return response.data;
  },

  async getOpcionesHabitacion() {
    return (await HTTP.get("opciones" + "/HABITACION")).data;
  },
  async crearHabitacion(habitacion) {
    return (await HTTP.post("habitaciones", habitacion)).data;
  },

  async modificarHabitacion(habitacion) {
    return (await HTTP.put("habitaciones/" + habitacion.id, habitacion)).data;
  },
  async cambiarEstadoHabitacion(idHabitacion) {
    return (await HTTP.put("habitaciones/cambiarEstado/" + idHabitacion)).data;
  },
  async crearAlquilerInicial(idHabitacion, emailUsuario) {
    const formData = new FormData();
    formData.append("emailUsuario", emailUsuario);
    return (
      await HTTP.post("habitaciones/crearAlquiler/" + idHabitacion, formData)
    ).data;
  },
  async getAlquilerAbierto(idHabitacion) {
    return (await HTTP.get("habitaciones/alquileres/" + idHabitacion)).data;
  },
  async cerrarAlquiler(idHabitacion) {
    return (await HTTP.put("habitaciones/cerrarAlquiler/" + idHabitacion)).data;
  },
  async subirImagen(id, file, esPortada) {
    const formData = new FormData();
    formData.append("imagen", file);
    formData.append("portada", esPortada);
    return (
      await HTTP.post(`habitaciones/${id}/imagenes`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
    ).data;
  },
  async borrarImagen(idHabitacion, idImagen) {
    await HTTP.delete(`habitaciones/${idHabitacion}/imagenes/${idImagen}`);
  },
  async cargarImagenes(id) {
    const response = await HTTP.get(`habitaciones/${id}/imagenes`);
    return response.data;
  },
  async borrarHabitacion(idHabitacion) {
    await HTTP.delete("habitaciones/" + idHabitacion);
  },
  async borrarAlquiler(idAlquiler, idHabitacion) {
    await HTTP.delete(
      "habitaciones/alquiler/" + idAlquiler + "/" + idHabitacion
    );
  },
  async ponerPortada(idPiso, idImagen, body) {
    const response = await HTTP.put(
      `habitaciones/${idPiso}/imagenes/${idImagen}`,
      body
    );
    return response.data;
  },
};
