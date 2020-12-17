package com.onejava.auth;

import com.onejava.exception.JwtExceptionHandlerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // For authorization
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyHotelUserDetailsService userDetailsService;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtExceptionHandlerFilter jwtExceptionHandlerFilter;

    // Basic Authentication. Default user is user and Password - Get it in log
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable()
                .authorizeRequests().antMatchers("/users/authenticate").permitAll()
                //.authorizeRequests().antMatchers("/guests/**", "/users/authenticate").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionHandlerFilter, JWTAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.inMemoryAuthentication().withUser("admin").password(bCryptPasswordEncoder().encode("pass")).roles("USER");
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        //provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setPasswordEncoder(bCryptPasswordEncoder()); // For BCryptPasswordEncoder of strength 11
        provider.setAuthoritiesMapper(authoritiesMapper());  // For authorization
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(11);
    }

    // For authorization
    // It takes role value from DB, converts to UC and adds ROLE_ to the role
    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper(){
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToUpperCase(true);
        authorityMapper.setDefaultAuthority("USER");
        return authorityMapper;
    }
}
