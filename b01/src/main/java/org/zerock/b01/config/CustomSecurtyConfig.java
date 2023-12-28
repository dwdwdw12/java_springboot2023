package org.zerock.b01.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b01.security.CustomUserDetailService;
import org.zerock.b01.security.handler.Custom403Handler;
import org.zerock.b01.security.handler.CustomSocialLoginSuccessHandler;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)    //사전/사후 권한 확인
public class CustomSecurtyConfig {

    private final DataSource dataSource;
    private final CustomUserDetailService userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info("--------------security---configure---------------");

        //커스텀 로그인 페이지
        http.formLogin(config -> {
            config.loginPage("/member/login");
        });  //람다 식으로 바꾸어야 함

        //CSRF 토큰 비활성화
        http.csrf(config->{config.disable();});

        //remember me
        http.rememberMe(config->{
            config.key("12345678")
                    .tokenRepository(persistentTokenRepository())
                    .userDetailsService(userDetailService)
                    .tokenValiditySeconds(60*60*24*30); //30일 유효기간
        });

        http.exceptionHandling(config->{
            config.accessDeniedHandler(accessDeniedHandler());  //403
        });

        http.oauth2Login(config->{
            config.loginPage("/member/login")
                    .successHandler(authenticationSuccessHandler());
        });

        return http.build();    //SecurityFilterChain 객체 반환. 로그인 없이 모든 사용자가 list에 접근 가능.
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("-------------web configure-------------");

        return (web)->web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());  //정적 자원 - 시큐리티 적용 무시(css 파일, js 파일 등)
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

}
