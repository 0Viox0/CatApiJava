package Viox.dtoMapper;

import Viox.dtos.CatCreationDto;
import Viox.dtos.CatIdDto;
import Viox.dtos.CatResponseDto;
import Viox.models.Cat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatDtoMapper {

    public CatIdDto toCatIdDto(Cat cat) {
        return new CatIdDto(
                cat.getId(),
                cat.getName(),
                cat.getColor(),
                cat.getBreed(),
                cat.getDateOfBirth(),
                cat.getOwnerId());
    }

    public CatResponseDto toCatResponseDto(Cat cat) {
        List<CatIdDto> friends = cat.getFriends()
                .stream()
                .map(this::toCatIdDto)
                .toList();

        return new CatResponseDto(
                cat.getId(),
                cat.getName(),
                cat.getColor(),
                cat.getBreed(),
                cat.getDateOfBirth(),
                friends,
                cat.getOwnerId());
    }

    public Cat toCat(CatCreationDto catIdDto) {
        Cat cat = new Cat();

        cat.setName(catIdDto.name());
        cat.setDateOfBirth(catIdDto.dateOfBirth());
        cat.setColor(catIdDto.color());
        cat.setBreed(catIdDto.breed());

        return cat;
    }

}
