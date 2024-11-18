package com.app.config;

import com.app.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    private final AuthSuccessHandler authSuccessHandler;
    private final SecurityService securityService;

    public SecurityConfig(@Lazy SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.authSuccessHandler = authSuccessHandler;
        this.securityService = securityService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/users/**").hasAnyAuthority("Root User", "Admin")
                .antMatchers("/companies/**").hasAnyAuthority("Root User")
                .antMatchers("/", "/login", "/fragments/**", "/assets/**", "/images/**").permitAll()
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
