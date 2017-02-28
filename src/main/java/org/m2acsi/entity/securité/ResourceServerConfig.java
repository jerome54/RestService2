package org.m2acsi.entity.securité;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfig {
    @Configuration
    @EnableResourceServer
    protected static class ServerConfig extends ResourceServerConfigurerAdapter {
        
        
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST,"/citoyen/demandes").permitAll()
                    .antMatchers(HttpMethod.GET,"/citoyen/demandes").permitAll()
                    .antMatchers(HttpMethod.PUT,"/citoyen/demandes").permitAll()
                    .antMatchers(HttpMethod.GET,"/users/**").authenticated()
                    .antMatchers(HttpMethod.GET,"/users/demandes/liste").authenticated()
                    .antMatchers(HttpMethod.POST,"/users/demandes/{demandeId}").authenticated()
            		.antMatchers(HttpMethod.GET,"/users/intervenants").hasAuthority("IntervenantInterne")
            		.antMatchers(HttpMethod.POST,"/users/{demandeId}").hasAuthority("Administration clerk")
            		.antMatchers(HttpMethod.POST,"/users/{demandeId}").hasAuthority("Administration Manager")
            		.antMatchers(HttpMethod.GET, "/{demandeId}/{actionId}").authenticated()
            		.antMatchers(HttpMethod.DELETE, "/{demandeId}").hasAuthority("Administration clerk");
        }
    }
    
}
