package es.udc.tfg.server.model.service.dto;

import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.domain.Piso;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HabitacionPisoDTO {

  private Long id;

  @NotNull
  private String titulo;
  @NotNull
  private String descripcion;
  @NotNull
  private Integer mCuadrados;

  private LocalDate fechaModificacion;

  private LocalDate fechaCreacion;
  @NotNull
  private LocalDate inicioContrato;
  @NotNull
  private LocalDate finContrato;
  @NotNull
  private Integer precio;
  @NotNull
  private Integer gastosCompartidos;
  @NotNull
  private boolean gCompIncluidos;
  @NotNull
  private Integer fianza;

  private String estadoHabitacion;

  private List<OpcionesDTO> opciones = new ArrayList<>();

  private Set<ImagenDTO> imagenes = new HashSet<>();

  @NotNull
  private PisoDTO piso;

  public HabitacionPisoDTO(){}

  public HabitacionPisoDTO(Habitacion habitacion){
    this.id = habitacion.getId();
    this.titulo = habitacion.getTitulo();
    this.descripcion= habitacion.getDescripcion();
    this.mCuadrados= habitacion.getmCuadrados();
    this.fechaModificacion= habitacion.getFechaModificacion();
    this.fechaCreacion = habitacion.getFechaCreacion();
    this.inicioContrato = habitacion.getInicioContrato();
    this.finContrato = habitacion.getFinContrato();
    this.estadoHabitacion= habitacion.getEstadoHabitacion().name();
    this.precio = habitacion.getPrecio();
    this.gastosCompartidos = habitacion.getGastosCompartidos();
    this.gCompIncluidos = habitacion.isgCompIncluidos();
    this.piso = new PisoDTO(habitacion.getPiso());
    this.fianza = habitacion.getFianza();
    habitacion.getOpciones().forEach(o -> {
      this.opciones.add(new OpcionesDTO(o));
    });
    if (habitacion.getImagenes() != null) {
      habitacion.getImagenes().forEach(i -> this.imagenes.add(new ImagenDTO(i.getId(), i.getNombre(), i.getPath(), i.isPortada())));
    }

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

  public String getEstadoHabitacion() {
    return estadoHabitacion;
  }

  public void setEstadoHabitacion(String estadoHabitacion) {
    this.estadoHabitacion = estadoHabitacion;
  }

  public List<OpcionesDTO> getOpciones() {
    return opciones;
  }

  public PisoDTO getPiso() {
    return piso;
  }

  public void setPiso(PisoDTO piso) {
    this.piso = piso;
  }

  public void setOpciones(List<OpcionesDTO> opciones) {
    this.opciones = opciones;
  }
  public Set<ImagenDTO> getImagenes() {
    return imagenes;
  }

  public void setImagenes(Set<ImagenDTO> imagenes) {
    this.imagenes = imagenes;
  }

}
