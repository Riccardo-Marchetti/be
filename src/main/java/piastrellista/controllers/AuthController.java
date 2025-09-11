package piastrellista.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import piastrellista.entities.User;
import piastrellista.exceptions.BadRequestException;
import piastrellista.payloads.UserDTO;
import piastrellista.payloads.UserLoginDTO;
import piastrellista.payloads.UserLoginRespDTO;
import piastrellista.services.AuthService;
import piastrellista.services.UserService;

@RestController
@RequestMapping ("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    //  This method is used for user login
    @PostMapping ("/login")
    public UserLoginRespDTO login(@RequestBody UserLoginDTO payload){
        return new UserLoginRespDTO(this.authService.authenticationUserAndGenerateToken(payload));
    }

    //  This method is used for user registration
    @PostMapping ("/register")
    @ResponseStatus (HttpStatus.CREATED)
    private User register (@RequestBody UserDTO payload, BindingResult validation){
        if (validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        }
        return this.userService.saveUser(payload);
    }
}
