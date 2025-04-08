package com.augusto.usersystemapi.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.augusto.usersystemapi.repository.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrCpfOrUsername) throws UsernameNotFoundException {
        var user = userRepository
                .findByEmailOrCpfOrUserNameAndDeletedFalse(emailOrCpfOrUsername, emailOrCpfOrUsername,
                        emailOrCpfOrUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found!"));
        var authorities = user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toSet());
        return new User(user.getUserName(), user.getPassword(), authorities);
    }

}
