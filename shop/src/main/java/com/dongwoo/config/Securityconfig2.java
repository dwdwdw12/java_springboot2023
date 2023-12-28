package com.dongwoo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class Securityconfig2 extends WebSecurityConfigurerAdapter {
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .mvcMatchers("/user/**").authenticated()
//                .mvcMatchers("/manager/**").access("hasRole('ADMIN') or hasRole('MANAGER')")
//                .mvcMatchers("/admin/**").access("hasRole('ADMIN')")
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/loginForm")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/");
//    }
//}