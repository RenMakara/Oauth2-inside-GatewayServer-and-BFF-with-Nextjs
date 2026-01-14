package co.istad.makara.authorizationserver.security;


import co.istad.makara.authorizationserver.domain.Role;
import co.istad.makara.authorizationserver.domain.User;
import co.istad.makara.authorizationserver.feature.role.RoleRepository;
import co.istad.makara.authorizationserver.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Load user from database
        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Build userDetails object
        String[] roles = loggedInUser.getRoles().stream()
                .map(Role::getName)
                .toArray(String[]::new);


        List<GrantedAuthority> authorities = new ArrayList<>();
        loggedInUser.getRoles().forEach(role ->{
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            });
        });

        UserDetails userSecurity = org.springframework.security.core.userdetails.User.builder()
                .username(loggedInUser.getUsername())
                .password(loggedInUser.getPassword())
//                .roles(roles)
                .authorities(authorities)
                .build();

        log.info("UserDetailsServiceImpl load user by username: {}", userSecurity.getAuthorities());
        log.info("UserDetailsServiceImpl load user by username: {}", userSecurity.getUsername());

        return userSecurity;
    }
}