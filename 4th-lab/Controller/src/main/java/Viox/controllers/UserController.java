package Viox.controllers;

import Viox.dtos.user.UserCreationDto;
import Viox.resourceModelMappers.UserResourceMapper;
import Viox.resourceModels.user.UserCreationResource;
import Viox.resourceModels.user.UserIdResource;
import Viox.resourceModels.user.UserResource;
import Viox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserResourceMapper userResourceMapper;

    @Autowired
    public UserController(
            UserService userService,
            UserResourceMapper userResourceMapper
    ) {
        this.userService = userService;
        this.userResourceMapper = userResourceMapper;
    }

    @GetMapping
    @PostFilter("hasRole('ADMIN') or (filterObject.id == principal.id and hasRole('USER'))")
    public List<UserIdResource> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(userResourceMapper::toUserIdResource)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == principal.id)")
    public UserResource getUserById(@PathVariable("id") Long id) {
        return userResourceMapper.toUserResource(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable("id") Long id) {
        userService.deleteById(id);
    }

    @PostMapping
    public UserIdResource createUser(@RequestBody UserCreationResource userCreationResource) {
        UserCreationDto userCreationDto = userResourceMapper.toUserCreationDto(userCreationResource);

        return userResourceMapper.toUserIdResource(userService.addUser(userCreationDto));
    }

    @PutMapping("{userId}/cat/{catId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == principal.id)")
    public UserResource assignCatToUser(
            @PathVariable("userId") Long userId,
            @PathVariable("catId") Long catId
    ) {
        return userResourceMapper.toUserResource(userService.addCat(userId, catId));
    }

    @DeleteMapping("{userId}/cat/{catId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == principal.id)")
    public UserResource deleteCatFromUser(
            @PathVariable("userId") Long userId,
            @PathVariable("catId") Long catId
    ) {
        return userResourceMapper.toUserResource(userService.deleteCat(userId, catId));
    }
}