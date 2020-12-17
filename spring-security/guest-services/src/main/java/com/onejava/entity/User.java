package com.onejava.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class User {
    @Id
    @Column(name="USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="USERNAME", nullable = false, unique = true)
    private String username;
    @Column(name="PASSWORD")
    private String password;
}
