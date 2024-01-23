package pierpaolo.colasante.u5w2d5project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pierpaolo.colasante.u5w2d5project.entities.User;
import pierpaolo.colasante.u5w2d5project.exceptions.UnauthorizedException;
import pierpaolo.colasante.u5w2d5project.payloads.UserLoginDTO;
import pierpaolo.colasante.u5w2d5project.security.JWTtools;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTtools jwTtools;

    public String authenticateUser(UserLoginDTO body){
        // 1. Verifichiamo che l'email dell'utente sia nel db
        User user = userService.findByEmail(body.email());
        // 2. In caso affermativo, verifichiamo se la password fornita corrisponde a quella trovata nel db
        if(body.password().equals(user.getPassword())){
            // 3. Se le credenziali sono OK --> Genere un token JWT e lo ritorno
            return jwTtools.createToken(user);

        }else{
            // 4. Se le credenziali NON sono OK --> 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali non valide!!");
        }

    }
}
