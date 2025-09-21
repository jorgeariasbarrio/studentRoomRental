package es.udc.tfg.server.model.repository;

import com.vividsolutions.jts.geom.Point;
import es.udc.tfg.server.model.domain.CentrosEstudios;
import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.domain.Opciones;
import es.udc.tfg.server.model.domain.Piso;
import es.udc.tfg.server.model.repository.util.GenericDaoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class HabitacionDaoJpa extends GenericDaoJpa implements HabitacionDao {

  @Autowired
  CentrosEstudiosDao centrosEstudiosDao;
  @Override
  public List<Habitacion> findAll(Long idPiso,Boolean isAdmin) {

    //Falta solo recuperar las habitaciones de usuarios que no esten desactivados.
    if(idPiso == null && !isAdmin){
    return entityManager.createQuery("select h from Habitacion h join h.piso p join p.usuario u where u.activo = true and h.estadoHabitacion = 'DISPONIBLE'", Habitacion.class).getResultList();
    }
    if (isAdmin && idPiso == null){
      return entityManager.createQuery("select h from Habitacion h join h.piso p join p.usuario u order by h.id", Habitacion.class).getResultList();
    }

    //Luego añadiremos todos los filtros disponibles(ciudad, universidad, distancia máxima, precio max, habitaciones libres, convivencia)
    else{
      return entityManager.createQuery("select h from Habitacion h join h.piso p join p.usuario u where u.activo = true and p.id = :idPiso and h.estadoHabitacion = 'DISPONIBLE'", Habitacion.class).setParameter("idPiso", idPiso).getResultList();
    }
  }

  @Override
  public List<Habitacion> findFilter(String provincia, LocalDate inicioContrato, LocalDate finContrato , Double distanciaMaxima, Long idCentroEstudios, Integer precioMaximo, Integer numHabitaciones, String opciones, String ordenacion,Long idUsuario,String municipio) {
    Integer numeroParametros = 0;
    String query = "SELECT h.* FROM Habitacion h join piso p on p.id=h.piso_id join usuario u on p.usuario_id=u.id";
    if(provincia != null || distanciaMaxima !=null || idCentroEstudios != null || precioMaximo!=null || numHabitaciones!=null || opciones !=null || inicioContrato!=null || finContrato!= null || idUsuario !=null || municipio!=null){
    query +=" where ";
    }
    if (provincia != null){
      query+= "p.provincia =" + "'" + provincia + "'";
      numeroParametros +=1;
    }
    if (municipio != null){
      if(numeroParametros > 0) query+=" and ";
      query+= "p.municipio =" + "'" + municipio + "'";
      numeroParametros +=1;
    }
    if (inicioContrato !=null){
      if(numeroParametros > 0) query+=" and ";
      query += " inicio_contrato <=" + "'"+inicioContrato+"'";
      numeroParametros+=1;
    }
    if (finContrato !=null){
      if(numeroParametros > 0) query+=" and ";
      query += " fin_contrato >=" +"'" + finContrato+"'";
      numeroParametros+=1;
    }
    if(distanciaMaxima != null && idCentroEstudios!=null){
      if(numeroParametros > 0) query+=" and ";
      Point centroEstudiosLocalizacion = centrosEstudiosDao.findById(idCentroEstudios).getLocalizacion();
      query += "ST_DISTANCE('" + centroEstudiosLocalizacion + "', p.posicion,TRUE)/1000 <" + distanciaMaxima;
      numeroParametros +=1;
    }
    if (precioMaximo != null){
      if(numeroParametros > 0) query+=" and ";
      query+="precio <= " + precioMaximo;
      numeroParametros +=1;
    }
    if (numHabitaciones != null){
      if(numeroParametros > 0) query+=" and ";
      query += "(SELECT COUNT(*) FROM habitacion WHERE piso_id = h.piso_id)=" + numHabitaciones;
      numeroParametros +=1;
    }
   // select * from habitacion where piso_id in (select distinct p.id from piso p natural join piso_opciones po where po.opciones_id in (1,2,3));
    if (opciones != null){
      if(numeroParametros > 0) query+=" and ";
      String idsOpciones = "(" + opciones + ")";
      query += "piso_id in (select distinct p.id from piso p join piso_opciones po on p.id = po.pisos_id where po.opciones_id in" + idsOpciones + ")";
      numeroParametros +=1;
    }
    if (idUsuario != null){
      if(numeroParametros > 0) query+=" and ";
      query += "p.usuario_id = "+ idUsuario;
      numeroParametros +=1;
    }
    if(numeroParametros > 0) query+=" and estado_habitacion='DISPONIBLE'";
    else{
      query +=" where estado_habitacion='DISPONIBLE'";
      numeroParametros +=1;
    }
    if(numeroParametros > 0) query+=" and u.activo=true";
    else{
      query +=" where u.activo=true";
    }
    if (ordenacion != null) {
      query += " ORDER BY";
      switch (ordenacion) {
        case "Menor distancia":
          Point centroEstudiosLocalizacion = centrosEstudiosDao.findById(idCentroEstudios).getLocalizacion();
          query += " ST_DISTANCE('" + centroEstudiosLocalizacion + "', p.posicion,TRUE)/1000";
          break;
        case "Mas baratos":
          query += " h.precio ASC";
          break;
        case "Mas caros":
          query += " h.precio DESC";
          break;
        case "Mas recientes":
          query += " h.fecha_creacion ASC";
          break;
      }
    }
    if (numeroParametros == 0 && ordenacion == null){
      return entityManager.createQuery("select h from Habitacion h join h.piso p join p.usuario u where u.activo = true and h.estadoHabitacion = 'DISPONIBLE'", Habitacion.class).getResultList();
    }
    return entityManager.createNativeQuery(query,Habitacion.class).getResultList();
  }

  @Override
  public Habitacion findById(Long id) {
    return entityManager.find(Habitacion.class, id);
  }

  @Override
  public void create(Habitacion habitacion) {
    entityManager.persist(habitacion);
  }

  @Override
  public void update(Habitacion habitacion) {
    entityManager.merge(habitacion);
  }

  @Override
  public void deleteById(Long id) {
    Habitacion habitacion = findById(id);
    delete(habitacion);
  }

  private void delete(Habitacion habitacion) {
    entityManager.remove(habitacion);
  }

}
