package pierpaolo.colasante.u5w2d5project.payloads;

public record UserLoginDTO(String email, String password) {
    //l'ideale è usare username anzichè email
}
