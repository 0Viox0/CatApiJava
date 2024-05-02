package Viox.controllers;

import Viox.dtos.cat.CatCreationDto;
import Viox.resourceModelMappers.CatResourceMapper;
import Viox.resourceModels.cat.CatCreationResource;
import Viox.resourceModels.cat.CatIdResource;
import Viox.resourceModels.cat.CatResource;
import Viox.services.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cats")
public class CatController {

    private final CatService catService;
    private final CatResourceMapper catResourceMapper;

    @Autowired
    public CatController(
            CatService catService,
            CatResourceMapper catResourceMapper
    ) {
        this.catService = catService;
        this.catResourceMapper = catResourceMapper;
    }

    @GetMapping
    public List<CatIdResource> getAllCats(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String breed
    ) {
        return catService.getAllCats(color, breed)
                .stream()
                .map(catResourceMapper::toCatIdResource)
                .toList();
    }

    @GetMapping("/{id}")
    public CatResource getCatById(@PathVariable("id") Long id) {
        return catResourceMapper.toCatResource(catService.getCatById(id));
    }

    @PostMapping
    public CatIdResource createCat(@RequestBody CatCreationResource catCreationResource) {
        CatCreationDto catCreationDto = catResourceMapper.toCatCreationDto(catCreationResource);

        return catResourceMapper.toCatIdResource(catService.createCat(catCreationDto));
    }

    @DeleteMapping("/{id}")
    public void deleteCatById(@PathVariable("id") Long id) {
        catService.removeCat(id);
    }

    @PutMapping("/{catId}/friendsWithCat/{friendId}")
    public CatResource makeFriends(
            @PathVariable("catId") Long catId,
            @PathVariable("friendId") Long friendId
    ) {
        return catResourceMapper.toCatResource(catService.befriendCats(catId, friendId));
    }

    @DeleteMapping("/{catId}/friendsWithCat/{friendId}")
    public CatResource deleteFriends(
            @PathVariable("catId") Long catId,
            @PathVariable("friendId") Long friendId
    ) {
        return catResourceMapper.toCatResource(catService.unfriendCats(catId, friendId));
    }
}