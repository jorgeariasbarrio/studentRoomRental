package es.udc.tfg.server.model.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alquiler")
public class Alquiler {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alquiler_generator")
  @SequenceGenerator(name = "alquiler_generator", sequenceName = "alquiler_seq")
  private Long id;

  private LocalDate fechaInicio;

  private LocalDate fechaFin;

  @ManyToOne(fetch= FetchType.LAZY, optional = true)
  private Usuario usuarioAlquiler;

  @ManyToOne(fetch=FetchType.LAZY, optional = false)
  private Habitacion habitacionAlquiler;

  public Alquiler(){}

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

  public Usuario getUsuarioAlquiler() {
    return usuarioAlquiler;
  }

  public void setUsuarioAlquiler(Usuario usuarioAlquiler) {
    this.usuarioAlquiler = usuarioAlquiler;
  }

  public Habitacion getHabitacionAlquiler() {
    return habitacionAlquiler;
  }

  public void setHabitacionAlquiler(Habitacion habitacionAlquiler) {
    this.habitacionAlquiler = habitacionAlquiler;
  }
}
