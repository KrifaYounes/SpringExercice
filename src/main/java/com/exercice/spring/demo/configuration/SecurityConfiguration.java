package com.exercice.spring.demo.configuration;

import com.exercice.spring.demo.domain.User;
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

@Configuration //  anotation bech t9oul eli hiya classe de configuration k
// Remarque : les méthodes configure de cette classe vont être déclenché une seul fois au lancement du serveur
@EnableWebSecurity //  kif kif lena juste bech tsir activation de la sécurité
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
// WebSecurityConfigurerAdapter : hethi  class de spring secruity eli feha bercha methode  qui permtent de configuer
    // secruité ta3 app


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {



        //2. si l'utilisateur demande une url qui commence par /admin =>
        // il doit être authentifié avec le role ADMIN

        //3. si l'authentification échoue on ajoute error=true en paramètre de l'url
        //4. si l'authentification est un succés en on redirige l'utilisateur vers /admin
        //5. les paramètres du formulaire doivent comporter deux input avec comme name email et password
        //6. lorsque l'utilisateur demande la page /logout il est déconnecté puis redirigé vers la page /logn



        httpSecurity
                .authorizeRequests()// lena sar appel de la methode  .authorizeRequests()  qui va retourner un object
                // qui sera utiliser  comme entrer pour tous les methos loutanin eli houma antMatchers()
                    .antMatchers("/").permitAll()
                    .antMatchers("/saveUser").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers("/logout").permitAll();
        ///antMatchers: lorsque l'utilisateur demande les url /, /login et /h2-console, /logout
        // il n'a pas besoin d'être authentifié pour
        // qu'on lui affiche le contenu de ces pages car on appelle la méthode permitAll

        httpSecurity
                // on peut dire à spring security quel est la resource de la page de POST login (page d'authentification
                // avec formLogin. formLogin a plusieurs parametre
                // loginPage =  nom de l'action du formulaire
                // failureURL = url qu'on echoue l'authentification on redirige l'utilisateur vers lurl ecrit
                // defaultSuccessUrl = en cas de success on redirige vers admin
                // usernameParameter et passwordParameter : ce sont les name des input login et password exemple :
                // <input name="password" />
                .formLogin()
                    .loginPage("/login") // nom de l'action du formulaire
                    .failureUrl("/login?error=true")
                    .defaultSuccessUrl("/admin")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .and() // pour changer de workflow
                // logout
                // logoutRequestMatcher : url de logout. quand il va tapper http://localhost:8080/logout il va se deconnecter
                // logoutSuccessUrl : lena ki houwa yenzel 3ala logout  fin bech yemchi lena a5tarna yemchi lpage ta3 login
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .and()
                .exceptionHandling()
                // toutes les pages qui meritent l'authentification on redirige vers access-denied si il n'est pas
                // authentifié
                    .accessDeniedPage("/access-denied");

        // voir internet
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // ignorer la sécurité pour les ressources static
        //  ahana les pages mte3na koul yest3mlou des fichers css ,js, image ... koul chay f ressouce rit spring secruity
        // yebda ye3mel f securité  3ala  accees l ay haja f project lena ahna jina 9olnelou asm3 besna les fichiers eli
        // f west l resources  w les css w les image etc ne7i 3alehou securité c adire  il faut pas demander
        // l'authentification pour les accéder  , dima ki pege tdemandehoum medhoum w chihmek

        web.ignoring()
            .antMatchers("/resources/**",
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/fonts/**");
    }

}