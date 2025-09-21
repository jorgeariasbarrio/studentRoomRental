package es.udc.tfg.server.model.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.domain.Usuario;
import es.udc.tfg.server.model.domain.Enumerados.Autorizaciones;

import es.udc.tfg.server.model.exception.*;
import es.udc.tfg.server.model.repository.HabitacionDao;
import es.udc.tfg.server.model.repository.UsuarioDao;
import es.udc.tfg.server.model.service.dto.HabitacionDTO;
import es.udc.tfg.server.model.service.dto.UsuarioDTOPrivado;
import es.udc.tfg.server.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfg.server.model.service.dto.UsuarioDTOPublico;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UsuarioService {

  @Autowired
  private UsuarioDao userDAO;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private HabitacionDao habitacionDao;


  public List<UsuarioDTOPublico> findAll() throws OperationNotAllowed {
    Stream<UsuarioDTOPublico> users = userDAO.findAll().stream().map(user -> new UsuarioDTOPublico(user));
    if (!getCurrentUserWithAuthority().getAutorizacion().equals(Autorizaciones.ADMIN.toString())) {
      throw new OperationNotAllowed("Debes ser usuario admin");
    }
    return users.collect(Collectors.toList());
  }

  public UsuarioDTOPublico findById(Long id){
    return new UsuarioDTOPublico(userDAO.findById(id));
  }

  @Transactional(readOnly = false)
  public void registerUser(Integer telefono,String nombre,String login, String password,String email) throws UserLoginExistsException, UserEmailExistsException {
    registerUser(telefono,nombre,login, password,email, false);
  }

  @Transactional(readOnly = false)
  public void registerUser(Integer telefono,String nombre,String login, String password,String email, boolean isAdmin) throws UserLoginExistsException, UserEmailExistsException {
    if (userDAO.findByLogin(login) != null) {
      throw new UserLoginExistsException(login);
    }
    if (userDAO.findByEmail(email) != null){
      throw new UserEmailExistsException(email);
    }

    Usuario user = new Usuario();
    String encryptedPassword = passwordEncoder.encode(password);

    user.setTelefono(telefono);
    user.setNombre(nombre);
    user.setLogin(login);
    user.setEmail(email);
    user.setActive(true);
    user.setContrasena(encryptedPassword);
    user.setAutorizaciones(Autorizaciones.USER);
    if (isAdmin) {
      user.setAutorizaciones(Autorizaciones.ADMIN);
    }

    userDAO.create(user);
  }
  @Transactional(readOnly = false)
  public void updateUser(Long id,UsuarioDTOPrivado usuario) throws  IncorrectOldPassword, NotFoundException {

    Usuario user = userDAO.findById(id);
    if (user == null) {
      throw new NotFoundException(id.toString(),Usuario.class);
    }

    user.setTelefono(usuario.getTelefono());
    user.setNombre(usuario.getNombre());
    user.setLogin(usuario.getLogin());
    user.setEmail(usuario.getEmail());
    userDAO.update(user);
  }

  @Transactional(readOnly = false)
  public void updatePassword(Long id, String oldPassword, String newPassword) throws NotFoundException, IncorrectOldPassword {
    Usuario user = userDAO.findById(id);
    if (user == null) {
      throw new NotFoundException(id.toString(),Usuario.class);
    }
    if(!passwordEncoder.matches(oldPassword,user.getContrasena())){
      throw new IncorrectOldPassword();
    }
    String encryptedNewPassword = passwordEncoder.encode(newPassword);

    user.setContrasena(encryptedNewPassword);
    userDAO.update(user);
  }

  @Transactional()
  public UsuarioDTOPublico updateActive(Long id) throws NotFoundException, OperationNotAllowed {
    Usuario user = userDAO.findById(id);
    if (user == null) {
      throw new NotFoundException(id.toString(), Usuario.class);
    }

    UsuarioDTOPrivado currentUser = getCurrentUserWithAuthority();
    if (currentUser.getId().equals(user.getId())) {
      throw new OperationNotAllowed("The user cannot activate/deactive itself");
    }
    if (!currentUser.getAutorizacion().equals(Autorizaciones.ADMIN.toString())){
      throw new OperationNotAllowed("The user is not admin");
    }

    user.setActive(!user.isActive());
    userDAO.update(user);
    return new UsuarioDTOPublico(user);
  }

  public UsuarioDTOPrivado getCurrentUserWithAuthority() {
    String currentUserLogin = SecurityUtils.getCurrentUserLogin();
    if (currentUserLogin != null) {
      return new UsuarioDTOPrivado(userDAO.findByLogin(currentUserLogin));
    }
    return null;
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    Usuario user = userDAO.findById(id);
    if (user == null) {
      throw new NotFoundException(id.toString(), Usuario.class);
    }

    UsuarioDTOPrivado currentUser = getCurrentUserWithAuthority();
    if (currentUser.getId().equals(user.getId())) {
      throw new OperationNotAllowed("The user cannot delete itself");
    }

    userDAO.deleteById(id);
  }

  @Transactional(readOnly = false)
  public void anadirAFavoritos(Long idHabitacion) throws NotFoundException, OperationNotAllowed {
    Habitacion habitacion = habitacionDao.findById(idHabitacion);
    if (habitacion == null) {
      throw new NotFoundException(idHabitacion.toString(), Habitacion.class);
    }
    UsuarioDTOPrivado user = getCurrentUserWithAuthority();
    if (user == null) {
      throw new OperationNotAllowed("Un usuario no logueado no puede marcar favoritos");
    }
    Usuario usuario = userDAO.findById(user.getId());

    if (usuario.getHabitacionesFavoritas().contains(habitacion) && habitacion.getUsuarioFavorito().contains(usuario) ){
      habitacion.getUsuarioFavorito().remove(usuario);
      usuario.getHabitacionesFavoritas().remove(habitacion);
    }
    else {
      habitacion.getUsuarioFavorito().add(usuario);
      usuario.getHabitacionesFavoritas().add(habitacion);
    }
    userDAO.update(usuario);
    habitacionDao.update(habitacion);
  }

  @Transactional(readOnly = false)
  public List<HabitacionDTO> getFavoritos() throws OperationNotAllowed {

    UsuarioDTOPrivado user = getCurrentUserWithAuthority();
    if (user == null) {
      throw new OperationNotAllowed("Un usuario no logueado no tiene favoritos");
    }

    Stream<HabitacionDTO> favoritos = userDAO.getFavoritos(user.getId()).stream().map(favorito -> new HabitacionDTO(favorito));

    return favoritos.collect(Collectors.toList());

  }

}

