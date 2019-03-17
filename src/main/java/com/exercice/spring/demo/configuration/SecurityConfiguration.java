package com.exercice.spring.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // activation de la sécurité
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // objet qui permet de crypté et décrypté un mot de passe
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //1. lorsque l'utilisateur demande les url /, /login et /h2-console, /logout
        // il n'a pas besoin d'être authentifié pour
        // qu'on lui affiche le contenu de ces pages (permitAll)

        //2. si l'utilisateur demande une url qui commence par /admin =>
        // il doit être authentifié avec le role ADMIN

        //3. si l'authentification échoue on ajoute error=true en paramètre de l'url
        //4. si l'authentification est un succés en on redirige l'utilisateur vers /admin
        //5. les paramètres du formulaire doivent comporter deux input avec comme name email et password
        //6. lorsque l'utilisateur demande la page /logout il est déconnecté puis redirigé vers la page /logn
        httpSecurity
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/saveUser").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers("/logout").permitAll();

        httpSecurity
                /*.authorizeRequests()
                    .antMatchers("/admin/**")
                    .hasAuthority("ADMIN")
                    .anyRequest()
                    .authenticated()
                    .and()*/
                .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login?error=true")
                    .defaultSuccessUrl("/admin")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .and()
                .exceptionHandling()
                    .accessDeniedPage("/access-denied");

        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/resources/**",
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/fonts/**");
    }

}