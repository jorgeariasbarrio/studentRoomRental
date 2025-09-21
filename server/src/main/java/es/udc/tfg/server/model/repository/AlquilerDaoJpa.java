package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.Alquiler;
import es.udc.tfg.server.model.domain.CentrosEstudios;
import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlquilerDaoJpa extends GenericDaoJpa implements AlquilerDao {
  @Override
  public void create(Alquiler alquiler) {
    entityManager.persist(alquiler);
  }

  @Override
  public void update(Alquiler alquiler) {
    entityManager.merge(alquiler);
  }

  @Override
  public Alquiler findById(Long id) {
    return entityManager.find(Alquiler.class, id);
  }

  @Override
  public void deleteById(Long id) {
    Alquiler alquiler = findById(id);
    delete(alquiler);
  }

  private void delete(Alquiler alquiler) {
    entityManager.remove(alquiler);
  }



  @Override
  public Alquiler findAlquilerAbierto(Long id) {
    // Una habitación no puede tener más de un alquiler abierto en ningún momento, por lo que llega con buscar el que no haya finalizado.
    try {
      List<Alquiler> alquiler =  entityManager.createQuery("from Alquiler where habitacionAlquiler=" + id + "and fecha_fin=null", Alquiler.class).getResultList();
      if(alquiler.size() == 1){
        return entityManager.createQuery("from Alquiler where habitacionAlquiler=" + id + "and fecha_fin=null", Alquiler.class).getSingleResult();
      }
      else{
        return null;
      }
    }catch (Error error){
      return null;
    }
  }
}
