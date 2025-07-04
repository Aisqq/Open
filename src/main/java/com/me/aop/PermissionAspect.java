package com.me.aop;

import com.me.annotation.Role;
import com.me.utils.UserHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Aspect
@Component
public class PermissionAspect {

    @Pointcut("@within(com.me.annotation.Role) || @annotation(com.me.annotation.Role)")
    public void permissionCheckPointcut() {}
    @Before("permissionCheckPointcut()")
    public void checkPermission(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Role role = method.getAnnotation(Role.class);
        if (role == null) {
            Class<?> targetClass = joinPoint.getTarget().getClass();
            role = targetClass.getAnnotation(Role.class);
        }
        String permission = role.value();
        if (!hasPermission(permission)) {
            throw new SecurityException("权限不足，无法执行该操作：" + permission);
        }
    }

    private boolean hasPermission(String permission) {
        String currentUserPermission = UserHolder.getUser().getRole()==0?"user":"admin";
        return currentUserPermission.equals(permission);
    }
}
