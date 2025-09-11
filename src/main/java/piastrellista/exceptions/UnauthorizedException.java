package piastrellista.exceptions;

// This class represents a custom exception for unauthorized access
public class UnauthorizedException extends RuntimeException{

    // Constructor that takes a message as input
    public UnauthorizedException(String message){
        super(message);
    }
}
