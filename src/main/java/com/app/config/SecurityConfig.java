package com.app.config;

import com.app.service.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AuthSuccessHandler authSuccessHandler;
    private final SecurityService securityService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeRequests()
                .antMatchers("/users/**").hasAnyAuthority("Root User", "Admin")
                .antMatchers("/companies/**").hasAnyAuthority("Root User")
                .antMatchers("/", "/login", "/fragments/**", "/assets/**","/img/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(authSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                .tokenValiditySeconds(86400)
                .userDetailsService(securityService)
                .and()
                .build();
    }

}
