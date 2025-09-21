package es.udc.tfg.server.model.service;

import com.vividsolutions.jts.geom.Point;
import es.udc.tfg.server.model.repository.CentrosEstudiosDao;
import es.udc.tfg.server.model.service.dto.CentrosEstudiosDTO;
import es.udc.tfg.server.model.service.dto.HabitacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CentrosEstudiosService {

  @Autowired
  CentrosEstudiosDao centrosEstudiosDao;

  @Transactional(readOnly = true)
  public List<CentrosEstudiosDTO> findAll(String provincia) {
    Stream<CentrosEstudiosDTO> centrosEstudio = centrosEstudiosDao.findAll(provincia).stream().map(centroEstudios -> new CentrosEstudiosDTO(centroEstudios));
    return centrosEstudio.collect(Collectors.toList());
  }
  @Transactional(readOnly = true)
  public Double distanciaCEstudiosPiso(Point ubicacionCEstudios, Point ubicacionPiso){
    return centrosEstudiosDao.distanciaEntreCEstudiosYPiso(ubicacionPiso,ubicacionCEstudios);
  }

}
