package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Opciones;
import es.udc.tfg.server.model.domain.Piso;

import java.util.List;

public interface OpcionesDao {

  List<Opciones> findAll(String discriminante);

  Opciones findById(Long id);

  void create(Opciones opcion);
}
