package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.domain.Opciones;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface HabitacionDao {

  //Decidir si es mejor realizar una nueva funcionalidad o si es mejor implementar
  //todos los filtros en la misma funcionalidad.
  List<Habitacion> findAll(Long idPiso,Boolean isAdmin);

  List<Habitacion> findFilter(String provincia, LocalDate inicioContrato, LocalDate finContrato, Double distanciaMaxima, Long idCentroEstudios, Integer precioMaximo, Integer numHabitaciones, String opciones, String ordenacion,Long idUsuario,String municipio);

  Habitacion findById(Long id);

  void create(Habitacion habitacion);

  void update(Habitacion habitacion);

  void deleteById(Long id);
}
