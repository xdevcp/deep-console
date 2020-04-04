package cc.devcp.project.common.exception;

import cc.devcp.project.common.model.result.ResEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * <pre>
 *     description:
 * </pre>
 *
 * @author deep.wu
 * @version 2020-04-04
 */
@Slf4j
@RestControllerAdvice
public class CommExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResEntity handleException(MethodArgumentNotValidException e, HttpServletResponse response){
        if(log.isDebugEnabled()){
            e.printStackTrace();
        }
        log.error(ExceptionUtil.getMessageWithStack(e));
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResEntity.fail(e.getBindingResult());
    }

    @ExceptionHandler(CommException.class)
    public ResEntity handleException(CommException e, HttpServletResponse response){
        if(log.isDebugEnabled()){
            e.printStackTrace();
        }
        log.error(ExceptionUtil.getMessageWithStack(e));
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResEntity.fail(e);
    }

    @ExceptionHandler(CommRuntimeException.class)
    public ResEntity handleException(CommRuntimeException e, HttpServletResponse response){
        if(log.isDebugEnabled()){
            e.printStackTrace();
        }
        log.error(ExceptionUtil.getMessageWithStack(e));
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResEntity.fail(e);
    }

    @ExceptionHandler(SQLException.class)
    public ResEntity handleException(SQLException e, HttpServletResponse response){
        if(log.isDebugEnabled()){
            e.printStackTrace();
        }
        log.error(ExceptionUtil.getMessageWithStack(e));
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResEntity.fail(50000, "数据库错误" + ExceptionUtil.getMessage(e));
    }

    @ExceptionHandler(Exception.class)
    public ResEntity handleException(Exception e, HttpServletResponse response){
        if(log.isDebugEnabled()){
            e.printStackTrace();
        }
        log.error(ExceptionUtil.getMessageWithStack(e));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResEntity.fail(50000, ExceptionUtil.getMessage(e));
    }

}
