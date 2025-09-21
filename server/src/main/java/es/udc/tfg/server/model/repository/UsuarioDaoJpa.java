package es.udc.tfg.server.model.repository;

import java.util.List;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import es.udc.tfg.server.model.domain.Habitacion;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import es.udc.tfg.server.model.domain.Usuario;
import es.udc.tfg.server.model.repository.util.GenericDaoJpa;

@Repository
public class UsuarioDaoJpa extends GenericDaoJpa implements UsuarioDao {

  @Override
  public List<Usuario> findAll() {
    return entityManager.createQuery("from Usuario", Usuario.class).getResultList();
  }

  @Override
  public Usuario findById(Long id) {
    return entityManager.find(Usuario.class, id);
  }

  @Override
  public Usuario findByLogin(String login) {
    TypedQuery<Usuario> query = entityManager.createQuery("from Usuario u where u.login = :login", Usuario.class)
        .setParameter("login", login);
    return DataAccessUtils.singleResult(query.getResultList());
  }

  @Override
  public Usuario findByEmail(String email) {
    TypedQuery<Usuario> query = entityManager.createQuery("from Usuario u where u.email = :email", Usuario.class)
      .setParameter("email", email);
    return DataAccessUtils.singleResult(query.getResultList());
  }

  @Override
  public List<Habitacion> getFavoritos(Long idUsuario){
     return entityManager.createNativeQuery("select h.* from habitacion h join habitacion_usuario_favorito hf " +
      "ON hf.habitaciones_favoritas_id = h.id where hf.usuario_favorito_id=" + idUsuario,Habitacion.class).getResultList();
  }

  @Override
  public void create(Usuario usuario) {
    entityManager.persist(usuario);
  }

  @Override
  public void update(Usuario usuario) {
    entityManager.merge(usuario);
  }

  @Override
  public void deleteById(Long id) {
    Usuario usuario = findById(id);
    delete(usuario);
  }

  private void delete(Usuario usuario) {
    entityManager.remove(usuario);
  }

}
