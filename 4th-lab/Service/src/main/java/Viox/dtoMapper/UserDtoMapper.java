package Viox.dtoMapper;

import Viox.dtos.cat.CatIdDto;
import Viox.dtos.user.UserCreationDto;
import Viox.dtos.user.UserIdDto;
import Viox.dtos.user.UserResponseDto;
import Viox.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDtoMapper {

    private final CatDtoMapper catDtoMapper;

    @Autowired
    public UserDtoMapper(CatDtoMapper catDtoMapper) {
        this.catDtoMapper = catDtoMapper;
    }

    public UserIdDto toUserIdDto(User user) {
        return new UserIdDto(user.getId(), user.getName(), user.getDateOfBirth());
    }

    public UserResponseDto toUserResponseDto(User user) {
        List<CatIdDto> ownedCats = user.getCats()
                .stream()
                .map(catDtoMapper::toCatIdDto)
                .toList();

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getDateOfBirth(),
                ownedCats
        );
    }

    public User toUser(UserCreationDto userCreationDto) {
        User user = new User();

        user.setName(userCreationDto.name());
        user.setPassword(userCreationDto.password());
        user.setDateOfBirth(userCreationDto.dateOfBirth());

        return user;
    }

}