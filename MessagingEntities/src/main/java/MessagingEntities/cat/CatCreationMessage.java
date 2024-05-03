package MessagingEntities.cat;

import java.time.LocalDate;

public record CatCreationMessage (
        String name,
        LocalDate dateOfBirth,
        CatColorMessage color,
        String breed) {  }