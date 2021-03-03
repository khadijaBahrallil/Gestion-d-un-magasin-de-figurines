package com.example.demo.security;

import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthProvider extends DaoAuthenticationProvider {

    @Autowired
    UserService userDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

        String name = auth.getName();
        String password = auth.getCredentials()
                .toString();


        UserDetails user = userDetailsService.loadUserByUsername(name);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (user == null) {
            throw new BadCredentialsException("Invalid Credentials " + auth.getPrincipal());
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid Credentials " + auth.getPrincipal());
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
