package keycloak.jsample.spring;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${keycloak.auth-server-url}")
	private String authServer;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${public.keycloak.resource}")
	private String clientId;

	private static final String APP_NAME = "swagger2-spring-security";

	private static final String OAUTH_NAME = "Swagger-UI";

	@Bean
	public Docket api() {

		/*
		 * Swagger UI can be access on -
		 * 
		 * http://localhost:8080/swagger-ui.html
		 */

		ApiInfo apiInfo = new ApiInfo("REST JSON jsample Api Documentation", "REST JSON jsample", "1.0", "", null, "",
				"", Collections.emptyList());

		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any())
				.build().securitySchemes(Arrays.asList(securityScheme()))
				.securityContexts(Arrays.asList(securityContext()));
	}
	
	 @Bean
	 public SecurityConfiguration security() {
	   return SecurityConfigurationBuilder.builder()
	    .realm(realm)
	    .clientId(clientId)
	    .appName(APP_NAME)
	    .scopeSeparator(" ")
	    .build();
	 }

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(Arrays.asList(new SecurityReference(OAUTH_NAME, scopes())))
				.forPaths(PathSelectors.any()).build();
	}

	private SecurityScheme securityScheme() {
		GrantType grantType = new AuthorizationCodeGrantBuilder()
				.tokenEndpoint(new TokenEndpoint(authServer + "/realms/" + realm + "/protocol/openid-connect/token",
						APP_NAME))
				.tokenRequestEndpoint(new TokenRequestEndpoint(
						authServer + "/realms/" + realm + "/protocol/openid-connect/auth", clientId, ""))
				.build();

		SecurityScheme oauth = new OAuthBuilder().name(OAUTH_NAME).grantTypes(Arrays.asList(grantType))
				.scopes(Arrays.asList(scopes())).build();
		return oauth;
	}

	private AuthorizationScope[] scopes() {
		AuthorizationScope[] scopes = { };
		return scopes;
	}
}
