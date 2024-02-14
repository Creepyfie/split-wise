package com.protvino.splitwise.infra.security;

import com.protvino.splitwise.adapter.impl.SqlUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
@RequiredArgsConstructor
public class AuthProviderImpl implements AuthenticationProvider {

    private final UsersDetailsService usersDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userName = authentication.getName();

        UserDetails userDetails = usersDetailsService.loadUserByUsername(userName);

        String password = authentication.getCredentials().toString();

        if(!password.equals(userDetails.getPassword()))
            throw new BadCredentialsException("incorrect password");

        return new UsernamePasswordAuthenticationToken(userDetails, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
