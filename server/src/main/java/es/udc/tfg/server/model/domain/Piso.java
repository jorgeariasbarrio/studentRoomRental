package es.udc.tfg.server.model.domain;

import javax.persistence.*;

import com.vividsolutions.jts.geom.Point;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "piso")
public class Piso {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "piso_generator")
  @SequenceGenerator(name = "piso_generator", sequenceName = "piso_seq")
  private Long id;


  private Point posicion;


  private String provincia;

  private Integer codigoPostal;

  private String municipio;

  private String direccion;

  private Integer piso;

  private Integer n_banos;

  private Integer n_habitacion;

  private Integer m_cuadrados;

  private Integer edadMinima;

  private Integer edadMaxima;

  //Con estas tres variables guardamos la informacion del usuario de contacto(que puede ser otro
  //que el que crea el anuncio.

  private String emailUsuario;

  private String nombreUsuario;

  private String telefonoUsuario;


  @ManyToOne(fetch= FetchType.LAZY, optional = false)
  private Usuario usuario;

  @OneToMany(mappedBy = "piso",fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE})
  private Set<Habitacion> habitaciones = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  private Set<Opciones> opciones = new HashSet<>();

  public Piso() {
  }

  public Piso(Point posicion, String provincia,
              Integer codigoPostal, String municipio, String direccion,
              Integer piso, Integer n_banos,
              Integer n_habitacion, Integer m_cuadrados,Integer edadMinima,
              Integer edadMaxima,String nombreUsuario,String emailUsuario,String telefonoUsuario) {
    this.posicion = posicion;
    this.provincia = provincia;
    this.codigoPostal = codigoPostal;
    this.municipio = municipio;
    this.direccion = direccion;
    this.piso = piso;
    this.n_banos = n_banos;
    this.n_habitacion = n_habitacion;
    this.m_cuadrados = m_cuadrados;
    this.edadMinima = edadMinima;
    this.edadMaxima = edadMaxima;
    this.emailUsuario=emailUsuario;
    this.nombreUsuario=nombreUsuario;
    this.telefonoUsuario=telefonoUsuario;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Point getPosicion() {
    return posicion;
  }

  public void setPosicion(Point posicion) {
    this.posicion = posicion;
  }

  public String getProvincia() {
    return provincia;
  }

  public void setProvincia(String provincia) {
    this.provincia = provincia;
  }

  public Integer getCodigoPostal() {
    return codigoPostal;
  }

  public void setCodigoPostal(Integer codigoPostal) {
    this.codigoPostal = codigoPostal;
  }

  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public Integer getPiso() {
    return piso;
  }

  public void setPiso(Integer piso) {
    this.piso = piso;
  }

  public Integer getN_banos() {
    return n_banos;
  }

  public void setN_banos(Integer n_banos) {
    this.n_banos = n_banos;
  }

  public Integer getN_habitacion() {
    return n_habitacion;
  }

  public void setN_habitacion(Integer n_habitacion) {
    this.n_habitacion = n_habitacion;
  }

  public Integer getM_cuadrados() {
    return m_cuadrados;
  }

  public void setM_cuadrados(Integer m_cuadrados) {
    this.m_cuadrados = m_cuadrados;
  }

  public Integer getEdadMinima() {
    return edadMinima;
  }

  public void setEdadMinima(Integer edadMinima) {
    this.edadMinima = edadMinima;
  }

  public Integer getEdadMaxima() {
    return edadMaxima;
  }

  public void setEdadMaxima(Integer edadMaxima) {
    this.edadMaxima = edadMaxima;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Set<Opciones> getOpciones() {
    return opciones;
  }

  public void setOpciones(Set<Opciones> opciones) {
    this.opciones = opciones;
  }

  public Set<Habitacion> getHabitaciones() {
    return habitaciones;
  }

  public void setHabitaciones(Set<Habitacion> habitaciones) {
    this.habitaciones = habitaciones;
  }

  public String getEmailUsuario() {
    return emailUsuario;
  }

  public void setEmailUsuario(String emailUsuario) {
    this.emailUsuario = emailUsuario;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public String getTelefonoUsuario() {
    return telefonoUsuario;
  }

  public void setTelefonoUsuario(String telefonoUsuario) {
    this.telefonoUsuario = telefonoUsuario;
  }
}
