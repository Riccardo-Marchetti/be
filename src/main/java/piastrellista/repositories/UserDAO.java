package piastrellista.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import piastrellista.entities.User;

import java.util.Optional;

// This interface is a repository for User entities
@Repository
public interface UserDAO extends JpaRepository<User, Long> {

    // This method find user by email
    Optional<User> findByEmail (String email);

    // This method checks if a user exists by their username
    boolean existsByUsername (String username);

    // This method checks if a user exists from their email
    boolean existsByEmail (String email);

    // This method find user by username
    User findByUsername (String username);

    // This method checks if a user exists by his username and email
    boolean existsByUsernameAndEmail(String username, String email);
}
