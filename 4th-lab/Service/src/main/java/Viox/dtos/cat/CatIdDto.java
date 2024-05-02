package Viox.dtos.cat;

import Viox.models.CatColor;

import java.time.LocalDate;

public record CatIdDto (
        Long id,
        String name,
        CatColor color,
        String breed,
        LocalDate dateOfBirth,
        Long ownerId
) {}