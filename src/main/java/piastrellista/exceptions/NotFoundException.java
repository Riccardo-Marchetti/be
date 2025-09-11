package piastrellista.exceptions;

// This class represents a custom exception for not found resources
public class NotFoundException extends RuntimeException{

    // Constructor that takes an id as input
    public NotFoundException (long id){super("Record with id: " + id + " has not been found!");}

    // Constructor that takes a message as input
    public NotFoundException (String message){super(message);}
}
