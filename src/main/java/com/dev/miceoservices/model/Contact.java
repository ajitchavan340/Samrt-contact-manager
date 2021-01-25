package com.dev.miceoservices.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int contact_id;
    @NotBlank(message = "user name cannot be empty")
    private String name;
    private String nick_name;
    private String work;

    @Column(unique = true)
    @Email(regexp = "^[a-zA-Z0-9+._-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Column(unique = true)
    @NotBlank(message = "mobile number cannot be empty")
    private String phone;
    private String image_url;

    @ManyToOne
    @JsonIgnore
    private User user ;

}
