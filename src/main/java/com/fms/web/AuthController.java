package com.fms.web;

import com.fms.entity.User;
import com.fms.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /*
     * CONNEXION
     */
    
    /** Formulaire de connexion */
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    /** Traitement de la connexion */
    @PostMapping("/login")
    public String login(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        Optional<User> user = userService.authenticate(email, password);
        if (user.isPresent()) {
            session.setAttribute("connectedUser", user.get());
            return "redirect:/tasks";
        }
        model.addAttribute("error", "Identifiants incorrects");
        return "login";
    }

    /*
     * INSCRIPTION
     */

    /** Formulaire d'inscription */
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    /** Traitement de l'inscription */
    @PostMapping("/register")
    public String register(@RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas");
            return "register";
        }
        try {
            userService.register(username, email, password);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    /*
     * DECONNEXION
     */

    /** Déconnexion */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}