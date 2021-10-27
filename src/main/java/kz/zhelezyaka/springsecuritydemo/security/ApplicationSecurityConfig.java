package kz.zhelezyaka.springsecuritydemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static kz.zhelezyaka.springsecuritydemo.security.ApplicationUserPermission.COURSE_WRITE;
import static kz.zhelezyaka.springsecuritydemo.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true);
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails vladimirUser = User.builder()
                .username("vladimir")
                .password(passwordEncoder.encode("qwerty"))
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails vladUser = User.builder()
                .username("vlad")
                .password(passwordEncoder.encode("vlad123"))
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails nikolayUser = User.builder()
                .username("nikolay")
                .password(passwordEncoder.encode("nikolay123"))
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(vladimirUser, vladUser, nikolayUser);
    }
}
