package Api.models.cat;

public record CatIdResource(
    Long id,
    String name,
    CatColor color,
    String breed) {  }
