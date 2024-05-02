package Viox.resourceModels.user;

import java.time.LocalDate;

public record UserIdResource(
        Long id,
        String name,
        LocalDate dateOfBirth
) { }