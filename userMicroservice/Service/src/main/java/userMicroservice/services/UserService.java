package userMicroservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import userMicroservice.customExceptions.UserNotFoundException;
import userMicroservice.dtoMapper.UserDtoMapper;
import userMicroservice.dtos.UserCreationDto;
import userMicroservice.dtos.UserIdDto;
import userMicroservice.dtos.UserResponseDto;
import userMicroservice.dtos.UserSecurityDto;
import userMicroservice.models.User;
import userMicroservice.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserDtoMapper userDtoMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            UserRepository userRepository,
            UserDtoMapper userDtoMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserIdDto addUser(UserCreationDto userCreationDto) {
        User user = userDtoMapper.toUser(userCreationDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        return userDtoMapper.toUserIdDto(userRepository.save(user));
    }

    public UserSecurityDto getUserByName(String username)
            throws UserNotFoundException {
        Optional<User> user = userRepository.findByName(username);

        if (user.isEmpty()) {
            throw new UserNotFoundException(username);
        }

        return userDtoMapper.toUserSecurityDto(user.get());
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
            throws UserNotFoundException {
        User user = this.getUserEntityById(userId);

        user.addCatId(catId);

        userRepository.save(user);

        return userDtoMapper.toUserResponseDto(user);
    }

    public UserResponseDto deleteCat(Long userId, Long catId)
            throws UserNotFoundException {
        User user = this.getUserEntityById(userId);

        user.removeCatId(catId);

        userRepository.save(user);

        return userDtoMapper.toUserResponseDto(user);
    }

    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + id));
    }
}