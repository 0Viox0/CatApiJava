package MessagingEntities.user;

import java.time.LocalDate;
import java.util.List;

public record UserMessageRes(
        Long id,
        String name,
        LocalDate dateOfBirth,
        List<Long> ownedCatsIds
) { }
