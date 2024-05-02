package Viox.dtos.cat;

import Viox.models.CatColor;

import java.time.LocalDate;
import java.util.List;

public record CatResponseDto (
        Long id,
        String name,
        CatColor color,
        String breed,
        LocalDate dateOfBirth,
        List<CatIdDto> friends,
        Long ownerId
) { }