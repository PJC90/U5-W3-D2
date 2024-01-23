package pierpaolo.colasante.u5w2d5project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public List<User> getUser(){return userService.getUser();}
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
    @GetMapping("/{id}")
    public User findById(@PathVariable int id){return userService.findById(id);}
    @PutMapping("/{id}")
    public User findIdUpdate(@PathVariable int id, @RequestBody User body){
        return this.userService.findByIdAndUpdate(id, body);
    }
    @DeleteMapping("/{id}")
    public void findIdDelete(@PathVariable int id){ this.userService.findByIdAndDelete(id);}

    @PostMapping("/{id}/upload")
    public User uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable int id)  {
        try {
            return userService.uploadPicture(id,file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
