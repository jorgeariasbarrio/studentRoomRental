package es.udc.tfg.server.web;

import es.udc.tfg.server.model.exception.NotFoundException;
import es.udc.tfg.server.model.exception.OperationNotAllowed;
import es.udc.tfg.server.model.service.ReviewService;
import es.udc.tfg.server.model.service.dto.ReviewDTO;
import es.udc.tfg.server.web.exceptions.RequestBodyNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/reviews")
public class ReviewResource {

  @Autowired
  private ReviewService reviewService;


  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public ReviewDTO create(@RequestBody @Valid ReviewDTO review, Errors errors) throws NotFoundException, RequestBodyNotValidException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }
    return reviewService.crearReview(review);
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping
  public ReviewDTO update(@RequestBody @Valid ReviewDTO review, Errors errors) throws NotFoundException, RequestBodyNotValidException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }
    return reviewService.updateReview(review);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{idReview}")
  public void asignarReviewAUsuario(@PathVariable Long idReview, @RequestBody @Valid String email, Errors errors) throws RequestBodyNotValidException, NotFoundException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }
    reviewService.asignarUsuarioAReview(idReview,email);
  }

  @GetMapping("/{idUsuario}")
  public List<ReviewDTO> findByUser(@PathVariable Long idUsuario) throws NotFoundException {
    return reviewService.findByUser(idUsuario);
  }

  @GetMapping("/habitacion/{idHabitacion}")
  public List<ReviewDTO> findByHabitacion(@PathVariable Long idHabitacion) throws NotFoundException {
    return reviewService.findByHabitacion(idHabitacion);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/link/{link}")
  public ReviewDTO findByLink(@PathVariable String link){
    return reviewService.findByLink(link);
  }

  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{id}")
  public void deleteReview(@PathVariable Long id) throws  NotFoundException, OperationNotAllowed {
    reviewService.deleteById(id);
  }

}
