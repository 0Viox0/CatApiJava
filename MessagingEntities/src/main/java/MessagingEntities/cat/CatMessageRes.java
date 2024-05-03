package MessagingEntities.cat;

import java.time.LocalDate;
import java.util.List;

public record CatMessageRes (
    Long id,
    String name,
    CatColorMessage color,
    String breed,
    LocalDate dateOfBirth,
    List<CatIdMessageRes> friends,
    Long ownerId) { }
