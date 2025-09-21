package es.udc.tfg.server.model.service.dto;

import com.vividsolutions.jts.geom.Point;
import es.udc.tfg.server.model.domain.CentrosEstudios;

import javax.validation.constraints.NotNull;

public class  CentrosEstudiosDTO {

  private Long id;

  @NotNull
  private Point localizacion;

  @NotNull
  private String nombre;

  @NotNull
  private String municipio;

  @NotNull
  private String provincia;

  public CentrosEstudiosDTO(CentrosEstudios centrosEstudios){
    this.id = centrosEstudios.getId();
    this.nombre = centrosEstudios.getNombre();
    this.localizacion=centrosEstudios.getLocalizacion();
    this.municipio=centrosEstudios.getMunicipio();
    this.provincia=centrosEstudios.getProvincia();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Point getLocalizacion() {
    return localizacion;
  }

  public void setLocalizacion(Point localizacion) {
    this.localizacion = localizacion;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  public String getProvincia() {
    return provincia;
  }

  public void setProvincia(String provincia) {
    this.provincia = provincia;
  }
}
