package es.udc.tfg.server.model.service.dto;

import com.vividsolutions.jts.geom.Point;
import es.udc.tfg.server.model.domain.Piso;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class PisoConHabitacionesDTO {

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

  private List<HabitacionPisoDTO> habitaciones = new ArrayList<>();

  private List<OpcionesDTO> opciones = new ArrayList<>();


  public PisoConHabitacionesDTO(){

  }

  public PisoConHabitacionesDTO(Piso piso) {
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
    piso.getHabitaciones().forEach(h -> {
      this.habitaciones.add(new HabitacionPisoDTO(h));
    });
    piso.getOpciones().forEach(o -> {
      this.opciones.add(new OpcionesDTO(o));
    });

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

  public List<HabitacionPisoDTO> getHabitaciones() {
    return habitaciones;
  }

  public void setHabitaciones(List<HabitacionPisoDTO> habitaciones) {
    this.habitaciones = habitaciones;
  }

  public List<OpcionesDTO> getOpciones() {
    return opciones;
  }

  public void setOpciones(List<OpcionesDTO> opciones) {
    this.opciones = opciones;
  }
}
