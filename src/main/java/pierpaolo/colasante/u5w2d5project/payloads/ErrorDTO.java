package pierpaolo.colasante.u5w2d5project.payloads;

import java.time.LocalDateTime;

public record ErrorDTO(String message, LocalDateTime time) {
}
