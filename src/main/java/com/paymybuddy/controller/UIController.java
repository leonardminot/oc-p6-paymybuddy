package com.paymybuddy.controller;

import com.paymybuddy.Exception.EmailException;
import com.paymybuddy.Exception.EmptyFieldException;
import com.paymybuddy.Exception.UsernameException;
import com.paymybuddy.dto.CreateUserAccountCommandDTO;
import com.paymybuddy.dto.UserRequestCommandDTO;
import com.paymybuddy.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@Slf4j
public class UIController {

    private final UserAccountService userAccountService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UIController(UserAccountService userAccountService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userAccountService = userAccountService;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "hasError", required = false) boolean hasError,
            Model model) {
        model.addAttribute("hasError", hasError);
        log.info("HasError: " + hasError);
        return "login";
    }

    @GetMapping("/createAccount")
    public String createAccount(Model model) {
        if (!model.containsAttribute("newUserCommand"))
            model.addAttribute("newUserCommand", new CreateUserAccountCommandDTO());
        return "createAccount";
    }

    @PostMapping("/createAccount")
    public String createAccount(@ModelAttribute CreateUserAccountCommandDTO newUserCommand, RedirectAttributes ra, Model model) {

        if (!Objects.equals(newUserCommand.getPassword(), newUserCommand.getPasswordConfirmation())) {
            ra.addFlashAttribute("arePasswordDifferent", true);
            ra.addFlashAttribute("newUserCommand", newUserCommand);
            return "redirect:/createAccount";
        }

        try {
            userAccountService.createUserAccount(new UserRequestCommandDTO(
                    newUserCommand.getUserName(),
                    newUserCommand.getEmail(),
                    passwordEncoder.encode(newUserCommand.getPassword()),
                    newUserCommand.getFirstName(),
                    newUserCommand.getLastName()
            ));
        } catch (EmailException exception) {
            ra.addFlashAttribute("isEmailExists", true);
            ra.addFlashAttribute("newUserCommand", newUserCommand);
            return "redirect:/createAccount";
        } catch (UsernameException exception) {
            ra.addFlashAttribute("isUsernameExists", true);
            ra.addFlashAttribute("newUserCommand", newUserCommand);
            return "redirect:/createAccount";
        } catch (EmptyFieldException exception) {
            ra.addFlashAttribute("emptyFields", true);
            ra.addFlashAttribute("newUserCommand", newUserCommand);
            return "redirect:/createAccount";
        }

        return "redirect:/login";
    }

    @GetMapping("/")
    public String showFirstPage() {
        return "connexion";
    }
}
