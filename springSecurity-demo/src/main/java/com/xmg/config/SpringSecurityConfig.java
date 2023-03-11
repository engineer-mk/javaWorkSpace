package com.xmg.config;

import com.xmg.component.CustomAuthorizationManager;
import com.xmg.filter.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static java.util.Collections.singletonList;

/**
 * @author makui
 * @created 2023/3/9
 **/
@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final CustomAuthorizationManager customAuthorizationManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain web(HttpSecurity http) throws Exception {
        return http
                //使用token鉴权无需csrf
                .csrf().disable()
                //关闭匿名token
                .anonymous().disable()
                //jwt过滤器
                .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                //关闭session
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //所有请求交付给自定义的AuthorizationManager处理
                .authorizeHttpRequests().requestMatchers("/**").access(customAuthorizationManager)
                .and()
                //异常处理
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                })
                .accessDeniedHandler((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, authException.getMessage());
                })
                .and()
//                .formLogin((conf) -> conf.loginProcessingUrl("/login").permitAll())
                .build();
    }

    /**
     * 跨域配置
     **/
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(singletonList("*"));
        configuration.setAllowedHeaders(singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
