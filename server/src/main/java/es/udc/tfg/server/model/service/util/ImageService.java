package es.udc.tfg.server.model.service.util;

import es.udc.tfg.server.model.exception.ModelException;
import es.udc.tfg.server.model.service.dto.ImagenDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  String saveImage(MultipartFile file, Long idImagen) throws ModelException;

  ImagenDTO getImage(String imagePath) throws ModelException;

  void deleteImage(String fileName,Long idImagen) throws ModelException;
}

