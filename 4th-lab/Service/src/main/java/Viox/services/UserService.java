package Viox.services;

import Viox.customExceptions.CatNotFoundException;
import Viox.customExceptions.UserNotFoundException;
import Viox.dtoMapper.UserDtoMapper;
import Viox.dtos.user.UserCreationDto;
import Viox.dtos.user.UserIdDto;
import Viox.dtos.user.UserResponseDto;
import Viox.models.Cat;
import Viox.models.User;
import Viox.repositories.CatRepository;
import Viox.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CatRepository catRepository;

    private final CatService catService;
    private final UserDtoMapper userDtoMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            UserRepository userRepository,
            CatRepository catRepository,
            CatService catService,
            UserDtoMapper userDtoMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.catRepository = catRepository;
        this.catService = catService;
        this.userDtoMapper = userDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserIdDto addUser(UserCreationDto userCreationDto) {
        User user = userDtoMapper.toUser(userCreationDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        return userDtoMapper.toUserIdDto(userRepository.save(user));
    }

    public UserResponseDto getUserById(Long id) throws UserNotFoundException {
        return userDtoMapper.toUserResponseDto(this.getUserEntityById(id));
    }

    public List<UserIdDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userDtoMapper::toUserIdDto)
                .toList();
    }

    public void deleteById(Long id) throws UserNotFoundException {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("User not found for id: " + id);
        }

        userRepository.deleteById(id);
    }

    public UserResponseDto addCat(Long userId, Long catId)
            throws UserNotFoundException, CatNotFoundException {
        User user = this.getUserEntityById(userId);
        Cat cat = catService.getCatEntityById(catId);

        user.addCat(cat);
        cat.setOwner(user);

        userRepository.save(user);
        catRepository.save(cat);

        return userDtoMapper.toUserResponseDto(user);
    }

    public UserResponseDto deleteCat(Long userId, Long catId)
            throws UserNotFoundException, CatNotFoundException {
        User user = this.getUserEntityById(userId);
        Cat cat = catService.getCatEntityById(catId);

        user.removeCat(cat);
        cat.setOwner(null);

        userRepository.save(user);
        catRepository.save(cat);

        return userDtoMapper.toUserResponseDto(user);
    }

    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + id));
    }
}