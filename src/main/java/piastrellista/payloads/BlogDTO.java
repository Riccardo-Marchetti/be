package piastrellista.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record BlogDTO(@NotEmpty(message = "Title is mandatory")
                      String title,
                      @NotEmpty(message = "Preview image url is mandatory")
                      String previewImageUrl,
                      @NotEmpty(message = "Description is mandatory")
                      String description,
                      @NotNull(message = "Creation date is mandatory")
                      LocalDate creationDate,
                      @NotNull(message = "Texts is mandatory")
                      List<String> texts,
                      @NotNull(message = "Images url is mandatory")
                      List<String> imagesUrl
) {
}
