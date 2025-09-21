package es.udc.tfg.server.model.service;

import es.udc.tfg.server.model.repository.OpcionesDao;
import es.udc.tfg.server.model.repository.PisoDao;
import es.udc.tfg.server.model.repository.UsuarioDao;
import es.udc.tfg.server.model.service.dto.OpcionesDTO;
import es.udc.tfg.server.model.service.dto.PisoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class OpcionesService {


  @Autowired
  private PisoDao pisoDAO;

  @Autowired
  private UsuarioDao userDAO;

  @Autowired
  private OpcionesDao opcionesDAO;


  public List<OpcionesDTO> findAll(String categoria) {
    Stream<OpcionesDTO> opciones = opcionesDAO.findAll(categoria).stream().map(opcion -> new OpcionesDTO(opcion));
    return opciones.collect(Collectors.toList());
  }
}

