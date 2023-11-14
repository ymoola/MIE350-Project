package com.example.RideShare.auth;

import com.example.RideShare.model.entity.User;
import com.example.RideShare.model.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.RideShare.security.ApplicationUserRole.*;

@Repository("H2")
public class H2UserDaoService implements ApplicationUserDao {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public H2UserDaoService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }
    public List<ApplicationUser> getApplicationUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new ApplicationUser(
                        user.getEmail(),
                        passwordEncoder.encode(user.getPassword()),
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ))
                .collect(Collectors.toList());
    }
}
