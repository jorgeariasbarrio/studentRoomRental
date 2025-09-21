package es.udc.tfg.server.web;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import es.udc.tfg.server.model.service.CentrosEstudiosService;
import es.udc.tfg.server.model.service.dto.CentrosEstudiosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cEstudios")
public class CentrosEstudioResource {

  @Autowired
  CentrosEstudiosService centrosEstudiosService;

  @GetMapping
  public List<CentrosEstudiosDTO> findAll(@RequestParam(required = false) String provincia){
    return centrosEstudiosService.findAll(provincia);
  }

  GeometryFactory geometryFactory = new GeometryFactory();
  @GetMapping("/distancia")
  public Double distanciaCEstudiosPiso(@RequestParam String ubicacionCEstudios,String ubicacionPiso){
    String[] coordenadasCEstudios = ubicacionCEstudios.split(",");
    String[] coordenadasPiso = ubicacionPiso.split(",");

    return centrosEstudiosService.distanciaCEstudiosPiso(
      geometryFactory.createPoint(new Coordinate(Double.parseDouble(coordenadasCEstudios[0]),
        Double.parseDouble(coordenadasCEstudios[1]))),
      geometryFactory.createPoint(new Coordinate(Double.parseDouble(coordenadasPiso[0]),
        Double.parseDouble(coordenadasPiso[1]))));

  }
}
