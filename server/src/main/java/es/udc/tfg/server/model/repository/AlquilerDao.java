package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Alquiler;

public interface AlquilerDao {

  void create(Alquiler alquiler);

  Alquiler findById(Long id);

  void update(Alquiler alquiler);

  void deleteById(Long id);

  Alquiler findAlquilerAbierto(Long id);
}
