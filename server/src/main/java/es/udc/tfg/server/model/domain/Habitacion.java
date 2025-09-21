package es.udc.tfg.server.model.domain;

import es.udc.tfg.server.model.domain.Enumerados.EstadoHabitacion;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "habitacion")
public class Habitacion {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habitacion_generator")
  @SequenceGenerator(name = "habitacion_generator", sequenceName = "habitacion_seq")
  private Long id;

  private String descripcion;

  private String titulo;

  private Integer mCuadrados;

  private LocalDate fechaModificacion;

  private LocalDate fechaCreacion;

  private LocalDate inicioContrato;

  private LocalDate finContrato;

  private Integer precio;

  private Integer gastosCompartidos;

  private boolean gCompIncluidos;

  private Integer fianza;

  @Enumerated(EnumType.STRING)
  private EstadoHabitacion estadoHabitacion;

  @ManyToOne(fetch= FetchType.LAZY, optional = false)
  private Piso piso;

  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Opciones> opciones = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Usuario> usuarioFavorito = new HashSet<>();

  @OneToMany(mappedBy = "habitacion",fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE})
  private Set<Imagen> imagenes = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY)
  private Set<Alquiler> alquileres = new HashSet<>();



  public Habitacion(){}

  public Habitacion(String titulo,String descripcion, Integer mCuadrados, LocalDate fechaCreacion,
                    LocalDate inicioContrato, LocalDate finContrato, Integer precio,
                    Integer gastosCompartidos, boolean gCompIncluidos,
                    Integer fianza, Piso piso) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.mCuadrados = mCuadrados;
    this.fechaCreacion = fechaCreacion;
    this.inicioContrato = inicioContrato;
    this.finContrato = finContrato;
    this.precio = precio;
    this.gastosCompartidos = gastosCompartidos;
    this.gCompIncluidos = gCompIncluidos;
    this.fianza = fianza;
    this.estadoHabitacion = EstadoHabitacion.DISPONIBLE;
    this.piso = piso;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Integer getmCuadrados() {
    return mCuadrados;
  }

  public void setmCuadrados(Integer mCuadrados) {
    this.mCuadrados = mCuadrados;
  }

  public LocalDate getFechaModificacion() {
    return fechaModificacion;
  }

  public void setFechaModificacion(LocalDate fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
  }

  public LocalDate getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDate fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public LocalDate getInicioContrato() {
    return inicioContrato;
  }

  public void setInicioContrato(LocalDate inicioContrato) {
    this.inicioContrato = inicioContrato;
  }

  public LocalDate getFinContrato() {
    return finContrato;
  }

  public void setFinContrato(LocalDate finContrato) {
    this.finContrato = finContrato;
  }

  public Integer getPrecio() {
    return precio;
  }

  public void setPrecio(Integer precio) {
    this.precio = precio;
  }

  public Integer getGastosCompartidos() {
    return gastosCompartidos;
  }

  public void setGastosCompartidos(Integer gastosCompartidos) {
    this.gastosCompartidos = gastosCompartidos;
  }

  public boolean isgCompIncluidos() {
    return gCompIncluidos;
  }

  public void setgCompIncluidos(boolean gCompIncluidos) {
    this.gCompIncluidos = gCompIncluidos;
  }

  public Integer getFianza() {
    return fianza;
  }

  public void setFianza(Integer fianza) {
    this.fianza = fianza;
  }

  public EstadoHabitacion getEstadoHabitacion() {
    return estadoHabitacion;
  }

  public void setEstadoHabitacion(EstadoHabitacion estadoHabitacion) {
    this.estadoHabitacion = estadoHabitacion;
  }

  public Piso getPiso() {
    return piso;
  }

  public void setPiso(Piso piso) {
    this.piso = piso;
  }

  public Set<Opciones> getOpciones() {
    return opciones;
  }

  public void setOpciones(Set<Opciones> opciones) {
    this.opciones = opciones;
  }

  public Set<Imagen> getImagenes() {
    return imagenes;
  }

  public void setImagenes(Set<Imagen> imagenes) {
    this.imagenes = imagenes;
  }

  public Set<Usuario> getUsuarioFavorito() {
    return usuarioFavorito;
  }

  public void setUsuarioFavorito(Set<Usuario> usuarioFavorito) {
    this.usuarioFavorito = usuarioFavorito;
  }

  public Set<Alquiler> getAlquileres() {
    return alquileres;
  }

  public void setAlquileres(Set<Alquiler> alquileres) {
    this.alquileres = alquileres;
  }
}
