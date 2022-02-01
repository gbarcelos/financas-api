package br.com.oak.financas.api.exception;

import br.com.oak.financas.api.model.ErrorCode;
import br.com.oak.financas.api.model.contract.response.ErrorContractResponse;
import br.com.oak.financas.api.model.contract.response.ErrorResponse;
import br.com.oak.financas.api.model.contract.response.ObjectError;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.DateTimeException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String MSG_ERRO_GENERICA_USUARIO_FINAL =
      "Ocorreu um erro inesperado no sistema. Tente novamente e se "
          + "o problema persistir, entre em contato com o administrador do sistema.";

  @Autowired private MessageSource messageSource;

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    return ResponseEntity.status(status).headers(headers).build();
  }

  @Override
  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    return handleValidationInternal(
        ex, headers, status, request, ex.getBindingResult(), ErrorCode.INVALID_DATA);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    ErrorCode errorCode = ErrorCode.INVALID_DATA;

    if (ex.getMessage().contains("NotNull") || ex.getMessage().contains("NotBlank")) {
      errorCode = ErrorCode.REQUIRED_FIELD;
    }

    return handleValidationInternal(ex, headers, status, request, ex.getBindingResult(), errorCode);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    Throwable rootCause = ExceptionUtils.getRootCause(ex);

    if (rootCause instanceof InvalidFormatException) {
      return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
    }

    if (rootCause instanceof DateTimeException) {
      return handleDateTimeException((DateTimeException) rootCause, headers, status, request);
    }

    if (rootCause instanceof PropertyBindingException) {
      return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
    }

    ServletWebRequest servletWebRequest = (ServletWebRequest) request;
    ErrorCode errorCode = ErrorCode.INVALID_DATA;
    String detail = "O corpo da requisição está inválido.";

    ErrorContractResponse errorContractResponse =
        ErrorContractResponse.builder()
            .error(createErrorResponseBuilder(status, errorCode, detail).build())
            .path(servletWebRequest.getRequest().getServletPath())
            .build();

    return handleExceptionInternal(ex, errorContractResponse, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    ServletWebRequest servletWebRequest = (ServletWebRequest) request;
    String detail =
        String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());

    ErrorContractResponse errorContractResponse =
        ErrorContractResponse.builder()
            .error(createErrorResponseBuilder(status, ErrorCode.RESOURCE_NOT_FOUND, detail).build())
            .path(servletWebRequest.getRequest().getServletPath())
            .build();

    return handleExceptionInternal(ex, errorContractResponse, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    if (ex instanceof MethodArgumentTypeMismatchException) {
      return handleMethodArgumentTypeMismatch(
          (MethodArgumentTypeMismatchException) ex, headers, status, request);
    }

    return super.handleTypeMismatch(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

    if (body == null || body instanceof String) {
      Throwable rootCause = ExceptionUtils.getRootCause(ex);
      ServletWebRequest servletWebRequest = (ServletWebRequest) request;

      body =
          ErrorContractResponse.builder()
              .error(
                  ErrorResponse.builder()
                      .status(status.value())
                      .type(status.name())
                      .errorCode(status.name())
                      .detail(rootCause.getMessage())
                      .build())
              .path(servletWebRequest.getRequest().getServletPath())
              .build();
    }

    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    ServletWebRequest servletWebRequest = (ServletWebRequest) request;

    String detail =
        String.format(
            "O parâmetro de URL '%s' recebeu o valor '%s', "
                + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

    ErrorContractResponse errorContractResponse =
        ErrorContractResponse.builder()
            .error(createErrorResponseBuilder(status, ErrorCode.INVALID_PARAMETER, detail).build())
            .path(servletWebRequest.getRequest().getServletPath())
            .build();

    return handleExceptionInternal(ex, errorContractResponse, headers, status, request);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {

    ServletWebRequest servletWebRequest = (ServletWebRequest) request;

    HttpStatus status = HttpStatus.BAD_REQUEST;

    ErrorContractResponse errorContractResponse =
        ErrorContractResponse.builder()
            .error(
                createErrorResponseBuilder(status, ex.getErrorCode(), ex.getFriendlyMessage())
                    .build())
            .path(servletWebRequest.getRequest().getServletPath())
            .build();

    return handleExceptionInternal(ex, errorContractResponse, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleNotFound(NotFoundException ex, WebRequest request) {

    ServletWebRequest servletWebRequest = (ServletWebRequest) request;

    HttpStatus status = HttpStatus.NOT_FOUND;

    ErrorContractResponse errorContractResponse =
        ErrorContractResponse.builder()
            .error(
                createErrorResponseBuilder(status, ex.getErrorCode(), ex.getFriendlyMessage())
                    .build())
            .path(servletWebRequest.getRequest().getServletPath())
            .build();

    return handleExceptionInternal(ex, errorContractResponse, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {

    ServletWebRequest servletWebRequest = (ServletWebRequest) request;

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    ex.printStackTrace();

    ErrorContractResponse errorContractResponse =
        ErrorContractResponse.builder()
            .error(
                createErrorResponseBuilder(
                        status, ErrorCode.INTERNAL_SERVER_ERROR, MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build())
            .path(servletWebRequest.getRequest().getServletPath())
            .build();

    return handleExceptionInternal(ex, errorContractResponse, new HttpHeaders(), status, request);
  }

  private ResponseEntity<Object> handleValidationInternal(
      Exception ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request,
      BindingResult bindingResult,
      ErrorCode errorCode) {

    String detail = "Um ou mais campos estão inválidos.";

    List<ObjectError> errors =
        bindingResult.getAllErrors().stream()
            .map(
                objectError -> {
                  String message =
                      messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                  String name = objectError.getObjectName();

                  if (objectError instanceof FieldError) {
                    name = ((FieldError) objectError).getField();
                  }

                  return ObjectError.builder().name(name).userMessage(message).build();
                })
            .collect(Collectors.toList());

    ServletWebRequest servletWebRequest = (ServletWebRequest) request;

    ErrorResponse responseError =
        createErrorResponseBuilder(status, errorCode, detail).objectErrors(errors).build();

    ErrorContractResponse errorContractResponse =
        ErrorContractResponse.builder()
            .error(responseError)
            .path(servletWebRequest.getRequest().getServletPath())
            .build();

    return handleExceptionInternal(ex, errorContractResponse, headers, status, request);
  }

  private ResponseEntity<Object> handleInvalidFormat(
      InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    ServletWebRequest servletWebRequest = (ServletWebRequest) request;
    String path = joinPath(ex.getPath());

    String detail =
        String.format(
            "A propriedade '%s' recebeu o valor '%s', "
                + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
            path, ex.getValue(), ex.getTargetType().getSimpleName());

    ErrorContractResponse errorContractResponse =
        ErrorContractResponse.builder()
            .error(createErrorResponseBuilder(status, ErrorCode.INVALID_FORMAT, detail).build())
            .path(servletWebRequest.getRequest().getServletPath())
            .build();

    return handleExceptionInternal(ex, errorContractResponse, headers, status, request);
  }

  private ResponseEntity<Object> handleDateTimeException(
      DateTimeException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    ServletWebRequest servletWebRequest = (ServletWebRequest) request;

    ErrorContractResponse errorContractResponse =
        ErrorContractResponse.builder()
            .error(
                createErrorResponseBuilder(status, ErrorCode.INVALID_FORMAT, ex.getMessage())
                    .build())
            .path(servletWebRequest.getRequest().getServletPath())
            .build();

    return handleExceptionInternal(ex, errorContractResponse, headers, status, request);
  }

  private ResponseEntity<Object> handlePropertyBinding(
      PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    ServletWebRequest servletWebRequest = (ServletWebRequest) request;
    String path = joinPath(ex.getPath());

    ErrorCode problemType = ErrorCode.INVALID_DATA;
    String detail = String.format("A propriedade '%s' não existe.", path);

    ErrorContractResponse errorContractResponse =
        ErrorContractResponse.builder()
            .error(createErrorResponseBuilder(status, problemType, detail).build())
            .path(servletWebRequest.getRequest().getServletPath())
            .build();

    return handleExceptionInternal(ex, errorContractResponse, headers, status, request);
  }

  private ErrorResponse.ErrorResponseBuilder createErrorResponseBuilder(
      HttpStatus status, ErrorCode errorCode, String detail) {
    return ErrorResponse.builder()
        .status(status.value())
        .type(status.name())
        .errorCode(errorCode.name())
        .detail(detail);
  }

  private String joinPath(List<JsonMappingException.Reference> references) {
    return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
  }
}
