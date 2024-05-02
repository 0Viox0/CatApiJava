package Api.controllers;

import MessagingEntities.CatIdMessageRes;
import org.springframework.web.bind.annotation.*;
import Api.senders.*;

import java.util.List;

@RestController
@RequestMapping("/cats")
public class CatController {

    private final CatSender catSender;

    public CatController(CatSender catSender) {
        this.catSender = catSender;
    }

    @GetMapping
    public List<CatIdMessageRes> getAllCats(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String breed) {

        List<CatIdMessageRes> result = catSender.sendMessage(color, breed);

        return result;
    }

    // @GetMapping("/{id}")
    // public CatResource getCatById(@PathVariable("id") Long id) {
    // return catResourceMapper.toCatResource(catService.getCatById(id));
    // }
    //
    // @PostMapping
    // public CatIdResource createCat(@RequestBody CatCreationResource
    // catCreationResource) {
    // CatCreationDto catCreationDto =
    // catResourceMapper.toCatCreationDto(catCreationResource);
    //
    // return
    // catResourceMapper.toCatIdResource(catService.createCat(catCreationDto));
    // }
    //
    // @DeleteMapping("/{id}")
    // public void deleteCatById(@PathVariable("id") Long id) {
    // catService.removeCat(id);
    // }
    //
    // @PutMapping("/{catId}/friendsWithCat/{friendId}")
    // public CatResource makeFriends(
    // @PathVariable("catId") Long catId,
    // @PathVariable("friendId") Long friendId) {
    // return catResourceMapper.toCatResource(catService.befriendCats(catId,
    // friendId));
    // }
    //
    // @DeleteMapping("/{catId}/friendsWithCat/{friendId}")
    // public CatResource deleteFriends(
    // @PathVariable("catId") Long catId,
    // @PathVariable("friendId") Long friendId) {
    // return catResourceMapper.toCatResource(catService.unfriendCats(catId,
    // friendId));
    // }
}
