package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Imagen;
import es.udc.tfg.server.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

@Repository
public class ImagenDaoJpa extends GenericDaoJpa implements ImagenDao {
  @Override
  public void create(Imagen imagen) {
    entityManager.persist(imagen);
  }

  @Override
  public Imagen find(Long id) {
    return entityManager.find(Imagen.class, id);
  }

  @Override
  public void update(Imagen imagen) {
    entityManager.merge(imagen);
  }

  @Override
  public void deleteById(Long id) {
    Imagen imagen = find(id);
    delete(imagen);
  }

  private void delete(Imagen imagen) {
    entityManager.remove(imagen);
  }
}

