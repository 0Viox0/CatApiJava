package userMicroservice.messagingMappers;

import MessagingEntities.user.UserCreationMessage;
import MessagingEntities.user.UserIdMessageRes;
import MessagingEntities.user.UserMessageRes;
import org.springframework.stereotype.Component;
import userMicroservice.dtos.UserCreationDto;
import userMicroservice.dtos.UserIdDto;
import userMicroservice.dtos.UserResponseDto;

@Component
public class MessagingMapper {

    public UserIdMessageRes toUserIdMessage(UserIdDto user) {
        return new UserIdMessageRes(
                user.id(),
                user.name(),
                user.dateOfBirth()
        );
    }

    public UserMessageRes toUserMessage(UserResponseDto userResponseDto) {

        return new UserMessageRes(
                userResponseDto.id(),
                userResponseDto.name(),
                userResponseDto.dateOfBirth(),
                userResponseDto.ownedCatsIds()
        );
    }

    public UserCreationDto toUserCreationDto(UserCreationMessage userCreationMessage) {
        return new UserCreationDto(
                userCreationMessage.name(),
                userCreationMessage.password(),
                userCreationMessage.dateOfBirth()
        );
    }
}