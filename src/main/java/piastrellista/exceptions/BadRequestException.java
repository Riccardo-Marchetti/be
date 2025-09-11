package piastrellista.exceptions;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
// This class represents a custom exception for bad requests
public class BadRequestException extends RuntimeException{
    List<ObjectError> errorList;
    public BadRequestException(String message){super(message);}
    public BadRequestException (List<ObjectError> errorList){
        super("Validation Error");
        this.errorList = errorList;
    }
}