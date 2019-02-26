package keycloak.jsample.spring;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

import keycloak.jsample.util.AppConstants;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@PreAuthorize("hasRole('ROLE_" + AppConstants.Role.USER + "') or hasRole('ROLE_" + AppConstants.Role.ADMIN + "')")
public @interface AllAccess {

}
