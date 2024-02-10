package com.sky.aspect;

/**
 * @author Mark
 * @date 2024/2/10
 */

import com.sky.anotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，填充公共字段，减少重复代码
 */
@Aspect
@Component
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.anotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 前置通知
     * @param point
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint point){
        // 方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 获取方法上的注解对象
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        // 获取数据库操作类型
        OperationType type = annotation.value();
        // 实体对象
        Object[] args = point.getArgs();
        if (args == null || args.length == 0){
            return;
        }
        Object arg = args[0];

        LocalDateTime localDateTime = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        if (type == OperationType.INSERT){
            try {
                Method createTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method createUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method updateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method updateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                createTime.invoke(arg, localDateTime);
                createUser.invoke(arg, currentId);
                updateTime.invoke(arg, localDateTime);
                updateUser.invoke(arg, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (type == OperationType.UPDATE) {
            try {
                Method updateTime = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method updateUser = arg.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                updateTime.invoke(arg, localDateTime);
                updateUser.invoke(arg, currentId);
            }catch (Exception exception){
                throw new RuntimeException(exception);
            }
        }
    }
}
