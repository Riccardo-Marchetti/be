package piastrellista.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private long id;
    @Getter
    private String title;
    @Getter
    private String previewImageUrl;
    @Getter
    private String description;
    @Getter
    private LocalDate creationDate;
    @Getter
    private List<String> texts = new ArrayList<>();
    @Getter
    private List<String> imagesUrl = new ArrayList<>();


    // CONSTRUCTOR
    public Blog(String title, String previewImageUrl, String description, LocalDate creationDate, List<String> texts, List<String> imagesUrl) {
        this.title = title;
        this.previewImageUrl = previewImageUrl;
        this.description = description;
        this.creationDate = creationDate;
        this.texts = texts != null ? texts : new ArrayList<>();
        this.imagesUrl = imagesUrl != null ? imagesUrl : new ArrayList<>();
    }

}
