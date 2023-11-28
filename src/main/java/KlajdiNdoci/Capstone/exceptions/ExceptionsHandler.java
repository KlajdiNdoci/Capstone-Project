package KlajdiNdoci.Capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponseWithListDTO handleBadRequest(BadRequestException e) {
        if (e.getErrorList() != null) {
            List<String> errorsList = e.getErrorList().stream().map(ObjectError::getDefaultMessage).toList();
            return new ErrorsResponseWithListDTO(e.getMessage(), new Date(), errorsList);
        } else {
            return new ErrorsResponseWithListDTO(e.getMessage(), new Date(), new ArrayList<>());
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsResponseDTO handleNotFound(NotFoundException e) {
        return new ErrorsResponseDTO(e.getMessage(), new Date());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsResponseDTO handleUnauthorized(UnauthorizedException e) {
        return new ErrorsResponseDTO(e.getMessage(), new Date());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsResponseDTO handleGenericError(Exception e) {
        return new ErrorsResponseDTO("server error", new Date());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponseDTO handleMultipart(MultipartException e) {
        return new ErrorsResponseDTO(e.getMessage(), new Date());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorsResponseDTO handleAccessDenied(AccessDeniedException e) {
        return new ErrorsResponseDTO(e.getMessage(), new Date());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponseDTO handleNotFound(HttpMessageNotReadableException e) {
        if (e.getMessage().equals("Required request body is missing: public KlajdiNdoci.Capstone.entities.Game KlajdiNdoci.Capstone.controllers.GameController.createGame(KlajdiNdoci.Capstone.payloads.NewGameDTO,org.springframework.validation.BindingResult)")) {
            return new ErrorsResponseDTO("You have to enter something in the body!", new Date());
        }
        return new ErrorsResponseDTO(e.getMessage(), new Date());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsResponseDTO handleIllegalArgument(IllegalArgumentException e) {
        return new ErrorsResponseDTO(e.getMessage(), new Date());
    }
}
