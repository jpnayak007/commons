package io.kernel.core.idrepo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * To expose the documentation for entire API
 * 
 * @author Manoj SP
 */
@Configuration(value = "ida_swagger_config")
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * 
	 * Docket bean provides more control over the API for Documentation Generation
	 * 
	 */

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("io.kernel.core.idrepo.controller"))
				.paths(PathSelectors.regex("(?!/(error|actuator).*).*")).build();
	}

}
