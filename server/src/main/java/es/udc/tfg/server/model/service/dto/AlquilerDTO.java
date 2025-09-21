package es.udc.tfg.server.model.service.dto;


import es.udc.tfg.server.model.domain.Alquiler;

import java.time.LocalDate;

public class AlquilerDTO {

  private Long id;

  private LocalDate fechaInicio;

  private LocalDate fechaFin;

  private UsuarioDTOPublico usuario;

  private HabitacionDTO habitacion;

  public AlquilerDTO(){}

  public AlquilerDTO(Alquiler alquiler){
    this.id = alquiler.getId();
    this.fechaInicio = alquiler.getFechaInicio();
    this.fechaFin = alquiler.getFechaFin();
    if(alquiler.getUsuarioAlquiler()!=null) {
      this.usuario = new UsuarioDTOPublico(alquiler.getUsuarioAlquiler());
    }
    this.habitacion = new HabitacionDTO(alquiler.getHabitacionAlquiler());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(LocalDate fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public LocalDate getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(LocalDate fechaFin) {
    this.fechaFin = fechaFin;
  }

  public UsuarioDTOPublico getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioDTOPublico usuario) {
    this.usuario = usuario;
  }

  public HabitacionDTO getHabitacion() {
    return habitacion;
  }

  public void setHabitacion(HabitacionDTO habitacion) {
    this.habitacion = habitacion;
  }
}
