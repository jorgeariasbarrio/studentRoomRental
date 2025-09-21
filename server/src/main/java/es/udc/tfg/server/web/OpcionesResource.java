package es.udc.tfg.server.web;

import es.udc.tfg.server.model.service.OpcionesService;
import es.udc.tfg.server.model.service.PisoService;
import es.udc.tfg.server.model.service.dto.OpcionesDTO;
import es.udc.tfg.server.model.service.dto.PisoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/opciones")
public class OpcionesResource {
  @Autowired
  private OpcionesService opcionesService;


  @GetMapping("/{categoria}")
  public List<OpcionesDTO> findAll(@PathVariable String categoria){
    return opcionesService.findAll(categoria);
  }
}
