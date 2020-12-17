package com.onejava.service;

import com.onejava.model.AuthenticationRequest;
import com.onejava.model.AuthenticationResponse;
import com.onejava.model.Guest;
import com.onejava.model.GuestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name = "guest-service")
@FeignClient(name = "gateway-service")  //Change1: to use gateway-service
// @RequestMapping("/guests")   // This is Wrong, we can not use like this
public interface GuestServiceProxy {

    //@GetMapping("/guests")
    @GetMapping("/guest-service/guests") //Change2: to use gateway-service, append application name in all endpoints
    public List<Guest> retrieveGuests();

    //@PostMapping("/guests")
    @PostMapping("/guest-service/guests")
    public Guest addAGuest(GuestDTO guestDTO);

    // If we have multiple arguments with one request body and more params. Specify the argument types using exact annotations
    //@GetMapping("/guests/{id}")
    @GetMapping("/guest-service/guests/{id}")
    public Guest getAGuest(@RequestParam("id") Long id);

    //@PutMapping("/guests/{id}")
    @PutMapping("/guest-service/guests/{id}")
    public Guest updateAGuest(@RequestParam("id") Long id, @RequestBody GuestDTO guestDTO);

    //@DeleteMapping("/guests/{id}")
    @DeleteMapping("/guest-service/guests/{id}")
    public void deleteAGuest(@RequestParam("id") Long id);

    @PostMapping("/guest-service/users/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest);


}
