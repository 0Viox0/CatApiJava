package Api.models.user;

import java.time.LocalDate;
import java.util.List;

import Api.models.cat.CatIdResource;

public record UserResource(
    Long id,
    String name,
    LocalDate dateOfBirth,
    List<CatIdResource> ownedCats) {  }
