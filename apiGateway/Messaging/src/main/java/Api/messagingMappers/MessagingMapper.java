package Api.messagingMappers;

import Api.models.cat.CatCreationResource;
import MessagingEntities.cat.CatColorMessage;
import MessagingEntities.cat.CatCreationMessage;
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

    @SneakyThrows
    public String toJson(Object value) {
        return objectMapper.writeValueAsString(value);
    }
}