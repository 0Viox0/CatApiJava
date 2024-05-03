package MessagingEntities.user;

import java.time.LocalDate;

public record UserIdMessageRes(
        Long id,
        String name,
        LocalDate dateOfBirth
) { }
