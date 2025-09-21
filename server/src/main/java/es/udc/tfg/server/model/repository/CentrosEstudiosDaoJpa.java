package es.udc.tfg.server.model.repository;

import com.vividsolutions.jts.geom.Point;
import es.udc.tfg.server.model.domain.CentrosEstudios;
import es.udc.tfg.server.model.repository.util.GenericDaoJpa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CentrosEstudiosDaoJpa extends GenericDaoJpa implements CentrosEstudiosDao {


  @Override
  public List<CentrosEstudios> findAll(String provincia) {
    if (provincia != null) {
      return entityManager.createQuery("from CentrosEstudios where provincia = '" + provincia + "'", CentrosEstudios.class).getResultList();
    } else {
      return entityManager.createQuery("from CentrosEstudios", CentrosEstudios.class).getResultList();
    }
  }

  @Override
  public CentrosEstudios findById(Long id) {
    return entityManager.find(CentrosEstudios.class, id);
  }

  @Override
  public Double distanciaEntreCEstudiosYPiso(Point ubicacionPiso, Point ubicacionCEstudios) {
    //"SELECT ST_Distance(ubicacionPiso,ubicacionCEstudios)"
      //String consulta = "select ST_DISTANCE(ST_GEOMFROMTEXT('"+ubicacionPiso+"'),('"+ubicacionCEstudios+"'),TRUE) FROM CentrosEstudios" ;
    String consulta = "select ST_DISTANCE('"+ubicacionPiso+"','"+ubicacionCEstudios+"',TRUE)";
    //SELECT ST_DISTANCE (ST_GEOMFROMTEXT ('POINT(300000 4500000)'),ST_GEOMFROMTEXT('POINT(300100 4500000)'));
    return Double.parseDouble(entityManager.createNativeQuery(consulta).getSingleResult().toString());
  }

  @Override
  public void create(CentrosEstudios centrosEstudios) {
    entityManager.persist(centrosEstudios);
  }

  @Override
  public void update(CentrosEstudios centrosEstudios) {
    entityManager.merge(centrosEstudios);
  }

  @Override
  public void deleteById(Long id) {
    CentrosEstudios cEstudios = findById(id);
    delete(cEstudios);
  }

  private void delete(CentrosEstudios cEstudios) {
    entityManager.remove(cEstudios);
  }
}
