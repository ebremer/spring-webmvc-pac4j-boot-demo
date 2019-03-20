package org.pac4j.demo.spring;

import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.config.Config;
import org.pac4j.springframework.annotation.AnnotationConfig;
import org.pac4j.springframework.component.ComponentConfig;
import org.pac4j.springframework.web.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Import({ComponentConfig.class, AnnotationConfig.class})
@ComponentScan(basePackages = "org.pac4j.springframework.web")
public class SecurityConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Config config;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor(config, "FacebookClient")).addPathPatterns("/facebook/*").excludePathPatterns("/facebook/notprotected.html");
        registry.addInterceptor(
                new SecurityInterceptor(
                        config,
                        "FacebookClient",
                        new Authorizer[] { new RequireAnyRoleAuthorizer("ROLE_ADMIN") }
                )
        ).addPathPatterns("/facebookadmin/*");
        registry.addInterceptor(new SecurityInterceptor(config, "FacebookClient")).addPathPatterns("/facebookadmin/*");
        registry.addInterceptor(
                new SecurityInterceptor(
                        config,
                        "FacebookClient",
                        new Authorizer[] { new CustomAuthorizer() }
                        )
        ).addPathPatterns("/facebookcustom/*");
        registry.addInterceptor(new SecurityInterceptor(config, "TwitterClient,FacebookClient")).addPathPatterns("/twitter/*");
        registry.addInterceptor(new SecurityInterceptor(config, "FormClient")).addPathPatterns("/form/*");
        registry.addInterceptor(new SecurityInterceptor(config, "IndirectBasicAuthClient")).addPathPatterns("/basicauth/*");
        registry.addInterceptor(new SecurityInterceptor(config, "CasClient")).addPathPatterns("/cas/*");
        registry.addInterceptor(new SecurityInterceptor(config, "SAML2Client")).addPathPatterns("/saml/*");
        registry.addInterceptor(new SecurityInterceptor(config, "GoogleOidcClient")).addPathPatterns("/oidc/*");
        registry.addInterceptor(new SecurityInterceptor(config)).addPathPatterns("/protected/*");
        registry.addInterceptor(new SecurityInterceptor(config, "DirectBasicAuthClient,ParameterClient")).addPathPatterns("/dba/*");
        registry.addInterceptor(new SecurityInterceptor(config, "ParameterClient")).addPathPatterns("/rest-jwt/*");
    }
}
