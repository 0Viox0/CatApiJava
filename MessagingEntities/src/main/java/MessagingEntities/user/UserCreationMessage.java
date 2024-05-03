package MessagingEntities.user;

import java.time.LocalDate;

public record UserCreationMessage(
        String name,
        String password,
        LocalDate dateOfBirth
) { }
