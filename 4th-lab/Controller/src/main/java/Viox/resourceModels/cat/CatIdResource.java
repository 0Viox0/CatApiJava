package Viox.resourceModels.cat;

import Viox.models.CatColor;

public record CatIdResource(
        Long id,
        String name,
        CatColor color,
        String breed
) { }
