package peaksoft.house.airbnbb9.api;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import peaksoft.house.airbnbb9.dto.request.UserRequest;
import peaksoft.house.airbnbb9.entity.User;
import peaksoft.house.airbnbb9.service.UserService;

@Controller
public class UserApi {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") @Valid UserRequest userRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "signup";
        }
        // Perform user registration and save user details
//        userService.registerUser(userRequest);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login/google")
    public String googleLogin() {
        // Redirect to Google Sign-In page or display Google Sign-In button
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/login/google/callback")
    public String googleLoginCallback(@RequestParam("code") String code) {
        // Handle Google Sign-In callback and retrieve user information
//        userService.processGoogleLogin(code);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        // Handle successful authentication
        return "home";
    }
}