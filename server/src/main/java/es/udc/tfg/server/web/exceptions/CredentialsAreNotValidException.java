package es.udc.tfg.server.web.exceptions;

public class CredentialsAreNotValidException extends ResourceException {

  public CredentialsAreNotValidException(String errorMsg) {
    super(errorMsg);
  }
}
