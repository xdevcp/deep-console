package cc.devcp.project.console.exception;

import cc.devcp.project.core.auth.AccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handler for console module
 *
 * @author nkorange
 * @since 1.2.0
 */
@ControllerAdvice
public class ConsoleExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleExceptionHandler.class);

    @ExceptionHandler(AccessException.class)
    private ResponseEntity<String> handleAccessException(AccessException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getErrMsg());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<String> handleException(Exception e) {
        logger.error("CONSOLE", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
    }
}
