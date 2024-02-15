package com.protvino.splitwise.infra.security;

import com.protvino.splitwise.adapter.impl.SqlUserDao;
import com.protvino.splitwise.domain.value.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsersDetailsService implements UserDetailsService {

    private final SqlUserDao sqlUserDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = sqlUserDao.findByUserName(username);
        if (user.equals(null))
            throw new UsernameNotFoundException("User Not Found");
        return new StorageUserDetailsImpl(user);
    }
}
