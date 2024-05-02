package Viox.resourceModels.cat;

import Viox.models.CatColor;

import java.time.LocalDate;

public record CatCreationResource(
        String name,
        LocalDate dateOfBirth,
        CatColor color,
        String breed
) { }