package es.udc.tfg.server.model.service.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import es.udc.tfg.server.model.domain.Usuario;

public class UsuarioDTOPrivado {

  private Long id;

  @NotEmpty
  private String nombre;

  @NotNull
  private Integer telefono;

  @NotEmpty
  @Size(min = 4)
  private String login;

  @Size(min = 4)
  private String contrasena;

  private String email;


  private String autorizacion;


  public UsuarioDTOPrivado() {
  }

  public UsuarioDTOPrivado(Usuario usuario) {
    this.id = usuario.getId();
    this.nombre = usuario.getNombre();
    this.telefono = usuario.getTelefono();
    this.login = usuario.getLogin();
    this.email = usuario.getEmail();
    // la contraseña no se rellena, nunca se envía al cliente
    this.autorizacion = usuario.getAutorizaciones().name();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Integer getTelefono() {
    return telefono;
  }

  public void setTelefono(Integer telefono) {
    this.telefono = telefono;
  }

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

  public String getEmail(){return email;}

  public void setEmail(String email){
    this.email = email;
  }


  public String getAutorizacion() {
    return autorizacion;
  }

  public void setAutorizacion(String autorizacion) {
    this.autorizacion = autorizacion;
  }
}
