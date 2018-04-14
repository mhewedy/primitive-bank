package primitivebank.error.support;

import lombok.Value;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import primitivebank.error.BankException;

@Value
public class ErrorInfo {

    private Integer id;
    private String key;

    public static ErrorInfo from(MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult().getFieldError();

        String code = fieldError.getCode();
        String field = fieldError.getField();
        String target = fieldError.getObjectName();

        return new ErrorInfo(ErrorId.MethodArgumentNotValidException,
                String.format("input.invalid.%s.%s.%s", target, field, code));
    }

    public static ErrorInfo from(BankException ex) {
        return new ErrorInfo(ex.getId(), ex.getMessage());
    }
}
