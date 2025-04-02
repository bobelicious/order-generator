package com.augusto.order_gen_auth.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.augusto.order_gen_auth.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailOrCpfOrUsername) throws UsernameNotFoundException {
        var user = userRepository
                .findByEmailOrCpfOrUserNameAndDeletedFalse(emailOrCpfOrUsername, emailOrCpfOrUsername, emailOrCpfOrUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found!"));
        var authorities = user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toSet());
        return new User(user.getUserName(), user.getPassword(), authorities);
    }

}
