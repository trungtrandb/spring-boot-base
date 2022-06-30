package site.code4fun.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import site.code4fun.dto.ResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    /**
     * IndexOutOfBoundsException sẽ được xử lý riêng tại đây
     */
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseDTO handleIndexOutOfBoundsException(Exception ex) {
        return new ResponseDTO(ResponseDTO.Type.danger, "Error", 400, ex.getMessage(), null);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseDTO handleNotFoundException(NotFoundException ex) {
        return new ResponseDTO(ResponseDTO.Type.danger, "Not found", 404, ex.getMessage(), null);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, String> errorMess = fieldErrors.stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return new ResponseDTO(ResponseDTO.Type.danger, "Validation", 404, errorMess.toString(), null);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseDTO handleValidationException(ValidationException ex) {
        return new ResponseDTO(ResponseDTO.Type.danger, "Validation", 404, ex.getMessage(), null);
    }


    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseDTO handleDuplicateException(DuplicateException ex) {
        return new ResponseDTO(ResponseDTO.Type.danger, "Duplicate", 404, ex.getMessage(), null);
    }

    /**
     * Tất cả các Exception không được khai báo sẽ được xử lý tại đây
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseDTO handleAllException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseDTO(null, "Error", null, ex.getMessage(), null);
    }
}
