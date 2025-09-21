package es.udc.tfg.server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfg.server.model.domain.Usuario;
import es.udc.tfg.server.model.repository.UsuarioDao;

@Component
public class MyUserDetailsService implements UserDetailsService {
  private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

  @Autowired
  private UsuarioDao usuarioDAO;


  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    Usuario usuario = usuarioDAO.findByLogin(login);
    if (usuario == null || !usuario.isActive()) {
      throw new UsernameNotFoundException("User " + login + " not found");
    }
    logger.info("Loaded user {}", login);
    return User.withUsername(usuario.getLogin()).password(usuario.getContrasena()).authorities("USER").build();
  }
}
