package Viox.dtos.user;

import java.time.LocalDate;

public record UserCreationDto (
        String name,
        String password,
        LocalDate dateOfBirth
) {}