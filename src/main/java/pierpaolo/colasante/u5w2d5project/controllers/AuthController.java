package pierpaolo.colasante.u5w2d5project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pierpaolo.colasante.u5w2d5project.entities.User;
import pierpaolo.colasante.u5w2d5project.exceptions.BadRequestException;
import pierpaolo.colasante.u5w2d5project.payloads.UserDTO;
import pierpaolo.colasante.u5w2d5project.payloads.UserLoginDTO;
import pierpaolo.colasante.u5w2d5project.payloads.UserLoginResponseDTO;
import pierpaolo.colasante.u5w2d5project.payloads.UserResponseDTO;
import pierpaolo.colasante.u5w2d5project.services.AuthService;
import pierpaolo.colasante.u5w2d5project.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService usersService;
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO body){
        String accessToken = authService.authenticateUser(body);
        return new UserLoginResponseDTO(accessToken);
    }
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@RequestBody @Validated UserDTO newUserPayload, BindingResult validation) {
        // Per completare la validazione devo in qualche maniera fare un controllo del tipo: se ci sono errori -> manda risposta con 400 Bad Request
        System.out.println(validation);
        if (validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException("Ci sono errori nel payload!"); // L'eccezione arriverà agli error handlers tra i quali c'è quello che manderà la risposta con status code 400
        } else {
            User newUser = usersService.save(newUserPayload);
//potrei mandare la mail di conferma di avvenuta registrazione
            return new UserResponseDTO(newUser.getId());
        }
    }
}
