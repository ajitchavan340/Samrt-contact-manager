package com.dev.miceoservices.model;

import lombok.*;

import javax.persistence.*;

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
    private String conact_name;
    private String nick_name;
    private String work;
    private String email;
    private String phone;
    private String image_url;

    @ManyToOne
    private User user ;

}
