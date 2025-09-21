package es.udc.tfg.server.model.exception;

public class UserEmailExistsException extends ModelException {
  public UserEmailExistsException(String email) {
    super("User email " + email + " already exists");
  }
}
