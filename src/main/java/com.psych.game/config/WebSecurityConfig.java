package com.psych.game.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//This annotation provides annotation configuration for your application, there can be only one configuration class
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService customUserDetailsService;
    @Override
    public void configure(HttpSecurity http) throws Exception{

        /*
        the hasRole function after anyRequest let you tell which role is granted permissions
        e.g - http.antMatchers("/dev-test/*").hasRole("ROLE_xyz");
         */
        http
                .authorizeRequests()
                .antMatchers("/dev-test/players").permitAll() //This all access from this endpoint //Ant framework is our framework
                .anyRequest().authenticated() //This endpoint is authenticated
                .and().formLogin().permitAll()
                .and().logout().permitAll();
    }
    //Spring does not allow password to be stored in plain-text format so it prompts you to use a password encoder
    //Also it needs it own encoder object to know what password you are entering
    @Bean //Bean for Spring to work with
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        // Basically manages user to be authenticated
        auth.parentAuthenticationManager(authenticationManagerBean())
                .userDetailsService(customUserDetailsService);
    }
}
