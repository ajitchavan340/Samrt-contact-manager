package com.dev.miceoservices.controller;

import com.dev.miceoservices.message.Message;
import com.dev.miceoservices.model.Contact;
import com.dev.miceoservices.model.User;
import com.dev.miceoservices.repo.ContactRepo;
import com.dev.miceoservices.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class TestController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ContactRepo contactRepo;
    @ModelAttribute
    public void commonData(Model model,Principal principal){
        model.addAttribute("title" , "user - smartcontact manager");
        String userName = principal.getName();
        System.out.printf("usename" + userName);
        User byEmail = userRepo.findByEmail(userName);

        model.addAttribute("user" + byEmail);


    }
    @GetMapping("/me")
    public String Hello(){

        return "dashboard";
    }
    @GetMapping("/add_contact")
    public String addContact(Model model){
        model.addAttribute("title","add contact");
        model.addAttribute("contatc" , new Contact());
        return "normal/add_contact";
    }
    @PostMapping(value ="/contact_process", consumes = {"multipart/form-data"})
    public String saveContact(@ModelAttribute("contact") Contact contact,
                              @RequestParam("profileImage") MultipartFile file,
                              Principal principal, HttpSession session){
        try {
            String name = principal.getName();

            User byUsername = userRepo.findByEmail(name);


            if (file.isEmpty()){

                contact.setImage_url("contact.png");

            }else {
                contact.setImage_url(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            }
            contact.setUser(byUsername);
            contactRepo.save(contact);
            session.setAttribute("message",new Message("Contact add successfully , Add more ", "success"));

        } catch (Exception e){
            System.out.println("error" +e);
            e.printStackTrace();
            session.setAttribute("message",new Message("Something went wrong ", "danger"));
        }
        //System.out.println("Data" + contact);

        return "normal/add_contact";
    }
    @GetMapping("/show_contact/{page}")
    public String showContact(@PathVariable Integer page,Model model , Principal principal){
        String name = principal.getName();

        User user = userRepo.findByEmail(name);
        PageRequest pageRequest = PageRequest.of(page, 5);

        Page<Contact> contacts = contactRepo.findContactByUser(user.getUser_id(),pageRequest);
        model.addAttribute("contacts", contacts);
        model.addAttribute("currentpage",page);
        model.addAttribute("totalpage", contacts.getTotalPages());
        return "normal/show_contact";
    }

    @GetMapping("/{contact_id}/contact")
    public String showDetails(@PathVariable Integer contact_id , Model model ,Principal principal ){

        Optional<Contact> byId = contactRepo.findById(contact_id);
        Contact contact = byId.get();
        String name = principal.getName();
        User user = userRepo.findByEmail(name);
        if (user.getUser_id()==contact.getUser().getUser_id()) {
            model.addAttribute("contact", contact);
        }
        return "normal/show_details";
    }
    @PostMapping("/delete/{contact_id}")
    public String deleteContact(@PathVariable Integer contact_id, HttpSession session,Principal principal){
        String name = principal.getName();
        User user = userRepo.findByEmail(name);
        Optional<Contact> byId = contactRepo.findById(contact_id);
        Contact contact = byId.get();
        if (user.getUser_id()==contact.getUser().getUser_id()) {
            contactRepo.deleteById(contact_id);
        }

        session.setAttribute("delete", new Message("contact deleted successfully","success"));
        return "redirect:/user/show_contact/0";
    }
    @PostMapping("/update_contact/{cid}")
    public String updateContact( @PathVariable Integer cid, Model model){

       Contact contact = contactRepo.findById(cid).get();
        model.addAttribute("contact",contact);
        model.addAttribute("title","update contact");

        return "normal/update_contact";
    }
    @PostMapping("/update_process")
    public String updateProcess(@ModelAttribute Contact contact,
                                Model model,Principal principal,
                                @RequestParam("profileImage") MultipartFile file){
        Contact oldContact = this.contactRepo.findById(contact.getContact_id()).get();
        try {
            if (!file.isEmpty()){
                File deleteFile = new ClassPathResource("static/img").getFile();
                File file1 = new File(deleteFile ,oldContact.getImage_url());

                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                contact.setImage_url(file.getOriginalFilename());

            }else {
                contact.setImage_url(oldContact.getImage_url());
            }
            User user = userRepo.findByEmail(principal.getName());
            contact.setUser(user);
            contactRepo.save(contact);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/user/show_contact/0";
    }
}
