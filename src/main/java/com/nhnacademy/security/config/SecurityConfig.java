package com.nhnacademy.security.config;

import com.nhnacademy.security.auth.LoginSuccessHandler;
import com.nhnacademy.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/private-project/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MEMBER")
                    .antMatchers("/project/**").authenticated()
                    .antMatchers("/redirect-index").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .requiresChannel()
                    .antMatchers("/admin/**").requiresSecure()
                    .antMatchers("/private-project/**").requiresSecure()
                    .antMatchers("/project/**").requiresSecure()
                    .anyRequest().requiresInsecure()
                    .and()
                .formLogin()
                    .usernameParameter("id")
                    .passwordParameter("pwd")
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/login")
                    .successHandler(loginSuccessHandler(null))
                    .and()
                .oauth2Login()
                    .loginPage("/oauth2/login")
                    .clientRegistrationRepository(clientRegistrationRepository())
                    .authorizedClientService(authorizedClientService())
                    .and()
                .logout()
                    .and()
                .csrf()
                    .and()
//                  .disable()
                .sessionManagement()
                    .sessionFixation()
                        .none()
                    .and()
                .headers()
                    .defaultsDisabled()
                    .frameOptions().sameOrigin()
                    .and()
                .exceptionHandling()
                    .accessDeniedPage("/error/403")
                    .and()
                    .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService customUserDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler(RedisTemplate<String, String> redisTemplate) {
        return new LoginSuccessHandler(redisTemplate);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(github());
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    private ClientRegistration github() {
        return CommonOAuth2Provider.GITHUB.getBuilder("github")
                .userNameAttributeName("name")
                .clientId("ebebf6f78e98feada2ea")
                .clientSecret("1cac5162232e4e47deca4422eef89774305c30ed")
                .build();
    }

}

