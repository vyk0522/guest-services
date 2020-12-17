package com.onejava.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Guest {
    private long id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String address;
    private String country;
    private String state;
    private String phoneNumber;
}
