package userMicroservice.dtoMapper;

import org.springframework.stereotype.Component;
import userMicroservice.dtos.UserCreationDto;
import userMicroservice.dtos.UserIdDto;
import userMicroservice.models.User;
import userMicroservice.dtos.UserResponseDto;

import java.util.List;

@Component
public class UserDtoMapper {

    public UserIdDto toUserIdDto(User user) {
        return new UserIdDto(user.getId(), user.getName(), user.getDateOfBirth());
    }

    public UserResponseDto toUserResponseDto(User user) {
        List<Long> ownedCats = user.getCatIds();

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