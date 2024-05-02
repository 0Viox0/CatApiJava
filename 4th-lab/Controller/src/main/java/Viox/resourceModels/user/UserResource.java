package Viox.resourceModels.user;

import java.time.LocalDate;
import java.util.List;

import Viox.resourceModels.cat.CatIdResource;

public record UserResource (
        Long id,
        String name,
        LocalDate dateOfBirth,
        List<CatIdResource> ownedCats
) { }
