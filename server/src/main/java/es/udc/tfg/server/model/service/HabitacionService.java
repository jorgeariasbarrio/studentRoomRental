package es.udc.tfg.server.model.service;


import es.udc.tfg.server.model.domain.*;
import es.udc.tfg.server.model.domain.Enumerados.Autorizaciones;
import es.udc.tfg.server.model.domain.Enumerados.EstadoHabitacion;
import es.udc.tfg.server.model.domain.Enumerados.Provincias;
import es.udc.tfg.server.model.exception.ModelException;
import es.udc.tfg.server.model.exception.NotFoundException;
import es.udc.tfg.server.model.exception.OperationNotAllowed;
import es.udc.tfg.server.model.repository.*;
import es.udc.tfg.server.model.service.dto.*;
import es.udc.tfg.server.model.service.util.ImageService;
import es.udc.tfg.server.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class HabitacionService {

  @Autowired
  private HabitacionDao habitacionDao;

  @Autowired
  private PisoDao pisoDao;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private OpcionesDao opcionesDao;

  @Autowired
  private ImageService imageService;

  @Autowired
  private ImagenDao imagenDAO;

  @Autowired
  private UsuarioDao usuarioDao;

  @Autowired
  private AlquilerDao alquilerDao;


  public UsuarioDTOPrivado getCurrentUserWithAuthority() {
    String currentUserLogin = SecurityUtils.getCurrentUserLogin();
    if (currentUserLogin != null) {
      return new UsuarioDTOPrivado(usuarioDao.findByLogin(currentUserLogin));
    }
    return null;
  }
  @Transactional(readOnly = true)
  public List<HabitacionDTO> findAll(Long idPiso) {
    UsuarioDTOPrivado usuario = getCurrentUserWithAuthority();
    if (usuario != null) {
      Stream<HabitacionDTO> habitaciones = habitacionDao.findAll(idPiso, Objects.equals(usuario.getAutorizacion(), Autorizaciones.ADMIN.toString())).stream().map(habitacion -> new HabitacionDTO(habitacion));
      return habitaciones.collect(Collectors.toList());
    }
    else{
      Stream<HabitacionDTO> habitaciones = habitacionDao.findAll(idPiso, false).stream().map(habitacion -> new HabitacionDTO(habitacion));
      return habitaciones.collect(Collectors.toList());
    }
  }

  @Transactional(readOnly = true)
  public List<HabitacionDTO> findFilter(String provincia, LocalDate inicioContrato, LocalDate finContrato, Double distanciaMaxima, Long idCentroEstudios, Integer precioMaximo, Integer numHabitaciones, String opciones, String ordenacion, Long idUsuario,String municipio) {
    Stream<HabitacionDTO> habitaciones = habitacionDao.findFilter(provincia, inicioContrato,finContrato,distanciaMaxima, idCentroEstudios, precioMaximo, numHabitaciones, opciones, ordenacion,idUsuario,municipio).stream().map(habitacion -> new HabitacionDTO(habitacion));
    return habitaciones.collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public HabitacionDTO findById(Long id) throws NotFoundException {
    Habitacion habitacion = habitacionDao.findById(id);
    if (habitacion == null) {
      throw new NotFoundException(id.toString(), Habitacion.class);
    }
    return new HabitacionDTO(habitacion);
  }


  @Transactional(readOnly = false)
  public HabitacionDTO create(HabitacionDTO habitacion) throws OperationNotAllowed {

    Habitacion habitacionBd = new Habitacion();
    habitacionBd.setDescripcion(habitacion.getDescripcion());
    habitacionBd.setTitulo(habitacion.getTitulo());
    habitacionBd.setFechaCreacion(LocalDate.now());
    habitacionBd.setFechaModificacion(null);
    habitacionBd.setGastosCompartidos(habitacion.getGastosCompartidos());
    habitacionBd.setmCuadrados(habitacion.getmCuadrados());
    habitacionBd.setFianza(habitacion.getFianza());
    habitacionBd.setEstadoHabitacion(EstadoHabitacion.DISPONIBLE);
    habitacionBd.setPrecio(habitacion.getPrecio());
    habitacionBd.setInicioContrato(habitacion.getInicioContrato());
    habitacionBd.setFinContrato(habitacion.getFinContrato());
    habitacionBd.setPiso(pisoDao.findById(habitacion.getPiso().getId()));

    usuarioService.getCurrentUserWithAuthority();
    if (habitacion.getPiso().getUsuario().getId() != usuarioService.getCurrentUserWithAuthority().getId()) {
      throw new OperationNotAllowed("No puedes crear habitaciones para un piso no creado por ti");
    }

    if (habitacion.getOpciones() != null) {
      habitacion.getOpciones().forEach(opcion -> {
        habitacionBd.getOpciones().add(opcionesDao.findById(opcion.getId()));
      });
    }


    habitacionDao.create(habitacionBd);

    return new HabitacionDTO(habitacionBd);
  }

  @Transactional(readOnly = false)
  public HabitacionDTO update(HabitacionDTO habitacion) throws NotFoundException, OperationNotAllowed {

    Habitacion habitacionBd = habitacionDao.findById(habitacion.getId());
    if (habitacionBd == null) {
      throw new NotFoundException(habitacion.getId().toString(), Habitacion.class);
    }

    habitacionBd.setDescripcion(habitacion.getDescripcion());
    habitacionBd.setTitulo(habitacion.getTitulo());
    habitacionBd.setFechaCreacion(habitacion.getFechaCreacion());
    habitacionBd.setFechaModificacion(LocalDate.now());
    habitacionBd.setmCuadrados(habitacion.getmCuadrados());
    habitacionBd.setFianza(habitacion.getFianza());
    habitacionBd.setGastosCompartidos(habitacion.getGastosCompartidos());
    habitacionBd.setEstadoHabitacion(EstadoHabitacion.valueOf(habitacion.getEstadoHabitacion()));
    habitacionBd.setPrecio(habitacion.getPrecio());
    habitacionBd.setInicioContrato(habitacion.getInicioContrato());
    habitacionBd.setFinContrato(habitacion.getFinContrato());
    habitacionBd.setPiso(pisoDao.findById(habitacion.getPiso().getId()));

    if (habitacion.getPiso().getUsuario().getId() != usuarioService.getCurrentUserWithAuthority().getId()) {
      throw new OperationNotAllowed("No puedes actualizar habitaciones para un piso no creado por ti");
    }
    habitacionBd.getOpciones().clear();
      habitacion.getOpciones().forEach(opcion -> habitacionBd.getOpciones().add(opcionesDao.findById(opcion.getId())));

    habitacionDao.update(habitacionBd);

    return new HabitacionDTO(habitacionBd);
  }

  @Transactional(readOnly = false)
  public void ocultarHabitacion(Long idHabitacion) throws NotFoundException {

    Habitacion habitacionBd = habitacionDao.findById(idHabitacion);
    if (habitacionBd == null) {
      throw new NotFoundException(idHabitacion.toString(), Habitacion.class);
    }
    if (habitacionBd.getEstadoHabitacion() == EstadoHabitacion.DISPONIBLE){
      habitacionBd.setEstadoHabitacion(EstadoHabitacion.OCULTA);
    }
    else if (habitacionBd.getEstadoHabitacion() == EstadoHabitacion.OCULTA){
      habitacionBd.setEstadoHabitacion(EstadoHabitacion.DISPONIBLE);
    }
    habitacionDao.update(habitacionBd);
  }

  @Transactional(readOnly = false)
  public AlquilerDTO crearAlquiler(Long idHabitacion, String emailUsuario) throws NotFoundException {

    Habitacion habitacionAlquilada = habitacionDao.findById(idHabitacion);
    if (habitacionAlquilada == null) {
      throw new NotFoundException(idHabitacion.toString(), Habitacion.class);
    }
    Alquiler alquilerAbierto = alquilerDao.findAlquilerAbierto(idHabitacion);
    if (alquilerAbierto != null){
      return null;
    }
    LocalDate inicioAlquiler = LocalDate.now();

    Alquiler alquiler = new Alquiler();
    alquiler.setFechaInicio(inicioAlquiler);
    alquiler.setHabitacionAlquiler(habitacionAlquilada);
    if (!emailUsuario.equals("null")){
      Usuario usuario = usuarioDao.findByEmail(emailUsuario);
      if (usuario == null){
        throw new NotFoundException(emailUsuario, Usuario.class);
      }
      alquiler.setUsuarioAlquiler(usuario);
    }
    habitacionAlquilada.setEstadoHabitacion(EstadoHabitacion.ALQUILADA);
    habitacionDao.update(habitacionAlquilada);
    alquilerDao.create(alquiler);
    return new AlquilerDTO(alquiler);
  }

  @Transactional(readOnly = false)
  public void cerrarAlquiler(Long idHabitacion) throws NotFoundException {

    Habitacion habitacionAlquilada = habitacionDao.findById(idHabitacion);
    if (habitacionAlquilada == null) {
      throw new NotFoundException(idHabitacion.toString(), Habitacion.class);
    }

    Alquiler alquiler = alquilerDao.findAlquilerAbierto(idHabitacion);

    if (alquiler == null){
      throw new NotFoundException(idHabitacion.toString(), Alquiler.class);
    }
    alquiler.setFechaFin(LocalDate.now());
    alquilerDao.update(alquiler);
    habitacionAlquilada.setEstadoHabitacion(EstadoHabitacion.DISPONIBLE);
    habitacionDao.update(habitacionAlquilada);
  }
  @Transactional(readOnly = false)
  public AlquilerDTO getAlquilerAbierto(Long idHabitacion) {

    Alquiler alquilerAbierto = alquilerDao.findAlquilerAbierto(idHabitacion);
    if (alquilerAbierto == null){
      return null;
    }
    return new AlquilerDTO(alquilerAbierto);
  }
  @Transactional(readOnly = false)
  public void deleteAlquilerById(Long id,Long idHabitacion) throws NotFoundException, OperationNotAllowed {
    Habitacion habitacionAlquilada = habitacionDao.findById(idHabitacion);
    Alquiler alquiler = alquilerDao.findById(id);
    if (alquiler == null) {
      throw new NotFoundException(id.toString(), Usuario.class);
    }
    alquilerDao.deleteById(id);
    habitacionAlquilada.setEstadoHabitacion(EstadoHabitacion.DISPONIBLE);
    habitacionDao.update(habitacionAlquilada);
  }



  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    Habitacion habitacion = habitacionDao.findById(id);
    if (habitacion == null) {
      throw new NotFoundException(id.toString(), Usuario.class);
    }

    habitacionDao.deleteById(id);
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false)
  public void guardarImagenes(Long id, MultipartFile imagen, boolean esPortada) throws OperationNotAllowed {
    Habitacion h = habitacionDao.findById(id);
    Piso p = h.getPiso();
    UsuarioDTOPrivado currentUser = usuarioService.getCurrentUserWithAuthority();
    if (!currentUser.getId().equals(p.getUsuario().getId())) {
      throw new OperationNotAllowed("Current user does not match piso creator");
    }
    if (h.getImagenes().size() + 1 > 20)
      throw new OperationNotAllowed("Image number exceeded. Max. number is 20");
    Imagen i = new Imagen(null, imagen.getOriginalFilename());
    i.setHabitacion(h);
    i.setPortada(esPortada);
    imagenDAO.create(i);
    String path;
    try {
      path = imageService.saveImage(imagen, i.getId());
    } catch (ModelException e) {
      e.printStackTrace();
      return;
    }
    i.setPath(path);
    i.setNombre("imagen"+path);
    imagenDAO.update(i);
    h.getImagenes().add(i);
    habitacionDao.update(h);
  }


  public ImagenDTO cargarImagen(String path) throws ModelException {
    return imageService.getImage(path);
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false)
  public ImagenDTO actualizarImagen(Long idHabitacion, Long idImagen, boolean portada) {
    Habitacion h = habitacionDao.findById(idHabitacion);
    h.getImagenes().forEach(i -> {
      i.setPortada(false); // Ponemos la portada de todas a falso para que no haya 2 portadas
      imagenDAO.update(i); // Actualizamos la tabla de imagenes, ya que lo hicimos solo en anuncio.imagenes
    });
    Imagen i = imagenDAO.find(idImagen);
    i.setPortada(portada);
    imagenDAO.update(i);
    return new ImagenDTO(i.getId(), i.getNombre(), i.getPath(), i.isPortada());
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false)
  public void borrarImagen(Long idHabitacion, Long idImagen) throws ModelException {
    Imagen i, borraIm;
    Habitacion h = habitacionDao.findById(idHabitacion);
    if (h == null) throw new NotFoundException(idHabitacion.toString(), Habitacion.class);
    if ((i = imagenDAO.find(idImagen)) == null) throw new NotFoundException(idImagen.toString(), Imagen.class);
    UsuarioDTOPrivado currentUser = usuarioService.getCurrentUserWithAuthority();
    if (!currentUser.getId().equals(h.getPiso().getUsuario().getId())) {
      throw new OperationNotAllowed("Current user does not match habiacion creator");
    }
    imageService.deleteImage(i.getNombre(), idImagen);
    h.getImagenes().remove(i);
    imagenDAO.deleteById(i.getId());
  }

}
