package com.onejava.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "AUTH_USER_GROUP")
public class AuthGroup {
    @Id
    @Column(name="AUTH_USER_GROUP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="USERNAME")
    private String username;
    @Column(name="AUTH_GROUP")
    private String authGroup;
}
