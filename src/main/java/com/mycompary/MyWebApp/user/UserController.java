package com.mycompary.MyWebApp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String showUserList(Model model) {

        Iterable<User> listUsers = userService.listAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/users/new")
    public String showNewForm(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Add New User");

        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes ra) {

        userService.saveUser(user);

        ra.addFlashAttribute("Message", "The user has been saved successfully");

        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUser(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {

        try {
            User user = userService.getUser(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User ( ID : " + id + " )");

            return "user_form";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("Message", e.getMessage());

            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {

        try {
            userService.deleteUser(id);
            ra.addFlashAttribute("Message", String.format("( ID : %s ) has been delete successfully",id ));
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("Message", e.getMessage());
        }

        return "redirect:/users";
    }
}
