package pierpaolo.colasante.u5w2d5project.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private DeviceStatusType statusType;
    private String typologies;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
