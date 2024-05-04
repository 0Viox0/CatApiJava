package Viox.services;

import Viox.customExceptions.CatNotFoundException;
import Viox.customExceptions.FriendsWithSelfException;
import Viox.customExceptions.InvalidCatColorException;
import Viox.dtoMapper.CatDtoMapper;
import Viox.dtos.CatCreationDto;
import Viox.dtos.CatIdDto;
import Viox.dtos.CatResponseDto;
import Viox.models.Cat;
import Viox.models.CatColor;
import Viox.repositories.CatRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatService {

    private final CatRepository catRepository;
    private final CatDtoMapper catDtoMapper;

    @Autowired
    public CatService(CatRepository catRepository, CatDtoMapper catDtoMapper) {
        this.catRepository = catRepository;
        this.catDtoMapper = catDtoMapper;
    }

    public CatResponseDto getCatById(Long id) throws CatNotFoundException {
        return catDtoMapper.toCatResponseDto(this.getCatEntityById(id));
    }

    public List<CatIdDto> getAllCats(
            @Nullable String catColor,
            @Nullable String breed) throws InvalidCatColorException {
        try {
            return catRepository.findAll().stream()
                    .filter(cat -> catColor == null || cat.getColor() == CatColor.fromString(catColor))
                    .filter(cat -> breed == null || cat.getBreed().equals(breed))
                    .map(catDtoMapper::toCatIdDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException ex) {
            throw new InvalidCatColorException("Cat " + catColor.toUpperCase() + " doesn't exist");
        }
    }

    public CatIdDto createCat(CatCreationDto catCreationDto) {
        Cat cat = catDtoMapper.toCat(catCreationDto);
        return catDtoMapper.toCatIdDto(catRepository.save(cat));
    }

    public CatResponseDto befriendCats(Long catId1, Long catId2)
            throws CatNotFoundException, FriendsWithSelfException {
        Cat cat1 = this.getCatEntityById(catId1);
        Cat cat2 = this.getCatEntityById(catId2);

        if (cat1.getId() == cat2.getId()) {
            throw new FriendsWithSelfException("Cat can't be friends with itself :)");
        }

        cat1.addFriend(cat2);
        cat2.addFriend(cat1);

        catRepository.saveAll(List.of(cat1, cat2));

        return catDtoMapper.toCatResponseDto(cat1);
    }

    public CatResponseDto unfriendCats(Long catId1, Long catId2)
            throws CatNotFoundException {
        Cat cat1 = this.getCatEntityById(catId1);
        Cat cat2 = this.getCatEntityById(catId2);

        cat1.removeFriend(cat2);
        cat2.removeFriend(cat1);

        catRepository.saveAll(List.of(cat1, cat2));

        return catDtoMapper.toCatResponseDto(cat1);
    }

    public CatIdDto addOwner(Long catId, Long ownerId)
            throws CatNotFoundException {
        Cat cat = this.getCatEntityById(catId);

        cat.setOwnerId(ownerId);

        catRepository.save(cat);

        return catDtoMapper.toCatIdDto(cat);
    }

    public CatIdDto removeOwner(Long catId)
            throws CatNotFoundException {
        Cat cat = this.getCatEntityById(catId);

        cat.setOwnerId(null);

        catRepository.save(cat);

        return catDtoMapper.toCatIdDto(cat);
    }

    public void removeCat(Long id)
            throws CatNotFoundException{
        if (catRepository.findById(id).isEmpty()) {
            throw new CatNotFoundException("Cat not found for id: " + id);
        }

        catRepository.deleteById(id);
    }

    public Cat getCatEntityById(Long id) {
        return catRepository.findById(id)
                .orElseThrow(() -> new CatNotFoundException("Cat not found for id: " + id));
    }

}
