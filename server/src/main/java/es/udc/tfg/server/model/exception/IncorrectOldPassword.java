package es.udc.tfg.server.model.exception;

public class IncorrectOldPassword extends ModelException{

  public IncorrectOldPassword() {
    super("Contraseña actual incorrecta");
  }
}
