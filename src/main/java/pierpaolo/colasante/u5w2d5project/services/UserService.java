package pierpaolo.colasante.u5w2d5project.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pierpaolo.colasante.u5w2d5project.entities.User;
import pierpaolo.colasante.u5w2d5project.exceptions.BadRequestException;
import pierpaolo.colasante.u5w2d5project.exceptions.NotFoundException;
import pierpaolo.colasante.u5w2d5project.payloads.UserDTO;
import pierpaolo.colasante.u5w2d5project.repositories.UserDAO;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private UserDAO userDAO;
    public List<User> getUser(){return this.userDAO.findAll();}
    public User save(UserDTO body){
        userDAO.findByEmail(body.email()).ifPresent(user -> {throw new BadRequestException("email " + user.getEmail() + " già usata!!!");});
        userDAO.findByUsername(body.username()).ifPresent(user -> {throw new BadRequestException("username " + user.getUsername() + " già in uso!!!");});
        User newUser = new User();
        newUser.setName(body.name());
        newUser.setLastName(body.lastName());
        newUser.setUsername(body.username());
        newUser.setEmail(body.email());
        newUser.setPassword(body.password());
        newUser.setAvatar("https://ui-avatars.com/api/?name=" + body.name() + "+" + body.lastName());
        return userDAO.save(newUser);
    }
    public User findById(long id){
        return userDAO.findById(id).orElseThrow(()->new NotFoundException(id));
    }

    public User findByEmail(String email) throws NotFoundException{
        return userDAO.findByEmail(email).orElseThrow(()->new NotFoundException("Utente con email " + email + " non trovata!!!"));
    }
    public User findByIdAndUpdate(long id, User body){
        User found = this.findById(id);
        found.setName(body.getName());
        found.setLastName(body.getLastName());
        found.setUsername(body.getUsername());
        found.setEmail(body.getEmail());
        found.setAvatar(body.getAvatar());
        found.setPassword(body.getPassword());
        return userDAO.save(found);
    }
    public void findByIdAndDelete(int id){
        User found = this.findById(id);
        if(!found.getDevice().isEmpty()){
            throw new BadRequestException("ATTENZIONE impossibile cancellare User di id: " + id + " è associato a uno o più dispositivi.");
        }
        userDAO.delete(found);
    }
    public User uploadPicture(int id, MultipartFile file) throws IOException {
        User found = this.findById(id);
        String url = (String) cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap())
                .get("url");
        found.setAvatar(url);
        return userDAO.save(found);
    }
}
