package es.udc.tfg.server.model.repository;

import com.vividsolutions.jts.geom.Point;
import es.udc.tfg.server.model.domain.CentrosEstudios;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CentrosEstudiosDao {

  List<CentrosEstudios> findAll(String provincia);

  CentrosEstudios findById(Long id);
  Double distanciaEntreCEstudiosYPiso(Point ubicacionPiso, Point ubicacionCEstudios);

  void create(CentrosEstudios centrosEstudios);

  void update(CentrosEstudios centrosEstudios);

  void deleteById(Long id);
}
