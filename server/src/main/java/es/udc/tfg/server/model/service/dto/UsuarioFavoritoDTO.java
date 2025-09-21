package es.udc.tfg.server.model.service.dto;

import es.udc.tfg.server.model.domain.Usuario;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class UsuarioFavoritoDTO {
    private Long id;

    @NotEmpty
    private String nombre;

    @NotNull
    @Size(min = 9, max = 9)
    private Integer telefono;

    @NotEmpty
    @Size(min = 4)
    private String login;

    @Size(min = 4)
    private String email;

    private boolean estado;

    private Set<HabitacionDTO> habitacionesFavoritas;


    public UsuarioFavoritoDTO() {
    }

    public UsuarioFavoritoDTO(Usuario usuario) {
      this.id = usuario.getId();
      this.login = usuario.getLogin();
      this.nombre = usuario.getNombre();
      this.telefono = usuario.getTelefono();
      // la contraseña no se rellena, nunca se envía al cliente
      this.email = usuario.getEmail();
      this.estado = usuario.isActive();
      usuario.getHabitacionesFavoritas().forEach(hf -> {
        this.habitacionesFavoritas.add(new HabitacionDTO(hf));
      });
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

    public String getLogin() {
      return login;
    }

    public void setLogin(String login) {
      this.login = login;
    }

    public Integer getTelefono() {
      return telefono;
    }

    public void setTelefono(Integer telefono) {
      this.telefono = telefono;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public boolean getEstado() {
      return estado;
    }

    public void setEstado(boolean estado) {
      this.estado = estado;
    }

    public Set<HabitacionDTO> getHabitacionesFavoritas() {
      return habitacionesFavoritas;
    }

    public void setHabitacionesFavoritas(Set<HabitacionDTO> habitacionesFavoritas) {
      this.habitacionesFavoritas = habitacionesFavoritas;
    }
  }
