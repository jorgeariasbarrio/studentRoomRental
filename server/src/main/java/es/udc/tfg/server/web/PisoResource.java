package es.udc.tfg.server.web;


import es.udc.tfg.server.model.domain.Enumerados.Provincias;
import es.udc.tfg.server.model.domain.Habitacion;
import es.udc.tfg.server.model.domain.Piso;
import es.udc.tfg.server.model.domain.ProvinciasMunicipios;
import es.udc.tfg.server.model.exception.NotFoundException;
import es.udc.tfg.server.model.exception.OperationNotAllowed;
import es.udc.tfg.server.model.repository.PisoDao;
import es.udc.tfg.server.model.service.PisoService;
import es.udc.tfg.server.model.service.dto.HabitacionDTO;
import es.udc.tfg.server.model.service.dto.PisoConHabitacionesDTO;
import es.udc.tfg.server.model.service.dto.PisoDTO;
import es.udc.tfg.server.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.tfg.server.web.exceptions.RequestBodyNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pisos")
public class PisoResource {
  @Autowired
  private PisoService pisoService;


  @GetMapping
  public List<PisoConHabitacionesDTO> findAll(@RequestParam(required = false) Long idUsuario){

    return pisoService.findAll(idUsuario);
  }

  @GetMapping("/{id}")
  public PisoDTO findById(@PathVariable Long id) throws RequestBodyNotValidException, NotFoundException {
    return pisoService.findById(id);
  }
  @GetMapping("/provincias")
  public List<ProvinciasMunicipios> findAllProvincias(){
    return pisoService.getProvincias(null);
  }
  @GetMapping("/provincias/{provincia}")
  public List<ProvinciasMunicipios> findMunicipiosByProvincia(@PathVariable String provincia){
    return pisoService.getProvincias(provincia);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping
  public PisoDTO create(@RequestBody @Valid PisoDTO piso, Errors errors) throws RequestBodyNotValidException, OperationNotAllowed {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }
    return pisoService.create(piso);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/anadirHabitacion/{idPiso}")
  public HabitacionDTO anadirHabitacion(@PathVariable Long idPiso, @RequestBody @Valid HabitacionDTO habitacion, Errors errors) throws RequestBodyNotValidException, OperationNotAllowed {
    if (errors.hasErrors()){
      throw new RequestBodyNotValidException(errors);
    }
    return pisoService.anadirHabitacion(habitacion,idPiso);
  }
  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{id}")
  public PisoDTO update(@PathVariable Long id, @RequestBody @Valid PisoDTO piso, Errors errors) throws RequestBodyNotValidException, NotFoundException, IdAndBodyNotMatchingOnUpdateException, OperationNotAllowed {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }
      if (id != piso.getId()) {
        throw new IdAndBodyNotMatchingOnUpdateException(Piso.class);
      }
      return pisoService.update(piso);
  }

  @PreAuthorize("isAuthenticated()")
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws  NotFoundException, OperationNotAllowed {
    pisoService.deleteById(id);
  }

}
