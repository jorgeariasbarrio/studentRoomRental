package es.udc.tfg.server.config;

import es.udc.tfg.server.model.domain.*;
import es.udc.tfg.server.model.domain.Enumerados.OpcionesDiscriminantes;
import es.udc.tfg.server.model.domain.Enumerados.Provincias;

import es.udc.tfg.server.model.domain.Enumerados.Autorizaciones;
import es.udc.tfg.server.model.exception.UserLoginExistsException;
import es.udc.tfg.server.model.repository.*;
import es.udc.tfg.server.model.service.PisoService;
import es.udc.tfg.server.model.service.UsuarioService;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class DatabaseLoader {
  @Autowired
  private UsuarioDao usuarioDAO;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private PisoService pisoService;
  @Autowired
  private PisoDao pisoDAO;
  @Autowired
  private OpcionesDao opcionesDAO;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private HabitacionDao habitacionDao;

  @Autowired
  private ImagenDao imagenDao;

  @Autowired
  private ProvinciasMunicipiosDao provinciasMunicipiosDao;

  @Autowired
  private CentrosEstudiosDao centrosEstudiosDao;

  @Autowired
  private Properties properties;


  /*
   * Para hacer que la carga de datos sea transacional, hay que cargar el propio
   * objeto como un bean y lanzar el método una vez cargado, ya que en el
   * PostConstruct (ni similares) se tienen en cuenta las anotaciones de
   * transaciones.
   */


  public Set<String> listFilesUsingFilesList(String dir) throws IOException {
    try (Stream<Path> stream = Files.list(Paths.get(dir))) {
      return stream
        .filter(file -> !Files.isDirectory(file))
        .map(Path::getFileName)
        .map(Path::toString)
        .collect(Collectors.toSet());
    }
  }
  private static List<ProvinciasMunicipios> readProvinciassFromCSV() {
    List < ProvinciasMunicipios > provincias = new ArrayList< >();
    Path pathToFile = Paths.get("src/main/java/es/udc/tfg/server/config/listaMunicipiosProvincias.csv"); // create an instance of BufferedReader // using try with resource, Java 7 feature to close resources
    try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)) { // read the first line from the text file
      String line = br.readLine(); // loop until all lines are read
      while (line != null) { // use string.split to load a string array with the values from // each line of // the file, using a comma as the delimiter
        String[] attributes = line.split(",");
        ProvinciasMunicipios provincia = createProvincias(attributes); // adding book into ArrayList
        provincias.add(provincia); // read next line before looping // if end of file reached, line would be null
        line = br.readLine();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return provincias;
  }

  private static ProvinciasMunicipios createProvincias(String[] metadata) {
    String provincia = metadata[1];
    String municipio = metadata[0];
    return new ProvinciasMunicipios(provincia,municipio);
  }
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void loadData() throws UserLoginExistsException, InterruptedException, IOException {

    GeometryFactory geometryFactory = new GeometryFactory();

    List<ProvinciasMunicipios> provinciasAInsertar = readProvinciassFromCSV();
    for(int i=0;i<provinciasAInsertar.size();i++){
      provinciasMunicipiosDao.create(provinciasAInsertar.get(i));
    }

    CentrosEstudios CampusElvina = new CentrosEstudios("Campus Coruña - Elviña", "A Coruña", "Elviña", geometryFactory.createPoint(new Coordinate(43.33351565621406, -8.409396588112687)));
    CentrosEstudios CampusZapateira = new CentrosEstudios("Campus Coruña - Zapateira", "A Coruña", "Zapateira", geometryFactory.createPoint(new Coordinate(43.32687967326783, -8.407954401605037)));
    CentrosEstudios CampusOza = new CentrosEstudios("Campus Coruña - Oza", "A Coruña", "Oza", geometryFactory.createPoint(new Coordinate(43.34771593199585, -8.386900615096925)));

    centrosEstudiosDao.create(CampusElvina);
    centrosEstudiosDao.create(CampusZapateira);
    centrosEstudiosDao.create(CampusOza);


    String pass1 = passwordEncoder.encode("admin");

    Usuario admin = new Usuario(678898765,"Admin","admin", pass1, "admin@admin.es");
    admin.setAutorizaciones(Autorizaciones.ADMIN);
    admin.setActive(true);

    String pass2 = passwordEncoder.encode("jorge");

    Usuario jorge = new Usuario(786565463,"Jorge Arias Barrio","jorge", pass2, "jorge@prueba.es");
    jorge.setAutorizaciones(Autorizaciones.USER);
    jorge.setActive(true);


    String pass3 = passwordEncoder.encode("ramon");

    Usuario ramon = new Usuario(123456789, "Ramón García Pérez", "ramon", pass3, "ramon@prueba.es");
    ramon.setAutorizaciones(Autorizaciones.USER);
    ramon.setActive(true);

    String pass4 = passwordEncoder.encode("pepe");

    Usuario pepe = new Usuario(498765403, "Francisco Fernández Ramirez", "pepe", pass4, "pepe@prueba.es");
    pepe.setAutorizaciones(Autorizaciones.USER);
    pepe.setActive(true);

    usuarioDAO.create(admin);
    usuarioDAO.create(jorge);
    usuarioDAO.create(ramon);
    usuarioDAO.create(pepe);


    Opciones fumadores = new Opciones("Se aceptan fumadores", "fire", OpcionesDiscriminantes.NORMAS);
    Opciones mascotas = new Opciones("Se aceptan mascotas", "bi bi-tencent-qq", OpcionesDiscriminantes.NORMAS);
    Opciones parejas = new Opciones("Se aceptan parejas", "bi bi-people-fill", OpcionesDiscriminantes.NORMAS);
    Opciones soloChicas = new Opciones("Se aceptan solo chicas", "bi bi-gender-female", OpcionesDiscriminantes.GENERO);
    Opciones soloChicos = new Opciones("Se aceptan solo chicos", "bi bi-gender-male", OpcionesDiscriminantes.GENERO);
    Opciones generoIndiferente = new Opciones("No importa el genero", "bi bi-gender-ambiguous", OpcionesDiscriminantes.GENERO);
    Opciones ascensor = new Opciones("Ascensor", "bi bi-device-ssd-fill", OpcionesDiscriminantes.PISO);
    Opciones calefaccion = new Opciones("Calefaccion", "bi bi-thermometer-sun", OpcionesDiscriminantes.PISO);
    Opciones trastero = new Opciones("Trastero", "bi bi-door-open-fill", OpcionesDiscriminantes.PISO);
    Opciones terraza = new Opciones("Terraza", "bi bi-brightness-alt-high-fill", OpcionesDiscriminantes.PISO);
    Opciones lavadora = new Opciones("Lavadora", "bi bi-0-square", OpcionesDiscriminantes.PISO);
    Opciones vitroceramica = new Opciones("Vitroceramica", "bi bi-cup-hot-fill", OpcionesDiscriminantes.PISO);
    Opciones exterior = new Opciones("Exterior", "bi bi-brightness-alt-high-fill", OpcionesDiscriminantes.HABITACION);
    Opciones amueblada = new Opciones("Amueblada", "bi bi-wallet-fill", OpcionesDiscriminantes.HABITACION);
    Opciones llaveP = new Opciones("Llave Propia", "bi bi-key-fill", OpcionesDiscriminantes.HABITACION);
    Opciones movilidad = new Opciones("Adaptada para movilidad reducida", "bi bi-universal-access", OpcionesDiscriminantes.HABITACION);
    Opciones banoP = new Opciones("Baño propio", "bi bi-door-open", OpcionesDiscriminantes.HABITACION);
    Opciones balcon = new Opciones("Balcon", "bi bi-sunset", OpcionesDiscriminantes.HABITACION);



    opcionesDAO.create(fumadores);
    opcionesDAO.create(mascotas);
    opcionesDAO.create(parejas);
    opcionesDAO.create(soloChicas);
    opcionesDAO.create(soloChicos);
    opcionesDAO.create(generoIndiferente);
    opcionesDAO.create(ascensor);
    opcionesDAO.create(calefaccion);
    opcionesDAO.create(vitroceramica);
    opcionesDAO.create(terraza);
    opcionesDAO.create(lavadora);
    opcionesDAO.create(trastero);
    opcionesDAO.create(exterior);
    opcionesDAO.create(amueblada);
    opcionesDAO.create(llaveP);
    opcionesDAO.create(movilidad);
    opcionesDAO.create(banoP);
    opcionesDAO.create(balcon);



    Piso piso1 = new Piso(geometryFactory.createPoint(new Coordinate(10,10)), "A Coruña",15172,"A Coruña","Rua Prueba, 6",
      3,2,4,100, 18,99,"Jorge Arias Barrio","jorge.arias@prueba.es","657980432");
    piso1.getOpciones().add(fumadores);
    piso1.getOpciones().add(mascotas);
    piso1.getOpciones().add(soloChicas);
    piso1.setUsuario(jorge);
    pisoDAO.create(piso1);




    LocalDate finDeContratoHabitacion1 = LocalDate.of(2024,1,1);
    Habitacion habitacion1 = new Habitacion("Habitacion de Prueba","Perfectas condiciones",
      100,LocalDate.now(),LocalDate.now(),finDeContratoHabitacion1,500,200,false,700,pisoDAO.findById(piso1.getId()));
    habitacion1.getOpciones().add(amueblada);
    habitacion1.getOpciones().add(balcon);
    habitacion1.getOpciones().add(banoP);
    habitacionDao.create(habitacion1);



    //C. Uruguay, 5, 15004 A Coruña, La Coruña
    Piso piso2 = new Piso(geometryFactory.createPoint(new Coordinate(43.36312979384155, -8.410350551554329)), "A Coruña",15004,"A Coruña","Calle Uruguay, 5",
      1,2,2,100, 18,30,"Jorge Arias Barrio","jorge.arias@prueba.es","657980432");
    piso2.getOpciones().add(soloChicos);
    piso2.getOpciones().add(calefaccion);
    piso2.setUsuario(jorge);
    pisoDAO.create(piso2);


    LocalDate finDeContratohabitacion2 = LocalDate.of(2023,10,1);
    Habitacion habitacion2 = new Habitacion("Habitacion en la calle Uruguay, al lado del Palacio de la Ópera","Habitación en perfectas condiciones. Al lado del palacio de la opera.",
      30,LocalDate.now(),LocalDate.now(),finDeContratohabitacion2,300,100,false,300,pisoDAO.findById(piso2.getId()));
    habitacion2.getOpciones().add(amueblada);
    habitacion2.getOpciones().add(llaveP);

    habitacionDao.create(habitacion2);


    Habitacion habitacion3 = new Habitacion("Habitacion en la calle Uruguay, al lado del Palacio de la Ópera","Habitación en perfectas condiciones. Al lado del palacio de la opera.",
      35,LocalDate.now(),LocalDate.now(),finDeContratohabitacion2,400,100,false,300,pisoDAO.findById(piso2.getId()));
    habitacion3.getOpciones().add(amueblada);
    habitacion3.getOpciones().add(llaveP);
    habitacion3.getOpciones().add(banoP);

    habitacionDao.create(habitacion3);


    Piso piso4 = new Piso(geometryFactory.createPoint(new Coordinate(43.35453, -8.41556)), "A Coruña",15007,"A Coruña","Calle Revolución Francesa, 3",
      2,1,2,80, 18,26,"Ramón García Pérez","ramon@prueba.es","123456789");
    piso4.getOpciones().add(soloChicas);
    piso4.getOpciones().add(calefaccion);
    piso4.getOpciones().add(lavadora);
    piso4.setUsuario(ramon);
    pisoDAO.create(piso4);

    Habitacion habitacion5 = new Habitacion("Habitacion en la calle Revolución Francesa","Habitación pequeña pero en perfectas condiciones. Soy una estudiante de arquitectura que busca otra chica para compartir piso.",
      37,LocalDate.now(),LocalDate.now(),finDeContratohabitacion2,200,50,false,50,pisoDAO.findById(piso4.getId()));
    habitacion5.getOpciones().add(amueblada);
    habitacion5.getOpciones().add(llaveP);
    habitacion5.getOpciones().add(banoP);

    habitacionDao.create(habitacion5);

    Piso piso5 = new Piso(geometryFactory.createPoint(new Coordinate(43.37640, -8.42291)), "A Coruña",15011,"A Coruña","Grupo Viviendas María Pita, 26",
      1,2,3,150, 18,40,"Jorge Arias Barrio","jorge@prueba.es","786565463");
    piso5.getOpciones().add(generoIndiferente);
    piso5.getOpciones().add(calefaccion);
    piso5.getOpciones().add(trastero);
    piso5.getOpciones().add(terraza);
    piso5.setUsuario(jorge);
    pisoDAO.create(piso5);

    Habitacion habitacion6 = new Habitacion("Habitación en el Grupo de Viviendas Maria Pita, 26","Habitación pequeña pero en perfectas condiciones. En la actualidad solo me encuentro yo (un chico), pero el género es indiferente.",
      40,LocalDate.now(),LocalDate.now(),finDeContratohabitacion2,250,0,true,70,pisoDAO.findById(piso5.getId()));
    habitacion6.getOpciones().add(amueblada);
    habitacion6.getOpciones().add(llaveP);
    habitacion6.getOpciones().add(banoP);

    habitacionDao.create(habitacion6);

    Habitacion habitacion7 = new Habitacion("Habitación en el Grupo de Viviendas Maria Pita, 26","Habitación pequeña pero en perfectas condiciones. En la actualidad solo me encuentro yo (un chico), pero el género es indiferente.",
      40,LocalDate.now(),LocalDate.now(),finDeContratohabitacion2,250,0,true,70,pisoDAO.findById(piso5.getId()));
    habitacion7.getOpciones().add(amueblada);
    habitacion7.getOpciones().add(llaveP);
    habitacion7.getOpciones().add(banoP);

    habitacionDao.create(habitacion7);


    Piso piso6 = new Piso(geometryFactory.createPoint(new Coordinate(43.33787, -8.37137)), "A Coruña",15172,"Oleiros","Rúa Cubelos, 20, Perillo",
      3,1,3,90, 18,40,"Francisco Fernández Ramirez ","pepe@prueba.es","489675362");
    piso6.getOpciones().add(soloChicos);
    piso6.getOpciones().add(calefaccion);
    piso6.getOpciones().add(fumadores);
    piso6.getOpciones().add(mascotas);
    piso6.getOpciones().add(terraza);
    piso6.setUsuario(pepe);
    pisoDAO.create(piso6);


    Habitacion habitacion8 = new Habitacion("Habitación en Rúa Cubelos, Perillo","Habitación pequeña pero en perfectas condiciones. Busco un compañero de piso. Habitación espaciosa y con muy buenas vistas. ",
      40,LocalDate.now(),LocalDate.now(),finDeContratohabitacion2,300,50,false,0,pisoDAO.findById(piso6.getId()));
    habitacion8.getOpciones().add(amueblada);
    habitacion8.getOpciones().add(llaveP);
    habitacion8.getOpciones().add(banoP);

    habitacionDao.create(habitacion8);

    Habitacion habitacion9 = new Habitacion("Habitación en Rúa Cubelos, Perillo","Habitación pequeña pero en perfectas condiciones. Busco un compañero de piso. Habitación espaciosa y con muy buenas vistas. ",
      40,LocalDate.now(),LocalDate.now(),finDeContratohabitacion2,300,50,false,0,pisoDAO.findById(piso6.getId()));
    habitacion9.getOpciones().add(amueblada);
    habitacion9.getOpciones().add(llaveP);
    habitacion9.getOpciones().add(banoP);

    habitacionDao.create(habitacion9);

    Piso piso7 = new Piso(geometryFactory.createPoint(new Coordinate(43.38482, -8.39682)), "A Coruña",15002,"A Coruña","Plaza do Galaeta, 5, Montealto ",
      1,2,3,100, 18,50,"Jorge Arias Barrio ","jorge@prueba.es","123456789");
    piso7.getOpciones().add(generoIndiferente);
    piso7.getOpciones().add(calefaccion);
    piso7.getOpciones().add(lavadora);
    piso7.getOpciones().add(terraza);
    piso7.setUsuario(jorge);
    pisoDAO.create(piso7);

    Habitacion habitacion10 = new Habitacion("Habitación en la Plaza do Galaeta,5, Montealto","Habitación pequeña pero en perfectas condiciones. Busco dos compañeros de piso, sin importar el género. Habitación espaciosa y muy cerca de la Torre de Hércules. ",
      50,LocalDate.now(),LocalDate.now(),finDeContratohabitacion2,250,0,true,0,pisoDAO.findById(piso7.getId()));
    habitacion10.getOpciones().add(amueblada);
    habitacion10.getOpciones().add(llaveP);
    habitacion10.getOpciones().add(banoP);

    habitacionDao.create(habitacion10);

    Habitacion habitacion11 = new Habitacion("Habitación en la Plaza do Galaeta, 5, Montealto","Habitación pequeña pero en perfectas condiciones. Busco dos compañeros de piso, sin importar el género. Habitación espaciosa y muy cerca de la Torre de Hércules. ",
      50,LocalDate.now(),LocalDate.now(),finDeContratohabitacion2,250,0,true,0,pisoDAO.findById(piso7.getId()));
    habitacion11.getOpciones().add(amueblada);
    habitacion11.getOpciones().add(llaveP);
    habitacion11.getOpciones().add(banoP);

    habitacionDao.create(habitacion11);

    Piso piso8 = new Piso(geometryFactory.createPoint(new Coordinate(43.33260, -8.42491)), "A Coruña",15190,"Mesoiro","Estrada de Mesoiro, 20",
      1,1,1,60, 18,40,"Francisco Fernández Ramirez ","pepe@prueba.es","489675362");
    piso8.getOpciones().add(generoIndiferente);
    piso8.getOpciones().add(fumadores);
    piso8.getOpciones().add(mascotas);
    piso8.getOpciones().add(terraza);
    piso8.setUsuario(pepe);
    pisoDAO.create(piso8);

    Habitacion habitacion12 = new Habitacion("Habitación en la Estrada de Mesoiro, 20","Se alquila habitación en un pequeño piso de la Estrada de Mesoiro. ",
      15,LocalDate.now(),LocalDate.now(),finDeContratohabitacion2,150,0,true,0,pisoDAO.findById(piso8.getId()));
    habitacion12.getOpciones().add(amueblada);
    habitacion12.getOpciones().add(banoP);

    habitacionDao.create(habitacion12);



    File imagenes = new File(properties.getImagesPath());


    String[] pathNames = imagenes.list();

    Imagen imagen1 = new Imagen(pathNames[0],"imagen"+pathNames[0]);
    imagen1.setHabitacion(habitacion1);
    Imagen imagen2 = new Imagen(pathNames[1],"imagen"+pathNames[1]);
    imagen2.setHabitacion(habitacion1);
    Imagen imagen3 = new Imagen(pathNames[2],"imagen"+pathNames[2]);
    imagen3.setHabitacion(habitacion2);
    Imagen imagen4 = new Imagen(pathNames[3],"imagen"+pathNames[3]);
    imagen4.setHabitacion(habitacion1);
    Imagen imagen5 = new Imagen(pathNames[4],"imagen"+pathNames[4]);
    imagen5.setHabitacion(habitacion2);
    Imagen imagen6 = new Imagen(pathNames[5],"imagen"+pathNames[5]);
    imagen6.setHabitacion(habitacion3);
    Imagen imagen7 = new Imagen(pathNames[6],"imagen"+pathNames[6]);
    imagen7.setHabitacion(habitacion3);
    Imagen imagen8 = new Imagen(pathNames[7],"imagen"+pathNames[7]);
    imagen8.setHabitacion(habitacion5);
    Imagen imagen9 = new Imagen(pathNames[8],"imagen"+pathNames[8]);
    imagen9.setHabitacion(habitacion5);
    Imagen imagen10 = new Imagen(pathNames[9],"imagen"+pathNames[9]);
    imagen10.setHabitacion(habitacion6);
    Imagen imagen11 = new Imagen(pathNames[10],"imagen"+pathNames[10]);
    imagen11.setHabitacion(habitacion6);
    Imagen imagen12 = new Imagen(pathNames[11],"imagen"+pathNames[11]);
    imagen12.setHabitacion(habitacion7);
    Imagen imagen13 = new Imagen(pathNames[12],"imagen"+pathNames[12]);
    imagen13.setHabitacion(habitacion7);
    Imagen imagen14 = new Imagen(pathNames[13],"imagen"+pathNames[13]);
    imagen14.setHabitacion(habitacion8);
    Imagen imagen15 = new Imagen(pathNames[14],"imagen"+pathNames[14]);
    imagen15.setHabitacion(habitacion8);
    Imagen imagen16 = new Imagen(pathNames[15],"imagen"+pathNames[15]);
    imagen16.setHabitacion(habitacion9);
    Imagen imagen17 = new Imagen(pathNames[16],"imagen"+pathNames[16]);
    imagen17.setHabitacion(habitacion9);
    Imagen imagen18 = new Imagen(pathNames[17],"imagen"+pathNames[17]);
    imagen18.setHabitacion(habitacion10);
    Imagen imagen19 = new Imagen(pathNames[18],"imagen"+pathNames[18]);
    imagen19.setHabitacion(habitacion10);
    Imagen imagen20 = new Imagen(pathNames[19],"imagen"+pathNames[19]);
    imagen20.setHabitacion(habitacion11);
    Imagen imagen21 = new Imagen(pathNames[20],"imagen"+pathNames[20]);
    imagen21.setHabitacion(habitacion11);
    Imagen imagen22 = new Imagen(pathNames[21],"imagen"+pathNames[21]);
    imagen22.setHabitacion(habitacion12);
    Imagen imagen23 = new Imagen(pathNames[22],"imagen"+pathNames[22]);
    imagen23.setHabitacion(habitacion12);


    imagen1.setPortada(true);
    imagenDao.create(imagen1);
    imagenDao.create(imagen2);
    imagen3.setPortada(true);
    imagenDao.create(imagen3);
    imagenDao.create(imagen4);
    imagenDao.create(imagen5);
    imagen6.setPortada(true);
    imagenDao.create(imagen6);
    imagenDao.create(imagen7);
    imagen8.setPortada(true);
    imagenDao.create(imagen8);
    imagenDao.create(imagen9);
    imagen10.setPortada(true);
    imagenDao.create(imagen10);
    imagenDao.create(imagen11);
    imagen12.setPortada(true);
    imagenDao.create(imagen12);
    imagenDao.create(imagen13);
    imagen14.setPortada(true);
    imagenDao.create(imagen14);
    imagenDao.create(imagen15);
    imagen16.setPortada(true);
    imagenDao.create(imagen16);
    imagenDao.create(imagen17);
    imagen18.setPortada(true);
    imagenDao.create(imagen18);
    imagenDao.create(imagen19);
    imagen20.setPortada(true);
    imagenDao.create(imagen20);
    imagenDao.create(imagen21);
    imagen22.setPortada(true);
    imagenDao.create(imagen22);
    imagenDao.create(imagen23);

    habitacion1.getImagenes().add(imagen1);
    habitacion1.getImagenes().add(imagen2);
    habitacion1.getImagenes().add(imagen4);
    habitacionDao.update(habitacion1);

    habitacion2.getImagenes().add(imagen3);
    habitacion2.getImagenes().add(imagen5);
    habitacionDao.update(habitacion2);

    habitacion3.getImagenes().add(imagen6);
    habitacion3.getImagenes().add(imagen7);

    habitacionDao.update(habitacion3);

    habitacion5.getImagenes().add(imagen8);
    habitacion5.getImagenes().add(imagen9);

    habitacionDao.update(habitacion5);

    habitacion6.getImagenes().add(imagen10);
    habitacion6.getImagenes().add(imagen11);

    habitacionDao.update(habitacion6);

    habitacion7.getImagenes().add(imagen12);
    habitacion7.getImagenes().add(imagen13);

    habitacionDao.update(habitacion7);

    habitacion8.getImagenes().add(imagen14);
    habitacion8.getImagenes().add(imagen15);

    habitacionDao.update(habitacion8);

    habitacion9.getImagenes().add(imagen16);
    habitacion9.getImagenes().add(imagen17);

    habitacionDao.update(habitacion9);

    habitacion10.getImagenes().add(imagen18);
    habitacion10.getImagenes().add(imagen19);

    habitacionDao.update(habitacion10);

    habitacion11.getImagenes().add(imagen20);
    habitacion11.getImagenes().add(imagen21);

    habitacionDao.update(habitacion11);

    habitacion12.getImagenes().add(imagen22);
    habitacion12.getImagenes().add(imagen23);

    habitacionDao.update(habitacion12);

  }
}
