package es.udc.tfg.server;

import javax.annotation.PostConstruct;

import es.udc.tfg.server.model.exception.UserLoginExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

import es.udc.tfg.server.config.DatabaseLoader;

import java.io.IOException;

@SpringBootApplication
public class RestExampleApplication {
  private final Logger logger = LoggerFactory.getLogger(RestExampleApplication.class);

  @Autowired
  @Lazy
  private DatabaseLoader databaseLoader;

  public static void main(String[] args) {
    SpringApplication.run(RestExampleApplication.class, args);
  }

  @PostConstruct
  public void init() throws InterruptedException {
    try {
      databaseLoader.loadData();
    } catch (UserLoginExistsException e) {
      logger.error(e.getMessage(), e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
