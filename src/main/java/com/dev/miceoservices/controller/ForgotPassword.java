package com.dev.miceoservices.controller;

import com.dev.miceoservices.communication.MailSender;
import com.dev.miceoservices.model.User;
import com.dev.miceoservices.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
public class ForgotPassword {
    @Autowired
    private MailSender mailSender;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    Random random = new Random(1000);

    @GetMapping("/forgot")
    public String forgotPassword(){
        return "forgot_password";
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email, HttpSession session){
        int otp = random.nextInt(9999);

        System.out.println("otp----."+otp);

        String to = email;
        String message = "<h1> OTP = " +otp+"</h1>";
        boolean password_forgot = mailSender.sendEmail(to, message, "password forgot");
        if (password_forgot){

            session.setAttribute("myotp",otp);
            session.setAttribute("email",email);
            return "otp_process";
        }
        else {
           return "forgot_password";
        }
    }
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") Integer otp,HttpSession session){
        Integer myotp =(Integer) session.getAttribute("myotp");
        String email = (String) session.getAttribute("email");

        if (myotp.equals(otp)){

            User user = userRepo.findByEmail(email);
            if (user==null){
                return "forgot_password";
            }
            return "set_password";
        }else {
            return "otp_process";
        }

    }
    @PostMapping("/set-password")
    public String setNewPassword(@RequestParam("newPassword") String newPassword,HttpSession session){
        String email = (String) session.getAttribute("email");
        User user = userRepo.findByEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepo.save(user);
        return "redirect:/login?change=password change successfully....";
    }
}
