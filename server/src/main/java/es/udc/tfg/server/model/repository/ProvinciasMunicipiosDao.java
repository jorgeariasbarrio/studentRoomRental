package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Enumerados.Provincias;
import es.udc.tfg.server.model.domain.Opciones;
import es.udc.tfg.server.model.domain.ProvinciasMunicipios;

import java.util.List;

public interface ProvinciasMunicipiosDao {

  List<ProvinciasMunicipios> findAllByProvincia(String provincia);

  List<ProvinciasMunicipios> findAll();

  void create(ProvinciasMunicipios provincias);
}
