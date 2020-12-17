package com.onejava.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.onejava.entity.Guest;
import com.onejava.exception.GuestNotFoundException;
import com.onejava.model.GuestDTO;
import com.onejava.repository.GuestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/guests")
@Validated // Required for PathVariable validation
public class GuestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuestController.class);
    @Autowired
    private GuestRepository guestRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Guest> getAllGuests(){
        return new ArrayList(this.guestRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Guest> addGuest(@Valid @RequestBody GuestDTO model){
        Guest guest = this.guestRepository.save(model.translateModelToGuest());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(guest.getId()).toUri();
        return ResponseEntity.created(location).body(guest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Guest getGuest(@PathVariable @Min(1) Long id){
        Optional<Guest> guest = this.guestRepository.findById(id);
        if(guest.isPresent()){
            return guest.get();
        }
        throw new GuestNotFoundException("Guest not found with id: " + id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Guest updateGuest(@PathVariable Long id, @RequestBody GuestDTO model){
        Optional<Guest> existing = this.guestRepository.findById(id);
        if(!existing.isPresent()){
            throw new GuestNotFoundException("Guest not found with id: " + id);
        }
        Guest guest = model.translateModelToGuest();
        guest.setId(id);
        return this.guestRepository.save(guest);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Guest updatePartialGuest(@PathVariable Long id, @RequestBody JsonPatch patch){
        Optional<Guest> existingGuest = this.guestRepository.findById(id);
        if(!existingGuest.isPresent()){
            throw new GuestNotFoundException("Guest not found with id: " + id);
        }
        Guest guestPatched = null;
        try {
            guestPatched = applyPatchToGuest(patch, existingGuest.get());
            return this.guestRepository.save(guestPatched);
        } catch (GuestNotFoundException e) {
            throw new GuestNotFoundException("Guest not found with id: " + id);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new GuestNotFoundException("JSON Patch Exception"); // If path field does not exist
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteGuest(@PathVariable Long id){
        Optional<Guest> existing = this.guestRepository.findById(id);
        if(!existing.isPresent()){
            throw new GuestNotFoundException("Guest not found with id: " + id);
        }
        this.guestRepository.deleteById(id);
    }

    private Guest applyPatchToGuest(JsonPatch patch, Guest guest) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(guest, JsonNode.class));
        return objectMapper.treeToValue(patched, Guest.class);
    }

}
