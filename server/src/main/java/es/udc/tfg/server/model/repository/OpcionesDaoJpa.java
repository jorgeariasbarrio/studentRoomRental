package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Opciones;
import es.udc.tfg.server.model.domain.Piso;
import es.udc.tfg.server.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OpcionesDaoJpa extends GenericDaoJpa implements OpcionesDao {

  @Override
  public List<Opciones> findAll(String discriminante) {
    return entityManager.createQuery("from Opciones where discriminante ="+"'"+discriminante+"'", Opciones.class).getResultList();
  }

  @Override
  public Opciones findById(Long id) {
    return entityManager.find(Opciones.class, id);
  }
  @Override
  public void create(Opciones opciones) {
    entityManager.persist(opciones);
  }



}
