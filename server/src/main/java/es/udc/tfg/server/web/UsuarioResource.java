package es.udc.tfg.server.web;

import javax.validation.Valid;

import es.udc.tfg.server.model.exception.NotFoundException;
import es.udc.tfg.server.model.exception.OperationNotAllowed;
import es.udc.tfg.server.model.exception.UserEmailExistsException;
import es.udc.tfg.server.model.exception.UserLoginExistsException;
import es.udc.tfg.server.model.service.dto.HabitacionDTO;
import es.udc.tfg.server.model.service.dto.LoginDTO;
import es.udc.tfg.server.model.service.dto.UsuarioDTOPublico;
import es.udc.tfg.server.security.JWTToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import es.udc.tfg.server.model.service.UsuarioService;
import es.udc.tfg.server.model.service.dto.UsuarioDTOPrivado;
import es.udc.tfg.server.security.TokenProvider;
import es.udc.tfg.server.web.exceptions.CredentialsAreNotValidException;
import es.udc.tfg.server.web.exceptions.RequestBodyNotValidException;

import java.util.List;
import java.util.Set;

/**
 * Este controlador va por separado que el UserResource porque se encarga de
 * tareas relacionadas con la autenticación, registro, etc.
 *
 * <p>
 * También permite a cada usuario logueado en la aplicación obtener información
 * de su cuenta
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

  @Autowired
  private UsuarioService usuarioService;


  @GetMapping
  public List<UsuarioDTOPublico> findAll() throws OperationNotAllowed {return usuarioService.findAll();}
  @GetMapping("/{id}")
  public UsuarioDTOPublico findById(@PathVariable Long id){
    return usuarioService.findById(id);
  }

  @GetMapping("/favoritos")
  public List<HabitacionDTO> getFavoritos() throws OperationNotAllowed {return usuarioService.getFavoritos();}

  @PutMapping("/modificarFavorito")
  public void modificarFavorito(@RequestParam Long idHabitacion) throws NotFoundException, OperationNotAllowed { usuarioService.anadirAFavoritos(idHabitacion);}

  @PutMapping("/cambiarEstado/{id}")
  public UsuarioDTOPublico modificarEstado(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    return usuarioService.updateActive(id);
  }
}
