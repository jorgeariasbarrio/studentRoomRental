package es.udc.tfg.server.model.domain;

import javax.persistence.*;

import es.udc.tfg.server.model.domain.Enumerados.OpcionesDiscriminantes;
import es.udc.tfg.server.model.domain.Enumerados.Provincias;
import com.vividsolutions.jts.geom.Point;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "opciones")
public class Opciones {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opciones_generator")
  @SequenceGenerator(name = "opciones_generator", sequenceName = "opciones_seq")
  private Long id;

  private String nombre;

  private String icono;
  @Enumerated(EnumType.STRING)
  private OpcionesDiscriminantes discriminante;

  @ManyToMany(mappedBy = "opciones", fetch = FetchType.LAZY)
  private Set<Piso> pisos = new HashSet<>();


  public Opciones(){}

  public Opciones(String nombre, String icono, OpcionesDiscriminantes discriminante){
    this.nombre = nombre;
    this.icono = icono;
    this.discriminante = discriminante;
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

  public String getIcono() {
    return icono;
  }

  public void setIcono(String icono) {
    this.icono = icono;
  }

  public OpcionesDiscriminantes getDiscriminante() {
    return discriminante;
  }

  public void setDiscriminante(OpcionesDiscriminantes discriminante) {
    this.discriminante = discriminante;
  }

  public Set<Piso> getPisos() {
    return pisos;
  }

  public void setPisos(Set<Piso> pisos) {
    this.pisos = pisos;
  }
}
