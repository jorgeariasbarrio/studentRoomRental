package es.udc.tfg.server.model.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import es.udc.tfg.server.model.domain.Habitacion;

@Entity
@Table(name = "imagen")
public class Imagen {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imagen_generator")
  @SequenceGenerator(name = "imagen_generator", sequenceName = "imagen_seq")
  private Long id;

  private String path;

  private String nombre;

  private boolean portada;

  @ManyToOne(fetch= FetchType.LAZY, optional = false)
  private Habitacion habitacion;

  public Imagen() {
  }

  public Imagen(String path, String nombre) {
    this.path = path;
    this.nombre = nombre;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }


  public boolean isPortada() {
    return portada;
  }

  public void setPortada(boolean portada) {
    this.portada = portada;
  }

  public Habitacion getHabitacion() {
    return habitacion;
  }

  public void setHabitacion(Habitacion habitacion) {
    this.habitacion = habitacion;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }





}

