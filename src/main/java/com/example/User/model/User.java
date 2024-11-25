package com.example.User.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data  /*Lombok annotation that creates the setters and getters of the fields in the class*/
@NoArgsConstructor /*Lombok annotation that creates an automatic constructor with no arguments*/
@AllArgsConstructor /*Lombok annotation that creates an automatic constructor with arguments*/
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;





}