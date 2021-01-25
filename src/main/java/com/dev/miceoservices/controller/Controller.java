package com.dev.miceoservices.controller;

import com.dev.miceoservices.message.Message;
import com.dev.miceoservices.model.User;
import com.dev.miceoservices.repo.UserRepo;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;


@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("title" , "home - smartcontact manager");
        return "home";
    }
    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("title" , "About - smartcontact manager");
        return "about";
    }
    @RequestMapping("/signup")
    public String signup(Model model){
        model.addAttribute("title" , "Signup - smartcontact manager");
        model.addAttribute("user", new User());
        return "signup";
    }
    @PostMapping("/do_register")
    public String register(@Valid @ModelAttribute("user") User user ,BindingResult result1,
                           @RequestParam(value = "agreement", defaultValue = "false")boolean agreement,
                           Model model, HttpSession session){
        try {
            if (!agreement) {
                System.out.println(" you have not agreed the terms and condition");
                throw new Exception("you have not agreed the terms and condition");
            }

            User emailExist = userRepo.findByEmail(user.getEmail());
            if (emailExist != null){
               session.setAttribute("message", new Message("There is already an account registered with that email","alert-warning"));
               return "signup";
            }
            if (result1.hasErrors()){
                System.out.println("error" + result1.toString());
                model.addAttribute("user",user);
                return "signup";
            }
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImage_url("default");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User result = userRepo.save(user);

            model.addAttribute("user", user);
            session.setAttribute("message",new Message("successfully register", "alert-success"));
            return "login";
        }catch(Exception e){
               e.printStackTrace();
               model.addAttribute("user", user);
               session.setAttribute("message",new Message("something went wrong " + e.getMessage(), "alert-danger"));
               return "signup";
        }
    }
    @GetMapping("/login")
    public String userLogin(Model model){
        model.addAttribute("title","Login here");
        return "login";
    }
    @GetMapping("/changepassword")
    public String changePassword(){
        return "changepassword";
    }

    @PostMapping("/change-password")
    public String processChangePassword(@RequestParam("oldPassword") String oldPassword ,
                                        @RequestParam("newPassword") String newPassword,
                                        Principal principal,HttpSession session){
        System.out.println("oldpassword" + oldPassword);
        System.out.println("new password"+ newPassword);
        User user = userRepo.findByEmail(principal.getName());

        if (bCryptPasswordEncoder.matches(oldPassword,user.getPassword())){
           user.setPassword(bCryptPasswordEncoder.encode(newPassword));
           userRepo.save(user);
           session.setAttribute("message", new Message("Password change successfully" ,"success"));
        }else {
            session.setAttribute("message", new Message("Password change successfully" ,"success"));
            return "/changepassword";
        }

        return "/login";
    }
}
