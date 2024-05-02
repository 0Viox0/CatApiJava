package Viox.dtos.user;

import Viox.dtos.cat.CatIdDto;

import java.time.LocalDate;
import java.util.List;

public record UserResponseDto (
        Long id,
        String name,
        LocalDate dateOfBirth,
        List<CatIdDto> ownedCats
) {}