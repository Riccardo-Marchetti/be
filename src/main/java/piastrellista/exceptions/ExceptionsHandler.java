package piastrellista.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import piastrellista.payloads.ErrorsDTO;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
// This class handles exceptions and returns appropriate responses
public class ExceptionsHandler {

    // This method handles BadRequestException
    @ExceptionHandler
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ErrorsDTO handleBadRequest (BadRequestException ex){
        if (ex.getErrorList() != null){
            String message = ex.getErrorList().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(". "));
            return new ErrorsDTO(message, LocalDateTime.now());
        }
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    // This method handles NotFoundException
    @ExceptionHandler
    @ResponseStatus (HttpStatus.NOT_FOUND)
    public ErrorsDTO handleNotFoundException (NotFoundException ex){
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    // This method handles general exceptions
    @ExceptionHandler
    @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsDTO handleException (Exception ex){
        return new ErrorsDTO("Server related problem, we will resolve as soon as possible", LocalDateTime.now());
    }

    // This method handles UnauthorizedException
    @ExceptionHandler
    @ResponseStatus (HttpStatus.UNAUTHORIZED)
    public ErrorsDTO handleException (UnauthorizedException ex){
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    // This method handles AccessDeniedException
    @ExceptionHandler
    @ResponseStatus (HttpStatus.FORBIDDEN)
    public ErrorsDTO handleException (AccessDeniedException ex){
        return new ErrorsDTO("You do not have access to this feature!", LocalDateTime.now());
    }
}