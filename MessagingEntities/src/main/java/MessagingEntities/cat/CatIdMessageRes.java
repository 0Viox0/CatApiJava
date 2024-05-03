package MessagingEntities.cat;

import java.time.LocalDate;

public record CatIdMessageRes(
        Long id,
        String name,
        CatColorMessage color,
        String breed,
        LocalDate dateOfBirth,
        Long ownerId) {
}
