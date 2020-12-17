package com.onejava.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // For authorization
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyHotelUserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        // For BCryptPasswordEncoder of strength 11
        //provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setPasswordEncoder(new BCryptPasswordEncoder(11));
        // For authorization
        provider.setAuthoritiesMapper(authoritiesMapper());
        return provider;
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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    // Basic Authentication. Default user is user and Password - Get it in log
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index", "/css/*", "/js/*", "/greeting/*").permitAll()
                .anyRequest().authenticated()
                .and()
               // .httpBasic()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/logout-success").permitAll();
    }


    // In-memory authentication

    /*@Bean
    @Override
    public UserDetailsService userDetailsService() {
        List<UserDetails> users = new ArrayList<>();
        users.add(User.withDefaultPasswordEncoder().username("vivek").password("password1").roles("USER", "ADMIN").build());
        users.add(User.withDefaultPasswordEncoder().username("vinay").password("password2").roles("USER").build());
        return new InMemoryUserDetailsManager(users);
    }*/


}
