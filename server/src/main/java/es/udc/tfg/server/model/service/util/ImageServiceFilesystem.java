package es.udc.tfg.server.model.service.util;

import es.udc.tfg.server.config.Properties;
import es.udc.tfg.server.model.domain.Imagen;
import es.udc.tfg.server.model.exception.ModelException;
import es.udc.tfg.server.model.repository.ImagenDao;
import es.udc.tfg.server.model.service.dto.ImagenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageServiceFilesystem implements ImageService {

  @Autowired
  Properties properties;

  @Autowired
  ImagenDao imagenDao;

  private Path rootLoc;

  @Override
  public String saveImage(MultipartFile file,Long idImagen) throws ModelException {
    if (file.isEmpty()) {
      throw new ModelException("No se ha enviado ningún fichero");
    }
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    try (InputStream inputStream = file.getInputStream()) {
      Files.copy(inputStream, getRootLoc().resolve(idImagen + getExtension(filename)), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
      throw new ModelException("Problema procesando el fichero");
    }

    return idImagen + getExtension(filename);
  }


  @Override
  public void deleteImage(String filename,Long idImagen) throws ModelException {
    try {
      Imagen imagen = imagenDao.find(idImagen);
      Files.delete(getRootLoc().resolve(imagen.getPath()));
    } catch (IOException e) {
      e.printStackTrace();
      throw new ModelException("Problema procesando el fichero");
    }
  }


  @Override
  public ImagenDTO getImage(String imagePath) throws ModelException {
    try {
      InputStream is = new FileInputStream(properties.getImagesPath() + imagePath);
      byte[] buffer = new byte[1024];
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      int len;
      while ((len = is.read(buffer)) > -1) {
        os.write(buffer, 0, len);
      }
      InputStream imageIs = new ByteArrayInputStream(os.toByteArray());
      os.flush();
      is.close();
      return new ImagenDTO(imageIs, imagePath, getImageMediaType(imagePath));
    } catch (IOException e) {
      e.printStackTrace();
      throw new ModelException("Problem while getting the image");
    }
  }

  private Path getRootLoc() {
    if (rootLoc == null)
      this.rootLoc = Paths.get(properties.getImagesPath());
    return rootLoc;
  }

  private String getExtension(String filename) {
    return filename.substring(filename.lastIndexOf("."));
  }

  private String getImageMediaType(String filename) {
    String extension = getExtension(filename);
    switch (extension) {
      case ".jpg":
      case ".jpeg":
        return MediaType.IMAGE_JPEG_VALUE;
      case ".png":
        return MediaType.IMAGE_PNG_VALUE;
      case ".gif":
        return MediaType.IMAGE_GIF_VALUE;
      default:
        return MediaType.IMAGE_JPEG_VALUE;
    }
  }
}

