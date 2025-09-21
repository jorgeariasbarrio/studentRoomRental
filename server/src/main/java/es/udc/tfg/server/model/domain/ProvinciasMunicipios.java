package es.udc.tfg.server.model.domain;

import javax.persistence.*;

@Entity
@Table(name = "provincias")
public class ProvinciasMunicipios {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provincias_generator")
  @SequenceGenerator(name = "provincias_generator", sequenceName = "provincias_seq")
  private Long id;

  private String provincia;

  private String municipio;

  public ProvinciasMunicipios() {
  }

  public ProvinciasMunicipios(String provincia, String municipio) {
    this.provincia = provincia;
    this.municipio = municipio;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
}
