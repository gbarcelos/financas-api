package br.com.oak.financas.api.exception;

import br.com.oak.financas.api.model.ErrorCode;

public class BusinessException extends AbstractException {

  public BusinessException(ErrorCode errorCode, String friendlyMessage) {
    super(errorCode, friendlyMessage);
  }
}
