package es.udc.tfg.server.web;

import javax.validation.Valid;

import es.udc.tfg.server.model.exception.IncorrectOldPassword;
import es.udc.tfg.server.model.exception.NotFoundException;
import es.udc.tfg.server.model.exception.UserEmailExistsException;
import es.udc.tfg.server.model.exception.UserLoginExistsException;
import es.udc.tfg.server.model.service.dto.LoginDTO;
import es.udc.tfg.server.model.service.dto.UsuarioDTOPublico;
import es.udc.tfg.server.security.JWTToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

/**
 * Este controlador va por separado que el UserResource porque se encarga de
 * tareas relacionadas con la autenticación, registro, etc.
 *
 * <p>
 * También permite a cada usuario logueado en la aplicación obtener información
 * de su cuenta
 */
@RestController
@RequestMapping("/api")
public class AccountResource {
  private final Logger logger = LoggerFactory.getLogger(AccountResource.class);

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UsuarioService usuarioService;

  @PostMapping("/autenticarse")
  public JWTToken authenticate(@Valid @RequestBody LoginDTO loginDTO) throws CredentialsAreNotValidException {

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginDTO.getLogin(), loginDTO.getContrasena());
    try {
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = tokenProvider.createToken(authentication);
      return new JWTToken(jwt);
    } catch (AuthenticationException e) {
      logger.warn(e.getMessage(), e);
      throw new CredentialsAreNotValidException(e.getMessage());
    }
  }

  @GetMapping("/cuenta")
  public UsuarioDTOPrivado getAccount() {
    return usuarioService.getCurrentUserWithAuthority();
  }

  @GetMapping("/id")
  public UsuarioDTOPublico findById(@PathVariable Long id){
    return usuarioService.findById(id);
  }

  @PostMapping("/registro")
  public void registerAccount(@Valid @RequestBody UsuarioDTOPrivado account, Errors errors)
    throws UserLoginExistsException, RequestBodyNotValidException, UserEmailExistsException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }
    usuarioService.registerUser(account.getTelefono(), account.getNombre(), account.getLogin(), account.getContrasena(), account.getEmail());
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("/actualizarUsuario/{id}")
  public void actualizarUsuario(@PathVariable Long id,@RequestBody @Valid UsuarioDTOPrivado usuario) throws NotFoundException, IncorrectOldPassword {
    usuarioService.updateUser(id,usuario);
  }
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/actualizarContrasena/{id}")
  public void actualizarContrasenaUsuario(@PathVariable Long id,@RequestParam String oldPassword,
                                          @RequestParam String newPassword) throws NotFoundException, IncorrectOldPassword {
    usuarioService.updatePassword(id,oldPassword,newPassword);
  }
}
