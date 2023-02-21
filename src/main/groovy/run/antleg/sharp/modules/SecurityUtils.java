package run.antleg.sharp.modules;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import run.antleg.sharp.modules.errors.AppException;
import run.antleg.sharp.modules.errors.Errors;
import run.antleg.sharp.modules.user.security.MyUserDetails;

public final class SecurityUtils {

    public static MyUserDetails getPrincipal() {
        var userDetails = (MyUserDetails) getAuthentication().getPrincipal();
        if (userDetails == null) {
            throw new AppException(Errors.REQUEST_UNAUTHORIZED);
        }
        return userDetails;
    }

    public static Authentication getAuthentication() {
        var authentication =  SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication == null) {
            throw new AppException(Errors.REQUEST_UNAUTHORIZED);
        }
        return authentication;
    }
}
