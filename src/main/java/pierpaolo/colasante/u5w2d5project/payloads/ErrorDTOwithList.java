package pierpaolo.colasante.u5w2d5project.payloads;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorDTOwithList(
        String message,
        LocalDateTime timestamp,
        List<String> errorsList
) {
}
