package pierpaolo.colasante.u5w2d5project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String name;
    private String lastName;
    private String email;
    private String avatar;
    private String password;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Device> device;
}
