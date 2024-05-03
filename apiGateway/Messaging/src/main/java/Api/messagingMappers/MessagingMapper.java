package Api.messagingMappers;

import Api.models.cat.CatCreationResource;
import Api.models.user.UserCreationResource;
import MessagingEntities.cat.CatColorMessage;
import MessagingEntities.cat.CatCreationMessage;
import MessagingEntities.user.UserCreationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class MessagingMapper {

    private final ObjectMapper objectMapper;

    public MessagingMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CatCreationMessage toCatCreationMessage(CatCreationResource catCreationResource) {
        return new CatCreationMessage(
                catCreationResource.name(),
                catCreationResource.dateOfBirth(),
                CatColorMessage.fromString(catCreationResource.color().toString()),
                catCreationResource.breed()
        );
    }

    public UserCreationMessage toUserCreationMessage(UserCreationResource userCreationResource) {
        return new UserCreationMessage(
                userCreationResource.name(),
                userCreationResource.password(),
                userCreationResource.dateOfBirth()
        );
    }

    @SneakyThrows
    public String toJson(Object value) {
        return objectMapper.writeValueAsString(value);
    }
}