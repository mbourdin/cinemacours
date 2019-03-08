package mbourdin.cinema_cours.config;

import mbourdin.cinema_cours.service.CinemaUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;

/**
 * Projet thyme-security
 * Pour LAERCE SAS
 * <p>
 * Créé le  21/03/2017.
 *
 * @author fred
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private CinemaUserDetailsService cinemaUserDetailsService;
    /*
    @Resource
    public void setUserDetailsService(CinemaUserDetailsService cinemaUserDetailsService){
        this.cinemaUserDetailsService = cinemaUserDetailsService;
    }
    */
    @Autowired
    public WebSecurityConfig(CinemaUserDetailsService cinemaUserDetailsService)
    {   this.cinemaUserDetailsService = cinemaUserDetailsService;

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }




    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .formLogin()
                    .loginPage("/login/connect")
                    .permitAll()
                    .and()
                .formLogin()
                    .loginProcessingUrl("/login/connect")
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/login/logout"))
                    .logoutSuccessUrl("/")
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/admin/**","/genre/**","/news/**","/play/**")
                    .hasAuthority("ADMIN")
                    .anyRequest()
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/user/**")
                    .hasAuthority("USER")
                    .anyRequest()
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/vendeur/**")
                    .hasAuthority("VENDEUR")
                    .anyRequest()
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/**")
                    .permitAll()
                    //.and()
                //.csrf().disable()
                ;
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("recup")
//                .password("recup")
//                .roles("ADMIN","USER")
//                .authorities("WITHDRAW","DEPOSIT","ADMIN");

        auth.userDetailsService(cinemaUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

}
