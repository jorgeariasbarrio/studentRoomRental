package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.domain.Review;
import es.udc.tfg.server.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDaoJpa extends GenericDaoJpa implements ReviewDao {

  @Override
  public void create(Review review) {
    entityManager.persist(review);
  }

  @Override
  public void update(Review review) {
    entityManager.merge(review);
  }

  @Override
  public Review findById(Long id){
    return entityManager.find(Review.class,id);
  }

  @Override
  public Review findByLink(String link){
    return entityManager.createQuery("from Review where link_review=" + "'"+link+"'",Review.class).getSingleResult();
  }

  @Override
  public List<Review> findByUser(Long idUsuario){return entityManager.createQuery("from Review where usuario_id=" +idUsuario).getResultList();}

  @Override
  public List<Review> findByHabitacion(Long idHabitacion){return entityManager.createQuery("from Review where habitacion_id=" +idHabitacion).getResultList();}

  @Override
  public void deleteById(Long id) {
    Review review = findById(id);
    delete(review);
  }

  private void delete(Review review) {
    entityManager.remove(review);
  }
}
