package piastrellista.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import piastrellista.entities.User;
import piastrellista.exceptions.BadRequestException;
import piastrellista.payloads.UserDTO;
import piastrellista.services.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // This method is used to get all users
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUsers(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "20") int size, @RequestParam (defaultValue = "username") String sortBy){
        return this.userService.getAllUsers(page, size, sortBy);
    }

    // This method is used to get a user by its ID
    @GetMapping ("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById (@PathVariable long userId){
        return this.userService.getUserById(userId);
    }

    // This method is used to update an existing user
    @PutMapping ("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUser (@PathVariable long userId, @RequestBody @Validated UserDTO payload, BindingResult validation ){
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return this.userService.updateUser(userId, payload);
    }

    // This method is used to delete a user
    @DeleteMapping ("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    public void deleteUser (@PathVariable long userId){
        this.userService.deleteUser(userId);
    }

    // This method is used to upload an image for a user
    @PostMapping ("/upload/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus (HttpStatus.CREATED)
    public User uploadImage(@RequestParam ("avatar")MultipartFile image, @PathVariable long userId) throws IOException {
        return this.userService.uploadImage(image, userId);
    }

    // This method is used to find the current user
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('MODERATOR') || hasAuthority('USER')")
    public User findMe(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    // This method is used to update the current user
    @PutMapping("/me")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('MODERATOR') || hasAuthority('USER')")
    public User updateMe(@AuthenticationPrincipal User currentUser,
                         @RequestBody UserDTO payload,
                         BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        return this.userService.updateUser(currentUser.getId(), payload);
    }

    // This method is used to delete the current user
    @DeleteMapping("/me")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('MODERATOR') || hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMe(@AuthenticationPrincipal User currentUser) {
        this.userService.deleteUser(currentUser.getId());
    }

}
