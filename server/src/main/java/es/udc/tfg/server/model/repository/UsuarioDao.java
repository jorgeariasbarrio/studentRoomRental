package es.udc.tfg.server.model.repository;

import java.util.List;
import java.util.Set;

import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.domain.Usuario;

public interface UsuarioDao {
  List<Usuario> findAll();

  List<Habitacion> getFavoritos(Long idUsuario);

  Usuario findById(Long id);

  Usuario findByLogin(String login);

  Usuario findByEmail(String email);

  void create(Usuario usuario);

  void update(Usuario usuario);

  void deleteById(Long id);

}
