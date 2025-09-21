package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Piso;
import es.udc.tfg.server.model.domain.Usuario;

import java.util.List;

public interface PisoDao {
  List<Piso> findAll(Long idUsuario);

  Piso findById(Long id);


  void create(Piso piso);

  void update(Piso piso);

  void deleteById(Long id);
}
