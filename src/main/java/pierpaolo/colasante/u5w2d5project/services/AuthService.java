package pierpaolo.colasante.u5w2d5project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pierpaolo.colasante.u5w2d5project.entities.Role;
import pierpaolo.colasante.u5w2d5project.entities.User;
import pierpaolo.colasante.u5w2d5project.exceptions.BadRequestException;
import pierpaolo.colasante.u5w2d5project.exceptions.UnauthorizedException;
import pierpaolo.colasante.u5w2d5project.payloads.UserDTO;
import pierpaolo.colasante.u5w2d5project.payloads.UserLoginDTO;
import pierpaolo.colasante.u5w2d5project.repositories.UserDAO;
import pierpaolo.colasante.u5w2d5project.security.JWTtools;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTtools jwTtools;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUser(UserLoginDTO body){
        // 1. Verifichiamo che l'email dell'utente sia nel db
        User user = userService.findByEmail(body.email());
        // 2. In caso affermativo, verifichiamo se la password fornita corrisponde a quella trovata nel db
        if(bcrypt.matches(body.password(), user.getPassword())){
            // 3. Se le credenziali sono OK --> Genere un token JWT e lo ritorno
            return jwTtools.createToken(user);

        }else{
            // 4. Se le credenziali NON sono OK --> 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali non valide!!");
        }

    }
    public User save(UserDTO body){
        userDAO.findByEmail(body.email()).ifPresent(user -> {throw new BadRequestException("email " + user.getEmail() + " già usata!!!");});
        userDAO.findByUsername(body.username()).ifPresent(user -> {throw new BadRequestException("username " + user.getUsername() + " già in uso!!!");});
        User newUser = new User();
        newUser.setName(body.name());
        newUser.setLastName(body.lastName());
        newUser.setUsername(body.username());
        newUser.setEmail(body.email());
//        newUser.setPassword(body.password());
        newUser.setPassword(bcrypt.encode(body.password()));
        // ho settato la password con bcrypt
        newUser.setAvatar("https://ui-avatars.com/api/?name=" + body.name() + "+" + body.lastName());
        newUser.setRole(Role.USER);
        return userDAO.save(newUser);
    }
}
