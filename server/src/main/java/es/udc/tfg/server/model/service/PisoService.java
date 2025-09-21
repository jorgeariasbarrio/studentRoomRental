package es.udc.tfg.server.model.service;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.udc.tfg.server.model.domain.Enumerados.OpcionesDiscriminantes;
import es.udc.tfg.server.model.domain.Opciones;
import es.udc.tfg.server.model.domain.Piso;
import es.udc.tfg.server.model.domain.ProvinciasMunicipios;
import es.udc.tfg.server.model.domain.Usuario;
import es.udc.tfg.server.model.domain.Enumerados.Autorizaciones;

import es.udc.tfg.server.model.exception.OperationNotAllowed;
import es.udc.tfg.server.model.exception.UserEmailExistsException;
import es.udc.tfg.server.model.exception.UserLoginExistsException;
import es.udc.tfg.server.model.repository.*;
import es.udc.tfg.server.model.service.dto.*;
import es.udc.tfg.server.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfg.server.model.exception.NotFoundException;
import com.vividsolutions.jts.*;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PisoService {

  @Autowired
  private PisoDao pisoDAO;

  @Autowired
  private UsuarioDao userDAO;

  @Autowired
  private HabitacionService habitacionService;

  @Autowired
  private OpcionesDao opcionesDAO;

  @Autowired
  private ProvinciasMunicipiosDao provinciasMunicipiosDao;


  public List<PisoConHabitacionesDTO> findAll(Long idUsuario) {
    Stream<PisoConHabitacionesDTO> pisos = pisoDAO.findAll(idUsuario).stream().map(piso -> new PisoConHabitacionesDTO(piso));
    return pisos.collect(Collectors.toList());
  }


  private UsuarioDTOPrivado getCurrentUserWithAuthority() {
    String currentUserLogin = SecurityUtils.getCurrentUserLogin();
    if (currentUserLogin != null) {
      return new UsuarioDTOPrivado(userDAO.findByLogin(currentUserLogin));
    }
    return null;
  }


  public PisoDTO findById(Long id) throws NotFoundException {
    Piso piso = pisoDAO.findById(id);
    if (piso == null) {
      throw new NotFoundException(id.toString(), Piso.class);
    }
    return new PisoDTO(piso);
  }

  @Transactional(readOnly = false)
  public HabitacionDTO anadirHabitacion(HabitacionDTO habitacion,Long idPiso) throws OperationNotAllowed {
    UsuarioDTOPrivado usuario = getCurrentUserWithAuthority();
    if (usuario == null) {
      throw new OperationNotAllowed("No hay usuario logueado");
    }
    Piso piso = pisoDAO.findById(idPiso);
    if(!Objects.equals(piso.getUsuario().getId(), usuario.getId())){
      throw new OperationNotAllowed("El usuario logueado no es el creador del piso");
    }
    if (piso.getHabitaciones().size() == piso.getN_habitacion()){
      throw new OperationNotAllowed("No puede haber más habitaciones libres que el total de habitaciones");
    }
    habitacion.setPiso(new PisoDTO(piso));
    return habitacionService.create(habitacion);
  }


  @Transactional(readOnly = false)
  public PisoDTO create(PisoDTO piso) throws OperationNotAllowed {

    Piso pisoBd = new Piso();
    pisoBd.setPosicion(piso.getPosicion());
    pisoBd.setProvincia(piso.getProvincia());
    pisoBd.setCodigoPostal(piso.getCodigoPostal());
    pisoBd.setMunicipio(piso.getMunicipio());
    pisoBd.setDireccion(piso.getDireccion());
    pisoBd.setPiso(piso.getPiso());
    pisoBd.setN_banos(piso.getnBanos());
    pisoBd.setN_habitacion(piso.getnHabitaciones());
    pisoBd.setM_cuadrados(piso.getmCuadrados());
    pisoBd.setEdadMaxima(piso.getEdadMaxima());
    pisoBd.setEdadMinima(piso.getEdadMinima());
    pisoBd.setNombreUsuario(piso.getNombreUsuario());
    pisoBd.setEmailUsuario(piso.getEmailUsuario());
    pisoBd.setTelefonoUsuario(piso.getTelefonoUsuario());


    UsuarioDTOPrivado usuarioActual = getCurrentUserWithAuthority();
    if (usuarioActual == null) {
      throw new OperationNotAllowed("No hay usuario logueado");
    } else {
      pisoBd.setUsuario(userDAO.findById(usuarioActual.getId()));

      if (piso.getOpciones() != null) {
        piso.getOpciones().forEach(opcion -> {
          pisoBd.getOpciones().add(opcionesDAO.findById(opcion.getId()));
        });
      }

      pisoDAO.create(pisoBd);

      return new PisoDTO(pisoBd);
    }
  }

  @Transactional(readOnly = false)
  public PisoDTO update(PisoDTO piso) throws NotFoundException, OperationNotAllowed {

    Piso a = pisoDAO.findById(piso.getId());
    if (a == null) {
      throw new NotFoundException(piso.getId().toString(), Piso.class);
    }

    a.setPosicion(piso.getPosicion());
    a.setProvincia(piso.getProvincia());
    a.setCodigoPostal(piso.getCodigoPostal());
    a.setMunicipio(piso.getMunicipio());
    a.setDireccion(piso.getDireccion());
    a.setPiso(piso.getPiso());
    a.setN_banos(piso.getnBanos());
    a.setN_habitacion(piso.getnHabitaciones());
    a.setM_cuadrados(piso.getmCuadrados());
    a.setEdadMaxima(piso.getEdadMaxima());
    a.setEdadMinima(piso.getEdadMinima());
    a.setNombreUsuario(piso.getNombreUsuario());
    a.setEmailUsuario(piso.getEmailUsuario());
    a.setTelefonoUsuario(piso.getTelefonoUsuario());

    UsuarioDTOPrivado usuarioActual = getCurrentUserWithAuthority();
    if (!usuarioActual.getId().equals(a.getUsuario().getId())) {
      throw new OperationNotAllowed("Current user does not match piso creator");
    }
    List<Opciones> opcionesGenero = opcionesDAO.findAll("GENERO");
    for (Opciones opciones : opcionesGenero) {
      a.getOpciones().remove(opciones);
    }
    if (piso.getOpciones() != null) {
      piso.getOpciones().forEach(opcion -> {
        a.getOpciones().add(opcionesDAO.findById(opcion.getId()));
      });
    }
    pisoDAO.update(a);

    return new PisoDTO(a);
  }


  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    Piso piso = pisoDAO.findById(id);
    if (piso == null) {
      throw new NotFoundException(id.toString(), Usuario.class);
    }

    UsuarioDTOPrivado creadorPiso = getCurrentUserWithAuthority();
    if (!creadorPiso.getId().equals(piso.getUsuario().getId())) {
      throw new OperationNotAllowed("El usuario no puede borrar pisos de otros");
    }

    pisoDAO.deleteById(id);
  }

  @Transactional(readOnly = false)
  public List<ProvinciasMunicipios> getProvincias(String provincia) {
    if (provincia != null) {
      return provinciasMunicipiosDao.findAllByProvincia(provincia);
    } else {
      return provinciasMunicipiosDao.findAll();
    }
  }
}
