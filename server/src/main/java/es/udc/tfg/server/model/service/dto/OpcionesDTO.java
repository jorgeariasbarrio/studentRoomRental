package es.udc.tfg.server.model.service.dto;

import com.vividsolutions.jts.geom.Point;
import es.udc.tfg.server.model.domain.Opciones;
import es.udc.tfg.server.model.domain.Piso;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class OpcionesDTO {

  private Long id;

  @NotEmpty
  private String nombre;

  @NotEmpty
  private String icono;

  @NotEmpty
  private String discriminante;


  public OpcionesDTO(){

  }

  public OpcionesDTO(Opciones opcion) {
    this.id = opcion.getId();
    this.nombre = opcion.getNombre();
    this.icono = opcion.getIcono();
    this.discriminante = opcion.getDiscriminante().name();
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

  public String getDiscriminante() {
    return discriminante;
  }

  public void setDiscriminante(String discriminante) {
    this.discriminante = discriminante;
  }
}
