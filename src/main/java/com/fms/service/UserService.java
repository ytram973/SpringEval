package com.fms.service;

import com.fms.entity.User;
import com.fms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Inscription d'un nouvel utilisateur
     */
    public User register(String username, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Nom d'utilisateur déjà pris");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("USER");

        return userRepository.save(user);
    }

    /**
     * Authentification — retourne l'utilisateur si les identifiants sont corrects
     */
    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }

    /**
     * Récupérer un utilisateur par son id
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}