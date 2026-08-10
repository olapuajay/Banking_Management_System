package com.banking.banking_management_system.security.service;

import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.exception.ResourceNotFoundException;
import com.banking.banking_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                )
                .accountLocked(user.getStatus().name().equals("LOCKED"))
                .disabled(user.getStatus().name().equals("DISABLED"))
                .build();
    }
}
