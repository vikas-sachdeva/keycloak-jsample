package keycloak.jsample.spring;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import keycloak.jsample.util.AppConstants;

@KeycloakConfiguration
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);

		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers("/admin/**").hasRole(AppConstants.Role.ADMIN).antMatchers("/*")
				.hasAnyRole(AppConstants.Role.USER, AppConstants.Role.ADMIN).antMatchers("/*/*")
				.hasAnyRole(AppConstants.Role.USER, AppConstants.Role.ADMIN).anyRequest().denyAll();

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
		SimpleAuthorityMapper simpleAuthorityMapper = new SimpleAuthorityMapper();
		keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(simpleAuthorityMapper);
		auth.authenticationProvider(keycloakAuthenticationProvider);
	}

	@Override
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Bean
	public FilterRegistrationBean<KeycloakAuthenticationProcessingFilter> keycloakAuthenticationProcessingFilterRegistrationBean(
			KeycloakAuthenticationProcessingFilter filter) {
		FilterRegistrationBean<KeycloakAuthenticationProcessingFilter> registrationBean = new FilterRegistrationBean<>(
				filter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<KeycloakPreAuthActionsFilter> keycloakPreAuthActionsFilterRegistrationBean(
			KeycloakPreAuthActionsFilter filter) {
		FilterRegistrationBean<KeycloakPreAuthActionsFilter> registrationBean = new FilterRegistrationBean<>(filter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<KeycloakAuthenticatedActionsFilter> keycloakAuthenticatedActionsFilterBean(
			KeycloakAuthenticatedActionsFilter filter) {
		FilterRegistrationBean<KeycloakAuthenticatedActionsFilter> registrationBean = new FilterRegistrationBean<>(
				filter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<KeycloakSecurityContextRequestFilter> keycloakSecurityContextRequestFilterBean(
			KeycloakSecurityContextRequestFilter filter) {
		FilterRegistrationBean<KeycloakSecurityContextRequestFilter> registrationBean = new FilterRegistrationBean<>(
				filter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}

	@Bean
	@Override
	@ConditionalOnMissingBean(HttpSessionManager.class)
	protected HttpSessionManager httpSessionManager() {
		return new HttpSessionManager();
	}

	@Bean
	public KeycloakConfigResolver keycloakConfigResolver() {
		return new KeycloakSpringBootConfigResolver();
	}
}
