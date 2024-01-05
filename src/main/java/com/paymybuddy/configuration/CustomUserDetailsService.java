package com.paymybuddy.configuration;

import com.paymybuddy.model.UserAccount;
import com.paymybuddy.repository.jpa.UserAccountRepositoryJpa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepositoryJpa userAccountRepositoryJpa;

    @Autowired
    public CustomUserDetailsService(UserAccountRepositoryJpa userAccountRepositoryJpa) {
        this.userAccountRepositoryJpa = userAccountRepositoryJpa;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       UserAccount user = userAccountRepositoryJpa.findByEmailEquals(username)
               .orElseThrow(() -> new UsernameNotFoundException("User not present"));

       return new User(user.getEmail(), user.getPassword(), getGrantedAuthorities(user.getRole()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}
