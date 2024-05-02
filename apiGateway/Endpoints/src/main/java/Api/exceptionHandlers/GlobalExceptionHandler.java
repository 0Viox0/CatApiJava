package Api.exceptionHandlers;

// import Viox.customExceptions.CatNotFoundException;
// import Viox.customExceptions.FriendsWithSelfException;
// import Viox.customExceptions.InvalidCatColorException;
// import Viox.customExceptions.UserNotFoundException;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // @ExceptionHandler({UserNotFoundException.class, CatNotFoundException.class})
    // public ResponseEntity<Object> handleUserNotFound(
    // RuntimeException ex,
    // WebRequest request
    // ) {
    // String message = ex.getMessage();
    //
    // return handleExceptionInternal(
    // ex,
    // message,
    // new HttpHeaders(),
    // HttpStatus.NOT_FOUND,
    // request
    // );
    // }
    //
    // @ExceptionHandler({FriendsWithSelfException.class,
    // InvalidCatColorException.class})
    // public ResponseEntity<Object> handleBadRequest(
    // RuntimeException ex,
    // WebRequest request
    // ) {
    // String message = ex.getMessage();
    //
    // return handleExceptionInternal(
    // ex,
    // message,
    // new HttpHeaders(),
    // HttpStatus.BAD_REQUEST,
    // request
    // );
    // }
}
