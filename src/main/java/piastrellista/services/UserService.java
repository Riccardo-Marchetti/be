package piastrellista.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import piastrellista.entities.User;
import piastrellista.enums.UserRole;
import piastrellista.exceptions.BadRequestException;
import piastrellista.exceptions.NotFoundException;
import piastrellista.mailgun.MailgunSender;
import piastrellista.payloads.UserDTO;
import piastrellista.repositories.UserDAO;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private MailgunSender mailgunSender;

    // Returns a page of user
    public Page<User> getAllUsers(int page, int size, String sortBy){
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.userDAO.findAll(pageable);
    }

    // Returns a user from its id
    public User getUserById (long id){
        return this.userDAO.findById(id).orElseThrow(() -> new NotFoundException("User " + id + " has not been found"));

    }

    // Saves a new user to the database
    public User saveUser (UserDTO payload){
        if (this.userDAO.existsByUsernameAndEmail(payload.username(), payload.email()))
            throw new BadRequestException("Email " + payload.email() + " and Username " + payload.username() + " are already taken");
        if (this.userDAO.existsByEmail(payload.email()))
            throw new BadRequestException("Email " + payload.email() + " is already taken");
        if (this.userDAO.existsByUsername(payload.username()))
            throw new BadRequestException("Username " + payload.username() + " is already taken");
        User users = new User( payload.username(),  payload.email(), bcrypt.encode(payload.password()) );
//        users.setAvatar("https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.surname());
        mailgunSender.sendRegistrationEmail(users);
        return this.userDAO.save(users);
    }

    // Updates a user in the database
    public User updateUser (long id, UserDTO payload){
        User user = this.userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        if (user.getEmail().equals(payload.email())) {
            if (user.getUsername().equals(payload.username())) {
                user.setPassword(encoder.encode(payload.password()));
            } else if (!this.userDAO.existsByUsername(payload.username())) {
                user.setUsername(payload.username());
                user.setPassword(encoder.encode(payload.password()));
            } else throw new BadRequestException("Username " + payload.username() + " is already taken");
            this.userDAO.save(user);
            return user;
        } else throw new BadRequestException("You are not allowed to change the email without permission");
    }

    // Deletes a user from the database
    public void deleteUser (long id){
        User user = this.userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        this.userDAO.delete(user);
    }

    // Find user by email
    public User findByEmail(String email) {
        return this.userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Email " + email + " has not been found"));
    }

    // Checks if a User exists by username
    public User existsByUsername(String name, String surname, String username, String email, String password){
        User user = this.userDAO.findByUsername(username);
        if (user == null){
            user = new User(username, email, bcrypt.encode(password), UserRole.ADMIN);
            this.userDAO.save(user);
        }
        return user;
    }

    // Uploads an image
    public User uploadImage(MultipartFile image, long id) throws IOException {
        User user = this.userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        String avatar = (String) cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap()).get("url");
        return this.userDAO.save(user);
    }
}
