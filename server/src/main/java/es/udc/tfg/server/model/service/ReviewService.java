package es.udc.tfg.server.model.service;

import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.domain.Review;
import es.udc.tfg.server.model.domain.Usuario;
import es.udc.tfg.server.model.exception.NotFoundException;
import es.udc.tfg.server.model.exception.OperationNotAllowed;
import es.udc.tfg.server.model.repository.HabitacionDao;
import es.udc.tfg.server.model.repository.ReviewDao;
import es.udc.tfg.server.model.repository.UsuarioDao;
import es.udc.tfg.server.model.service.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.NotActiveException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ReviewService {
  @Autowired
  private HabitacionDao habitacionDao;

  @Autowired
  private UsuarioDao usuarioDao;

  @Autowired
  private ReviewDao reviewDao;

  @Transactional(readOnly = false)
  public ReviewDTO crearReview(ReviewDTO review) throws NotFoundException {

    Review reviewBd = new Review();
    reviewBd.setNombre(review.getNombre());
    reviewBd.setComentario(review.getComentario());
    reviewBd.setPuntuacion(review.getPuntuacion());
    reviewBd.setFechaCreacion(LocalDate.now());
    reviewBd.setHabitacion(habitacionDao.findById(review.getHabitacion().getId()));
    if(review.getUsuario() != null){
    reviewBd.setUsuario(usuarioDao.findById(review.getUsuario().getId()));
    }
    reviewBd.setLinkReview(review.getLink());
    reviewBd.setCompleta(false);
    reviewDao.create(reviewBd);
    return new ReviewDTO(reviewBd);
  }
  @Transactional(readOnly = false)
  public ReviewDTO updateReview(ReviewDTO review) throws NotFoundException {

    Review reviewBd = reviewDao.findById(review.getId());
    if(reviewBd == null){
      throw new NotFoundException(review.getId().toString(), Review.class);
    }
    reviewBd.setNombre(review.getNombre());
    reviewBd.setComentario(review.getComentario());
    reviewBd.setPuntuacion(review.getPuntuacion());
    reviewBd.setCompleta(true);
    reviewBd.setFechaCreacion(LocalDate.now());
    reviewBd.setHabitacion(habitacionDao.findById(review.getHabitacion().getId()));
    reviewBd.setUsuario(usuarioDao.findById(review.getUsuario().getId()));
    reviewBd.setLinkReview(review.getLink());
    reviewDao.update(reviewBd);
    return new ReviewDTO(reviewBd);
  }

  @Transactional(readOnly = false)
  public void asignarUsuarioAReview(Long idReview, String email) throws NotFoundException {

    Review reviewBd = reviewDao.findById(idReview);
    if(reviewBd == null){
      throw new NotFoundException(idReview.toString(), Review.class);
    }

    // Este codigo transforma el email enviado desde el frontend en un email valido
    // Recibe (admin%40admin.es=)
    // Devuelve (admin@admin.es)
    if (email.contains("%40")){
      email = email.replace("%40","@");
    }
    if(email.contains("=")){
      email= email.replace("=","");
    }
    Usuario usuario = usuarioDao.findByEmail(email);
    if(usuario == null){
      throw new NotFoundException(email, Usuario.class);
    }
    reviewBd.setUsuario(usuario);
    reviewDao.update(reviewBd);
  }
  @Transactional(readOnly = true)
  public List<ReviewDTO> findByUser(Long idUsuario) throws NotFoundException {
    if (usuarioDao.findById(idUsuario) == null){
      throw new NotFoundException(idUsuario.toString(),Usuario.class);
    }
    return reviewDao.findByUser(idUsuario).stream().map(ReviewDTO::new).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<ReviewDTO> findByHabitacion(Long idHabitacion) throws NotFoundException {
    if (habitacionDao.findById(idHabitacion) == null){
      throw new NotFoundException(idHabitacion.toString(),Usuario.class);
    }
    return reviewDao.findByHabitacion(idHabitacion).stream().map(ReviewDTO::new).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public ReviewDTO findByLink(String link){
    return new ReviewDTO(reviewDao.findByLink(link));
  }

  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    Review review = reviewDao.findById(id);
    if (review == null) {
      throw new NotFoundException(id.toString(), Review.class);
    }

    reviewDao.deleteById(id);
  }
}
