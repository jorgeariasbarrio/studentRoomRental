package es.udc.tfg.server.model.domain;

import javax.persistence.*;
import com.vividsolutions.jts.geom.Point;
@Entity
@Table(name = "centrosEstudios")
public class CentrosEstudios {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cEstudios_generator")
  @SequenceGenerator(name = "cEstudios_generator", sequenceName = "cEstudios_seq")
  private Long id;

  private String nombre;

  private String provincia;

  private String municipio;

  private Point localizacion;

  public CentrosEstudios() {
  }

  public CentrosEstudios(String nombre, String provincia, String municipio, Point localizacion) {
    this.nombre = nombre;
    this.provincia = provincia;
    this.municipio = municipio;
    this.localizacion = localizacion;
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

  public String getProvincia() {
    return provincia;
  }

  public void setProvincia(String provincia) {
    this.provincia = provincia;
  }

  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
  }

  public Point getLocalizacion() {
    return localizacion;
  }

  public void setLocalizacion(Point localizacion) {
    this.localizacion = localizacion;
  }
}
