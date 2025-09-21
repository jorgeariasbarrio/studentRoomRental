package es.udc.tfg.server.web;


import es.udc.tfg.server.model.domain.Enumerados.Provincias;
import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.domain.Imagen;
import es.udc.tfg.server.model.domain.Opciones;
import es.udc.tfg.server.model.domain.Piso;
import es.udc.tfg.server.model.exception.ModelException;
import es.udc.tfg.server.model.exception.NotFoundException;
import es.udc.tfg.server.model.exception.OperationNotAllowed;
import es.udc.tfg.server.model.service.HabitacionService;
import es.udc.tfg.server.model.service.dto.*;
import es.udc.tfg.server.model.service.util.ImageService;
import es.udc.tfg.server.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.tfg.server.web.exceptions.RequestBodyNotValidException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionResource {
  @Autowired
  private HabitacionService habitacionService;

  @GetMapping
  public List<HabitacionDTO> findAll(@RequestParam(required = false) Long idPiso){
    return habitacionService.findAll(idPiso);
  }

  @GetMapping("/filter")
  public List<HabitacionDTO> findFilter(@RequestParam(required = false) String provincia, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicioContrato, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finContrato, @RequestParam(required = false) Double distanciaMaxima, @RequestParam(required = false)
  Long idCentroEstudios, @RequestParam(required = false) Integer precioMaximo, @RequestParam(required = false)Integer numHabitaciones, @RequestParam(required = false) String opciones, @RequestParam(required = false) String ordenacion,@RequestParam(required = false) Long idUsuario,@RequestParam(required = false) String municipio){
    return habitacionService.findFilter(provincia,inicioContrato,finContrato,distanciaMaxima,idCentroEstudios,precioMaximo,numHabitaciones,opciones,ordenacion,idUsuario,municipio);
  }

  @GetMapping("/{id}")
  public HabitacionDTO findById(@PathVariable Long id) throws RequestBodyNotValidException, NotFoundException {
    return habitacionService.findById(id);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public HabitacionDTO create(@RequestBody @Valid HabitacionDTO habitacion, Errors errors) throws RequestBodyNotValidException, OperationNotAllowed {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }
    habitacion.setFechaCreacion(LocalDate.now());
    return habitacionService.create(habitacion);
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("/{id}")
  public HabitacionDTO update(@PathVariable Long id, @RequestBody HabitacionDTO habitacion) throws RequestBodyNotValidException, NotFoundException, IdAndBodyNotMatchingOnUpdateException, OperationNotAllowed {
    if (!Objects.equals(id, habitacion.getId())) {
      throw new IdAndBodyNotMatchingOnUpdateException(Piso.class);
    }
    return habitacionService.update(habitacion);
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("cambiarEstado/{idHabitacion}")
  public void ocultarHabitacion(@PathVariable Long idHabitacion) throws NotFoundException {
     habitacionService.ocultarHabitacion(idHabitacion);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("crearAlquiler/{idHabitacion}")
  public AlquilerDTO crearAlquiler(@PathVariable Long idHabitacion, @RequestParam String emailUsuario) throws NotFoundException {
    return habitacionService.crearAlquiler(idHabitacion,emailUsuario);
  }
  @PreAuthorize("isAuthenticated()")
  @PutMapping("cerrarAlquiler/{idHabitacion}")
  public void cerrarAlquiler(@PathVariable Long idHabitacion) throws NotFoundException{
    habitacionService.cerrarAlquiler(idHabitacion);
  }
  @PreAuthorize("isAuthenticated()")
  @GetMapping("alquileres/{idHabitacion}")
  public AlquilerDTO getAlquilerAbierto(@PathVariable Long idHabitacion){
    return habitacionService.getAlquilerAbierto(idHabitacion);
  }


  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws  NotFoundException, OperationNotAllowed {
    habitacionService.deleteById(id);
  }
  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/alquiler/{idAlquiler}/{idHabitacion}")
  public void deleteAlquiler(@PathVariable Long idAlquiler,@PathVariable Long idHabitacion) throws  NotFoundException, OperationNotAllowed {
    habitacionService.deleteAlquilerById(idAlquiler,idHabitacion);
  }
  @PostMapping("/{id}/imagenes")
  @ResponseStatus(HttpStatus.OK)
  public void guardarImagenes(@PathVariable Long id, @RequestParam MultipartFile imagen,@RequestParam boolean portada) throws NotFoundException, OperationNotAllowed {
    try {
      habitacionService.findById(id);
    } catch (NotFoundException e) {
      throw new NotFoundException(id.toString(), Habitacion.class);
    }
    habitacionService.guardarImagenes(id, imagen,portada);
  }

  @GetMapping("/{id}/imagenes/{idImagen}")
  @ResponseStatus(HttpStatus.OK)
  public void cargarImagen(@PathVariable Long id, @PathVariable Long idImagen, HttpServletResponse response) throws ModelException {
    HabitacionDTO a;
    String path = "";
    try {
      a = habitacionService.findById(id);
    } catch (NotFoundException e) {
      throw new NotFoundException(id.toString(), Habitacion.class);
    }
    for (ImagenDTO i:a.getImagenes()) {
      if (idImagen == i.getIdImagen()) {
        path = i.getPath();
        break;
      }
    }
    ImagenDTO imagenDTO = habitacionService.cargarImagen(path);
    try {
      response.setContentType(imagenDTO.getMediaType());
      response.setHeader("Content-disposition", "filename=" + imagenDTO.getPath());
      IOUtils.copy(imagenDTO.getInputStream(), response.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @GetMapping("/{id}/imagenes")
  public Set<ImagenDTO> listaImagenes(@PathVariable Long id) throws NotFoundException {
    HabitacionDTO a;
    try {
      a = habitacionService.findById(id);
    } catch (NotFoundException e) {
      throw new NotFoundException(id.toString(), Habitacion.class);
    }
    Set<ImagenDTO> i = new HashSet<>();
    a.getImagenes().forEach(
      im -> i.add(new ImagenDTO(im.getIdImagen(), im.getNombre(), im.getPath(), im.isPortada()))
    );
    return i;
  }

  @PutMapping("/{id}/imagenes/{idImagen}")
  public ImagenDTO actualizarPortada(@PathVariable Long id, @PathVariable Long idImagen, @RequestBody @Valid ActualizarImagenDTO imagen, Errors errors) throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException, NotFoundException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }
    if (idImagen != imagen.getIdImagen()) {
      throw new IdAndBodyNotMatchingOnUpdateException(Imagen.class);
    }
    try {
      habitacionService.findById(id);
    } catch (NotFoundException e) {
      throw new NotFoundException(id.toString(), Habitacion.class);
    }
    return habitacionService.actualizarImagen(id, imagen.getIdImagen(), imagen.isPortada());
  }

  @DeleteMapping("/{id}/imagenes/{idImagen}")
  public void borrarImagen(@PathVariable Long id, @PathVariable Long idImagen) throws ModelException {
    try {
      habitacionService.findById(id);
    } catch (NotFoundException e) {
      throw new NotFoundException(id.toString(), Habitacion.class);
    }
    habitacionService.borrarImagen(id, idImagen);
  }

}
