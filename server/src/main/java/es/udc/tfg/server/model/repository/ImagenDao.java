package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Imagen;

public interface ImagenDao {

  void create(Imagen imagen);

  void update(Imagen imagen);

  void deleteById(Long id);

  Imagen find(Long id);
}
