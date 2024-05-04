package MessagingEntities.user;

import java.util.List;

public record UserSecurityDetails(
        Long id,
        String username,
        String password,
        boolean enabled,
        List<String> authorities) { }