package es.udc.tfg.server.model.service.dto;

import es.udc.tfg.server.model.domain.Review;

import java.time.LocalDate;

public class ReviewDTO {

  private Long id;


  private String nombre;
  private String comentario;

  private Double puntuacion;

  private String link;

  private boolean completa;

  private LocalDate fechaCreacion;

  private UsuarioDTOPublico usuario;

  private HabitacionDTO habitacion;


  public ReviewDTO(){}

  public ReviewDTO(Review review){
    this.id = review.getId();
    this.nombre= review.getNombre();
    this.comentario = review.getComentario();
    this.puntuacion = review.getPuntuacion();
    this.link = review.getLinkReview();
    this.fechaCreacion = review.getFechaCreacion();
    this.completa = review.isCompleta();
    this.habitacion = new HabitacionDTO(review.getHabitacion());
    if(review.getUsuario() != null) {
      this.usuario = new UsuarioDTOPublico(review.getUsuario());
    }
  }

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

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public boolean isCompleta() {
    return completa;
  }

  public void setCompleta(boolean completa) {
    this.completa = completa;
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

  public LocalDate getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDate fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }
}
