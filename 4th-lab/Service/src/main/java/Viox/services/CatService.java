package Viox.services;

import Viox.customExceptions.CatNotFoundException;
import Viox.customExceptions.FriendsWithSelfException;
import Viox.customExceptions.InvalidCatColorException;
import Viox.dtoMapper.CatDtoMapper;
import Viox.dtos.cat.CatCreationDto;
import Viox.dtos.cat.CatIdDto;
import Viox.dtos.cat.CatResponseDto;
import Viox.models.Cat;
import Viox.models.CatColor;
import Viox.repositories.CatRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
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

    @PostAuthorize("hasRole('ADMIN') or (hasRole('USER') and returnObject.ownerId == principal.id)")
    public CatResponseDto getCatById(Long id) throws CatNotFoundException {
        return catDtoMapper.toCatResponseDto(this.getCatEntityById(id));
    }

    @PostFilter("hasRole('ADMIN') or filterObject.ownerId == principal.id")
    public List<CatIdDto> getAllCats(
            @Nullable String catColor,
            @Nullable String breed
    ) {
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

        catRepository.saveAll(List.of(cat1, cat2));

        return catDtoMapper.toCatResponseDto(cat1);
    }

    public CatResponseDto unfriendCats(Long catId1, Long catId2)
            throws CatNotFoundException {
        Cat cat1 = this.getCatEntityById(catId1);
        Cat cat2 = this.getCatEntityById(catId2);

        cat1.removeFriend(cat2);

        catRepository.saveAll(List.of(cat1, cat2));

        return catDtoMapper.toCatResponseDto(cat1);
    }

    public void removeCat(Long id) {
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