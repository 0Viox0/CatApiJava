package Viox.resourceModelMappers;

import Viox.dtos.cat.CatCreationDto;
import Viox.dtos.cat.CatIdDto;
import Viox.dtos.cat.CatResponseDto;
import Viox.resourceModels.cat.CatCreationResource;
import Viox.resourceModels.cat.CatResource;
import Viox.resourceModels.cat.CatIdResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatResourceMapper {

    public CatCreationDto toCatCreationDto(CatCreationResource catCreationResource) {
        return new CatCreationDto(
                catCreationResource.name(),
                catCreationResource.dateOfBirth(),
                catCreationResource.color(),
                catCreationResource.breed()
        );
    }

    public CatResource toCatResource(CatResponseDto catResponseDto) {
        List<CatIdResource> catIdResources = catResponseDto.friends()
                .stream()
                .map(this::toCatIdResource)
                .toList();

        return new CatResource(
                catResponseDto.id(),
                catResponseDto.name(),
                catResponseDto.color(),
                catResponseDto.breed(),
                catResponseDto.dateOfBirth(),
                catIdResources
        );
    }

    public CatIdResource toCatIdResource(CatIdDto catIdDto) {
        return new CatIdResource(
                catIdDto.id(),
                catIdDto.name(),
                catIdDto.color(),
                catIdDto.breed()
        );
    }
}