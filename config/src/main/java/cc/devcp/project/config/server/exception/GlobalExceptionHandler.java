package cc.devcp.project.config.server.exception;

import cc.devcp.project.common.exception.GlobalException;
import cc.devcp.project.config.server.monitor.MetricsMonitor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * global exception handler
 *
 * @author Nacos
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * For IllegalArgumentException, we are returning void with status code as 400, so our error-page will be used in
     * this case.
     *
     * @throws IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(HttpServletResponse response, Exception ex) throws IOException {
        MetricsMonitor.getIllegalArgumentException().increment();
        response.setStatus(400);
        if (ex.getMessage() != null) {
            response.getWriter().println(ex.getMessage());
        } else {
            response.getWriter().println("invalid param");
        }
    }

    /**
     * For GlobalException
     *
     * @throws GlobalException
     */
    @ExceptionHandler(GlobalException.class)
    public void handleNacosException(HttpServletResponse response, GlobalException ex) throws IOException {
        MetricsMonitor.getNacosException().increment();
        response.setStatus(ex.getErrCode());
        if (ex.getErrMsg() != null) {
            response.getWriter().println(ex.getErrMsg());
        } else {
            response.getWriter().println("unknown exception");
        }
    }

    /**
     * For DataAccessException
     *
     * @throws DataAccessException
     */
    @ExceptionHandler(DataAccessException.class)
    public void handleDataAccessException(HttpServletResponse response, DataAccessException ex) throws DataAccessException {
        MetricsMonitor.getDbException().increment();
        throw new CannotGetJdbcConnectionException(ex.getMessage());
    }

}
