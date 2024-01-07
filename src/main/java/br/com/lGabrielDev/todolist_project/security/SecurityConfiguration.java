package br.com.lGabrielDev.todolist_project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    //endpoint permissions
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return
            http
                .httpBasic(Customizer.withDefaults())   
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((auth) ->  { 
                    auth
                    //person controller
                    .requestMatchers(HttpMethod.POST, "/v1/api/person").permitAll() //create a person/user
                    //swagger
                    .requestMatchers(this.swaggerEndPoints()).permitAll()
                    
                    .requestMatchers(HttpMethod.GET, "/v1/api/person").hasAuthority("ADMIN") //read all persons
                    .requestMatchers(HttpMethod.PUT, "/v1/api/person/give-admin-permission/{id}").hasAuthority("ADMIN") //give admin permission
                    .anyRequest().hasAuthority("REGULAR_USER");
                })
                .build();
    }

    //PasswordEncounder Bean
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //swagger end points - permit all
    public String[] swaggerEndPoints(){
        String[] endPoints = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
        };
        return endPoints;
    }
}