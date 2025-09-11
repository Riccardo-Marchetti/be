package piastrellista.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import piastrellista.entities.User;
import piastrellista.exceptions.UnauthorizedException;
import piastrellista.payloads.UserLoginDTO;
import piastrellista.security.JWTTools;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    // This method checks the user's credentials and generates a token
    public String authenticationUserAndGenerateToken (UserLoginDTO payload){
        // 1 check the credentials
        // 1.1 search in the db for the user by email
        User user = userService.findByEmail(payload.email());

        // 1.2 verify if the password matches the one received in the payload
        if (bcrypt.matches(payload.password(), user.getPassword())) {
            // 2 if everything is ok, generate a token and return it
           return jwtTools.createToken(user);
        } else {
            // 3 otherwise throw an exception
            throw new UnauthorizedException("Incorrect credentials! Log in again");
        }
    }
}
