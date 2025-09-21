package es.udc.tfg.server.model.repository;

import es.udc.tfg.server.model.domain.ProvinciasMunicipios;
import es.udc.tfg.server.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ProvinciasMunicipiosDaoJpa extends GenericDaoJpa implements ProvinciasMunicipiosDao {

  @Override
  public List<ProvinciasMunicipios> findAllByProvincia(String provincia) {
    return entityManager.createQuery("from ProvinciasMunicipios where provincia ='" + provincia + "'" + "ORDER BY municipio ASC ", ProvinciasMunicipios.class).getResultList();
  }

  @Override
  public List<ProvinciasMunicipios> findAll() {
    return entityManager.createQuery("from ProvinciasMunicipios", ProvinciasMunicipios.class).getResultList();
  }

  @Override
  public void create(ProvinciasMunicipios provincias) {
    {
      entityManager.persist(provincias);
    }
  }
}
