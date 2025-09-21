package es.udc.tfg.server.model.service.dto;

public class ActualizarImagenDTO {
  private Long idHabitacion;
  private Long idImagen;
  private boolean portada;

  public ActualizarImagenDTO() {
  }

  public ActualizarImagenDTO(Long idHabitacion, Long idImagen, boolean portada) {
    this.idHabitacion = idHabitacion;
    this.idImagen = idImagen;
    this.portada = portada;
  }

  public Long getIdHabitacion() {
    return idHabitacion;
  }

  public void setIdHabitacion(Long idHabitacion) {
    this.idHabitacion = idHabitacion;
  }

  public Long getIdImagen() {
    return idImagen;
  }

  public void setIdImagen(Long idImagen) {
    this.idImagen = idImagen;
  }

  public boolean isPortada() {
    return portada;
  }

  public void setPortada(boolean portada) {
    this.portada = portada;
  }
}


