package br.com.oak.financas.api.exception;

import br.com.oak.financas.api.model.ErrorCode;

public class NotFoundException extends AbstractException {

  public NotFoundException(ErrorCode errorCode, String friendlyMessage) {
    super(errorCode, friendlyMessage);
  }
}
