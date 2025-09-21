package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Piso;
import es.udc.tfg.server.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PisoDaoJpa  extends GenericDaoJpa implements PisoDao {

  @Override
  public List<Piso> findAll(Long idUsuario) {

    if (idUsuario != null){
      return entityManager.createQuery("from Piso where usuario_id =" + idUsuario, Piso.class).getResultList();
    }
    return entityManager.createQuery("from Piso", Piso.class).getResultList();
  }

  @Override
  public Piso findById(Long id) {
    return entityManager.find(Piso.class, id);
  }

  @Override
  public void create(Piso piso) {
    entityManager.persist(piso);
  }

  @Override
  public void update(Piso piso) {
    entityManager.merge(piso);
  }

  @Override
  public void deleteById(Long id) {
    Piso piso = findById(id);
    delete(piso);
  }

  private void delete(Piso piso) {
    entityManager.remove(piso);
  }
}
