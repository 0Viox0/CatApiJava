package Api.models.cat;

import java.time.LocalDate;

public record CatCreationResource(
    String name,
    LocalDate dateOfBirth,
    CatColor color,
    String breed) {  }
