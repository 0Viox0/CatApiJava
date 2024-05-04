package userMicroservice.dtos;

import java.util.List;

public record UserSecurityDto (
        Long id,
        String username,
        String password,
        boolean enabled,
        List<String> authorities) { }
