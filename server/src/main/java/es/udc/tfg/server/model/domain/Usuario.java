package es.udc.tfg.server.model.domain;

import javax.persistence.*;

import es.udc.tfg.server.model.domain.Enumerados.Autorizaciones;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_generator")
  @SequenceGenerator(name = "usuario_generator", sequenceName = "usuario_seq")
  private Long id;

  @Column
  private String nombre;

  @Column(unique = true)
  private String login;

  private String contrasena;
  @Column(unique = true)
  private String email;

  private Integer telefono;

  @Enumerated(EnumType.STRING)
  private Autorizaciones autorizaciones;

  private boolean activo;

  @OneToMany(mappedBy = "usuario",fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
  private List<Piso> pisos = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "usuarioFavorito")
  private Set<Habitacion> habitacionesFavoritas = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY)
  private Set<Alquiler> alquileres = new HashSet<>();

  public Usuario() {
  }

  public Usuario(Integer telefono,String nombre,String login, String contrasena, String email) {
    this.telefono = telefono;
    this.nombre = nombre;
    this.login = login;
    this.contrasena = contrasena;
    this.email = email;
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

  public String getContrasena() {
    return contrasena;
  }

  public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getTelefono() {
    return telefono;
  }

  public void setTelefono(Integer telefono) {
    this.telefono = telefono;
  }

  public Autorizaciones getAutorizaciones() {
    return autorizaciones;
  }

  public void setAutorizaciones(Autorizaciones autorizaciones) {
    this.autorizaciones = autorizaciones;
  }

  public boolean isActive() {
    return activo;
  }

  public void setActive(boolean activo) {
    this.activo = activo;
  }

  public List<Piso> getPisos() {
    return pisos;
  }

  public void setPisos(List<Piso> pisos) {
    this.pisos = pisos;
  }

  public Set<Habitacion> getHabitacionesFavoritas() {
    return habitacionesFavoritas;
  }

  public void setHabitacionesFavoritas(Set<Habitacion> habitacionesFavoritas) {
    this.habitacionesFavoritas = habitacionesFavoritas;
  }


  public Set<Alquiler> getAlquileres() {
    return alquileres;
  }

  public void setAlquileres(Set<Alquiler> alquileres) {
    this.alquileres = alquileres;
  }
}
