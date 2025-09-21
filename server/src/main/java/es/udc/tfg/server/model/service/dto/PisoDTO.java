package es.udc.tfg.server.model.service.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.domain.Piso;
import com.vividsolutions.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

public class PisoDTO {

  private Long id;


  @NotNull
  private Point posicion;

  @NotEmpty
  private String provincia;

  @NotNull
  private Integer codigoPostal;

  @NotEmpty
  private String municipio;

  @NotEmpty
  private String direccion;

  @NotNull
  private Integer piso;

  @NotNull
  private Integer nBanos;

  @NotNull
  private Integer nHabitaciones;

  @NotNull
  private Integer mCuadrados;

  @NotNull
  private Integer edadMinima;
  @NotNull
  private Integer edadMaxima;

  private UsuarioDTOPublico usuario;

  private String nombreUsuario;

  private String emailUsuario;

  private String telefonoUsuario;


  private List<OpcionesDTO> opciones = new ArrayList<>();


  public PisoDTO(){

  }

  public PisoDTO(Piso piso) {
    this.id = piso.getId();
    this.posicion = piso.getPosicion();
    this.provincia = piso.getProvincia();
    this.codigoPostal = piso.getCodigoPostal();
    this.piso = piso.getPiso();
    this.municipio = piso.getMunicipio();
    this.direccion = piso.getDireccion();
    this.nBanos = piso.getN_banos();
    this.nHabitaciones = piso.getN_habitacion();
    this.mCuadrados = piso.getM_cuadrados();
    this.edadMinima = piso.getEdadMinima();
    this.edadMaxima = piso.getEdadMaxima();
    this.usuario = new UsuarioDTOPublico(piso.getUsuario());
    piso.getOpciones().forEach(o -> {
      this.opciones.add(new OpcionesDTO(o));
    });
    this.nombreUsuario = piso.getNombreUsuario();
    this.emailUsuario = piso.getEmailUsuario();
    this.telefonoUsuario = piso.getTelefonoUsuario();
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

  public Integer getnBanos() {
    return nBanos;
  }

  public void setnBanos(Integer nBanos) {
    this.nBanos = nBanos;
  }

  public Integer getnHabitaciones() {
    return nHabitaciones;
  }

  public void setnHabitaciones(Integer nHabitaciones) {
    this.nHabitaciones = nHabitaciones;
  }

  public Integer getmCuadrados() {
    return mCuadrados;
  }

  public void setmCuadrados(Integer mCuadrados) {
    this.mCuadrados = mCuadrados;
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

  public UsuarioDTOPublico getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioDTOPublico usuario) {
    this.usuario = usuario;
  }

  public List<OpcionesDTO> getOpciones() {
    return opciones;
  }

  public void setOpciones(List<OpcionesDTO> opciones) {
    this.opciones = opciones;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public String getEmailUsuario() {
    return emailUsuario;
  }

  public void setEmailUsuario(String emailUsuario) {
    this.emailUsuario = emailUsuario;
  }

  public String getTelefonoUsuario() {
    return telefonoUsuario;
  }

  public void setTelefonoUsuario(String telefonoUsuario) {
    this.telefonoUsuario = telefonoUsuario;
  }
}
