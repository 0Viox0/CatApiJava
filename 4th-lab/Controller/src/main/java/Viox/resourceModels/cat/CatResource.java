package Viox.resourceModels.cat;

import Viox.models.CatColor;

import java.time.LocalDate;
import java.util.List;

public record CatResource(
        Long id,
        String name,
        CatColor color,
        String breed,
        LocalDate dateOfBirth,
        List<CatIdResource> friends
) { }
