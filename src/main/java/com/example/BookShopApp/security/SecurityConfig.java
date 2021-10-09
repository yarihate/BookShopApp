package com.example.BookShopApp.security;

import com.example.BookShopApp.data.services.BookstoreUserDetailsService;
import com.example.BookShopApp.security.jwt.JWTRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTRequestFilter filter;
    private final CustomLogoutHandler customLogoutHandler;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    public SecurityConfig(BookstoreUserDetailsService bookstoreUserDetailsService, JWTRequestFilter filter, CustomLogoutHandler customLogoutHandler, CustomAuthenticationProvider customAuthenticationProvider) {
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.filter = filter;
        this.customLogoutHandler = customLogoutHandler;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/my", "/profile").authenticated()//.hasRole("USER")
                .antMatchers("/**").permitAll()
                .and().formLogin()
                .loginPage("/signin").failureUrl("/signin")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/signin").deleteCookies("token").addLogoutHandler(customLogoutHandler)
                .and().oauth2Login()
                .successHandler((request, response, authentication) -> {
                    DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
                    bookstoreUserDetailsService.processOAuthPostLogin(oauthUser.getAttribute("email"), oauthUser.getAttribute("name"));
                    response.sendRedirect("/my");
                });


        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
