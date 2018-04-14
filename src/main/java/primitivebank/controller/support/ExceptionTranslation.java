package primitivebank.controller.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import primitivebank.error.ClientException;
import primitivebank.error.support.ErrorInfo;
import primitivebank.error.ServerException;

@Slf4j
@RestControllerAdvice
public class ExceptionTranslation {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ClientException.class)
    public ErrorInfo handleException(ClientException ex) {
        log.debug("client error: {}", ex.getMessage());
        return ErrorInfo.from(ex);
    }

    @ResponseBody
    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorInfo handleException(ServerException ex) {
        log.error("server error: {}", ex.getMessage(), ex);
        return ErrorInfo.from(ex);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo handleException(MethodArgumentNotValidException ex) {
        log.debug("client error: {}", ex.getMessage());
        return ErrorInfo.from(ex);
    }
}
