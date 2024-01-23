package pierpaolo.colasante.u5w2d5project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // Obbligatoria se vogliamo dichiarare su ogni singolo endpoint i permessi di accesso in base al ruolo tramite annotazioni @PreAuthorize
public class SecurityConfig {
    @Autowired
    private JWTAuthFilter jwtAuthFilter;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        // Disabilitiamo alcuni comportamenti di default  tra cui il login in cui la password e quella che da spring nel terminale e lo user dovrebbe essere admin
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Aggiungiamo filtri custom
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Aggiungere/Rimuovere regole di protezione su singoli endpoint
        // in maniera che venga/non venga richiesta l'autenticazione per accedervi
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers("/**").permitAll());

//        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers( "/v3/**", "/swagger-ui/**").permitAll().anyRequest().authenticated());


        return httpSecurity.build();
    }
    @Bean
    PasswordEncoder getPWEncoder() {
        return new BCryptPasswordEncoder(11);
        // 11 è il numero di ROUNDS, ovvero quante volte viene eseguito l'algoritmo. Ci serve per impostare la velocità di esecuzione
        // di BCrypt. Più è alto il numero, più lento sarà l'algoritmo, più sicure saranno le password. Di contro più è lento l'algoritmo
        // e peggiore sarà la User Experience. Bisogna trovare il giusto bilanciamento tra le 2.
        // 11 significa che l'algoritmo verrà eseguito 2^11 volte 2048 volte
    }
}
