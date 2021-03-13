package com.example.demo.security;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userDetailsService;

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new MyAuthenticationSuccessHandler();
    }

    @Autowired
    private LogoutSuccessHandler myLogoutSuccessHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/register");
        web.ignoring().antMatchers("/addUser");
        web.ignoring().antMatchers("/addCustomer");
        web.ignoring().antMatchers("/addFigurine");
        web.ignoring().antMatchers("/figurines");
        web.ignoring().antMatchers("/indexFigurine");
        web.ignoring().antMatchers("/addOpinion");
        web.ignoring().antMatchers("/addLicence");
        web.ignoring().antMatchers("/addSubscription");
        web.ignoring().antMatchers("/addCategory");
        web.ignoring().antMatchers("/indexCategory");
        web.ignoring().antMatchers("/indexOpinion");
        web.ignoring().antMatchers("/add");
        web.ignoring().antMatchers("/index");
        web.ignoring().antMatchers("/licences");
        web.ignoring().antMatchers("/opinion");
        web.ignoring().antMatchers("/subscription");
        web.ignoring().antMatchers("/logout");
        web.ignoring().antMatchers("/favicon.ico");

        web.ignoring().antMatchers("/listSubscription");
        web.ignoring().antMatchers("/listLicence");
        web.ignoring().antMatchers("/listCategory");
        web.ignoring().antMatchers("/deleteSubscription");
        web.ignoring().antMatchers("/deleteLicence");
        web.ignoring().antMatchers("/deleteCategory");
        web.ignoring().antMatchers("/updateCategory");
        web.ignoring().antMatchers("/updateLicence");
        web.ignoring().antMatchers("/updateSubscription");
        web.ignoring().antMatchers("/findFigurine");
        web.ignoring().antMatchers("/listCategoryPerso");
        web.ignoring().antMatchers("/listLicencePerso");
        web.ignoring().antMatchers("/listSubscriptionPerso");
        web.ignoring().antMatchers("/listFigurinePerso");
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/favicon.ico", "/index","/register","/addAdministrator", "/addCustomer", "/indexFigurine","/addFigurine", "/licences", "/opinion",
                        "/addLicence", "/addOpinion", "/indexCategory", "/subscription", "/addSubscription","/listSubscription", "/deleteSubscription","/users",
                        "/listLicence", "/deleteLicence", "/listCategory", "/deleteCategory", "/addCategory", "/updateCategory", "/updateLicence", "/updateSubscription",
                        "/figurines", "/images/**", "static/**", "/findFigurine", "/listCategoryPerso", "/listLicencePerso", "/listSubscriptionPerso", "/listFigurinePerso",
                        "/indexOpinion","/logout").permitAll()

                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .headers().disable()
                .authenticationProvider(getProvider())
                .formLogin()
                .loginPage("/login")
                .successHandler(myAuthenticationSuccessHandler())
                .passwordParameter("password")
                .usernameParameter("username")
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(myLogoutSuccessHandler)

                .logoutSuccessUrl("/indexlogout")

                .permitAll();


    }

    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getProvider());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationProvider getProvider() {
        CustomAuthProvider provider = new CustomAuthProvider();
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}