package pierpaolo.colasante.u5w2d5project.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DeviceDTO(
        @NotEmpty(message = "statusType è un campo obbligatorio!")
        String statusType,
        @NotEmpty(message = "typologies è un campo obbligatorio!")
        String typologies,
        @NotNull(message = "userId è un campo obbligatorio!")
        int userId
) {
}
