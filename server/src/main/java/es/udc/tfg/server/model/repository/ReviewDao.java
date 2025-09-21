package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Review;

import java.util.List;

public interface ReviewDao {

  void create(Review review);

  void update(Review review);

  Review findById(Long id);

  Review findByLink(String link);

  List<Review> findByUser(Long idUsuario);

  List<Review> findByHabitacion(Long idHabitacion);

  void deleteById(Long id);

}
