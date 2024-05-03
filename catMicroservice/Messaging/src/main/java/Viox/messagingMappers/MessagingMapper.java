package Viox.messagingMappers;

import MessagingEntities.cat.CatColorMessage;
import MessagingEntities.cat.CatCreationMessage;
import MessagingEntities.cat.CatIdMessageRes;
import MessagingEntities.cat.CatMessageRes;
import Viox.dtos.CatCreationDto;
import Viox.dtos.CatIdDto;
import Viox.dtos.CatResponseDto;
import Viox.models.CatColor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessagingMapper {

    public CatIdMessageRes toCatIdMessage(CatIdDto cat) {
        return new CatIdMessageRes(
                cat.id(),
                cat.name(),
                CatColorMessage.fromString(cat.color().toString()),
                cat.breed(),
                cat.dateOfBirth(),
                cat.ownerId());
    }

    public CatMessageRes toCatMessage(CatResponseDto catResponseDto) {

        List<CatIdMessageRes> friends = catResponseDto
                .friends()
                .stream()
                .map(this::toCatIdMessage)
                .toList();

        return new CatMessageRes(
                catResponseDto.id(),
                catResponseDto.name(),
                CatColorMessage.fromString(catResponseDto.color().toString()),
                catResponseDto.breed(),
                catResponseDto.dateOfBirth(),
                friends,
                catResponseDto.ownerId());
    }

    public CatCreationDto toCatCreationDto(CatCreationMessage catCreationMessage) {
        return new CatCreationDto(
                catCreationMessage.name(),
                catCreationMessage.dateOfBirth(),
                CatColor.fromString(catCreationMessage.color().toString()),
                catCreationMessage.breed()
        );
    }
}