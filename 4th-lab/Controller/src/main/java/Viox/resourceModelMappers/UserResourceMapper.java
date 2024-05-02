package Viox.resourceModelMappers;

import Viox.dtos.user.UserCreationDto;
import Viox.dtos.user.UserIdDto;
import Viox.dtos.user.UserResponseDto;
import Viox.resourceModels.cat.CatIdResource;
import Viox.resourceModels.user.UserCreationResource;
import Viox.resourceModels.user.UserIdResource;
import Viox.resourceModels.user.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserResourceMapper {

    private final CatResourceMapper catResourceMapper;

    @Autowired
    public UserResourceMapper(CatResourceMapper catResourceMapper) {
        this.catResourceMapper = catResourceMapper;
    }

    public UserIdResource toUserIdResource(UserIdDto userIdDto) {
        return new UserIdResource(
                userIdDto.id(),
                userIdDto.name(),
                userIdDto.dateOfBirth()
        );
    }

    public UserResource toUserResource(UserResponseDto userResponseDto) {

        List<CatIdResource> catIdResources = userResponseDto.ownedCats()
                .stream()
                .map(catResourceMapper::toCatIdResource)
                .toList();

        return new UserResource(
                userResponseDto.id(),
                userResponseDto.name(),
                userResponseDto.dateOfBirth(),
                catIdResources
        );
    }

    public UserCreationDto toUserCreationDto(UserCreationResource userCreationResource) {
        return new UserCreationDto(
                userCreationResource.name(),
                userCreationResource.password(),
                userCreationResource.dateOfBirth()
        );
    }
}