package run.antleg.sharp.config.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;

@Aspect
@Component
public class AuthenticatedAspect {

    @Around("@annotation(run.antleg.sharp.config.security.Authenticated)")
    public Object authenticated(ProceedingJoinPoint joinPoint) throws Throwable {
        var authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(Errors.REQUEST_UNAUTHORIZED);
        }
        return joinPoint.proceed();
    }

}
