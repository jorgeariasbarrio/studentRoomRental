package es.udc.tfg.server.model.service.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class  LoginDTO {
  @NotEmpty
  private String login;

  @NotEmpty
  @Size(min = 4)
  private String contrasena;

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getContrasena() {
    return contrasena;
  }

  public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
  }
}
