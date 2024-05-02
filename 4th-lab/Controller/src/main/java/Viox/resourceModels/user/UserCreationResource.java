package Viox.resourceModels.user;

import java.time.LocalDate;

public record UserCreationResource(
        String name,
        String password,
        LocalDate dateOfBirth
) { }