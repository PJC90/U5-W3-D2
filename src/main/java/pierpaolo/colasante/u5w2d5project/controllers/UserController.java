package pierpaolo.colasante.u5w2d5project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pierpaolo.colasante.u5w2d5project.entities.User;
import pierpaolo.colasante.u5w2d5project.exceptions.BadRequestException;
import pierpaolo.colasante.u5w2d5project.payloads.UserDTO;
import pierpaolo.colasante.u5w2d5project.payloads.UserResponseDTO;
import pierpaolo.colasante.u5w2d5project.services.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // Solo gli admin possono leggere l'elenco degli utenti
    public List<User> getUser(){return userService.getUser();}

    //non mi serve più qui perchè devi essere registarto per modificare!
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponseDTO saveUser(@RequestBody @Validated UserDTO body, BindingResult validation){
//        if(validation.hasErrors()){
//            throw new BadRequestException(validation.getAllErrors());
//        }else {
//            User newUser = userService.save(body);
//            return new UserResponseDTO(newUser.getId());
//        }
//    }
    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentUser) {
        // @AuthenticationPrincipal permette di accedere ai dati dell'utente attualmente autenticato
        // (perchè avevamo estratto l'id dal token e cercato l'utente nel db)
        return currentUser;
    }


    @PutMapping("/me")
    public User getMeAndUpdate(@AuthenticationPrincipal User currentUser, @RequestBody User body) {
        return userService.findByIdAndUpdate(currentUser.getId(), body);
    }

    @DeleteMapping("/me")
    public void getMeAnDelete(@AuthenticationPrincipal User currentUser) {
        userService.findByIdAndDelete(currentUser.getId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findById(@PathVariable int id){return userService.findById(id);}
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findIdUpdate(@PathVariable int id, @RequestBody User body){
        return this.userService.findByIdAndUpdate(id, body);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findIdDelete(@PathVariable int id){ this.userService.findByIdAndDelete(id);}

    @PostMapping("/{id}/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable int id)  {
        try {
            return userService.uploadPicture(id,file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/me/upload")
    public User uploadMeAvatar(@RequestParam("avatar") MultipartFile file, @AuthenticationPrincipal User currentUser)  {
        try {
            return userService.uploadPicture(currentUser.getId(),file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
