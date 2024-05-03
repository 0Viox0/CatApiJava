package userMicroservice.dtos;

import java.time.LocalDate;
import java.util.List;

public record UserResponseDto (
        Long id,
        String name,
        LocalDate dateOfBirth,
        List<Long> ownedCatsIds
) {}