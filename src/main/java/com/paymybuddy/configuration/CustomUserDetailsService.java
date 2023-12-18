package com.paymybuddy.configuration;

import com.paymybuddy.application.model.UserAccount;
import com.paymybuddy.application.repository.UserAccountRepositoryJpa;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepositoryJpa userAccountRepositoryJpa;

    public CustomUserDetailsService(UserAccountRepositoryJpa userAccountRepositoryJpa) {
        this.userAccountRepositoryJpa = userAccountRepositoryJpa;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userAccountRepositoryJpa.findByEmail(username);

        return new User(user.getEmail(), user.getPassword(), getGrantedAuthorities(user.getRole()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}
