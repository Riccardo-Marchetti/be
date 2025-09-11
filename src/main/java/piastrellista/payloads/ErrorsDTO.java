package piastrellista.payloads;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime dateMessage) {
}

