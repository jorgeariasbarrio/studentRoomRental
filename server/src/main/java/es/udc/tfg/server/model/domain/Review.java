package es.udc.tfg.server.model.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="review")
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_generator")
  @SequenceGenerator(name = "review_generator", sequenceName = "review_seq")
  private Long id;

  private String comentario;

  private String nombre;

  private Double puntuacion;

  private LocalDate fechaCreacion;

  private String linkReview;

  private boolean completa;

  @ManyToOne(fetch = FetchType.LAZY)
  private Usuario usuario;

  @ManyToOne(fetch = FetchType.LAZY)
  private Habitacion habitacion;

  public Review(){}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getComentario() {
    return comentario;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }

  public Double getPuntuacion() {
    return puntuacion;
  }

  public void setPuntuacion(Double puntuacion) {
    this.puntuacion = puntuacion;
  }

  public LocalDate getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDate fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public String getLinkReview() {
    return linkReview;
  }

  public void setLinkReview(String linkReview) {
    this.linkReview = linkReview;
  }

  public boolean isCompleta() {
    return completa;
  }

  public void setCompleta(boolean completa) {
    this.completa = completa;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Habitacion getHabitacion() {
    return habitacion;
  }

  public void setHabitacion(Habitacion habitacion) {
    this.habitacion = habitacion;
  }
}
