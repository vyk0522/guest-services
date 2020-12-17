package com.onejava.model;

import com.onejava.entity.Guest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class GuestDTO {
    @NotNull(message = "FirstName is missing")
    private String firstName;
    private String lastName;
    @Email
    private String emailAddress;
    @NotNull(message = "Address is missing")
    @Size(min = 5, message = "Address should be at least 5 characters")
    private String address;
    private String country;
    private String state;
    @NotNull(message = "PhoneNumber is missing")
    private String phoneNumber;

    public Guest translateModelToGuest(){
        Guest guest = new Guest();
        guest.setFirstName(this.firstName);
        guest.setLastName(this.lastName);
        guest.setEmailAddress(this.emailAddress);
        guest.setAddress(this.address);
        guest.setCountry(this.country);
        guest.setState(this.state);
        guest.setPhoneNumber(this.phoneNumber);
        return guest;
    }
}
