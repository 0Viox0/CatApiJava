package userMicroservice.dtos;

import java.time.LocalDate;

public record UserIdDto (
        Long id,
        String name,
        LocalDate dateOfBirth
) {}