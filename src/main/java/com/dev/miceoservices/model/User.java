package com.dev.miceoservices.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;

    @Column(unique = true)
    @NotBlank(message = "user name cannot be empty")
    private String user_name;

    @Column(unique = true)
    @Email(regexp = "^[a-zA-Z0-9+._-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotBlank(message = "enter password")
   // @Size(min = 6 ,max = 12 , message = "password must be between 6 to 12 character")
    private String password;

    private String role;
    private boolean enabled;
    private String image_url;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Contact> contacts = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", image_url='" + image_url + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
