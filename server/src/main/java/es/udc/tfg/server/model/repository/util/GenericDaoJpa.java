package es.udc.tfg.server.model.repository.util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class GenericDaoJpa {
  @PersistenceContext
  protected EntityManager entityManager;
}
