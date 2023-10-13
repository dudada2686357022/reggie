package top.dudada.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * AOP思想，全局异常捕获类
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {


//  @ExceptionHandler 是Spring框架提供的注解之一，
//  用于在控制器(Controller)中处理异常。通过将这个注解添加到方法上，
//  你可以指定要处理的异常类型，并在该方法中编写异常处理逻辑。
    /**
     * SQLIntegrityConstraintViolationException.class：这是一个异常类的引用，
     * 表示了一个特定类型的异常，即SQLIntegrityConstraintViolationException异常。
     * 这种异常通常在数据库操作中出现，当违反了数据库的完整性约束时抛出。
     * 例如，如果试图插入一条重复的记录或者违反了外键约束，就可能会引发此异常。
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
//        打印错误日志
        log.error(ex.getMessage());


        if (ex.getMessage().contains("Duplicate entry")){
//            将错误信息拆分成一个字符数组，以空格隔开
            String[] split = ex.getMessage().split(" ");
            return R.error(split[2]+"已存在");

        }


        return R.error("未知错误");
    }


    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
//        打印错误日志
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }


}
