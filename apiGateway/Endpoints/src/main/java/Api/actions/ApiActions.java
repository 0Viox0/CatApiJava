package Api.actions;

import Api.models.cat.CatColor;
import Api.models.cat.CatIdResource;
import Api.models.mappers.ResourceMessageMapper;
import Api.models.user.UserResource;
import MessagingEntities.MessageModel;
import MessagingEntities.cat.CatIdMessageRes;
import MessagingEntities.user.UserMessageRes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiActions {

    private final ObjectMapper objectMapper;
    private final ResourceMessageMapper resourceMessageMapper;

    public ApiActions(@Qualifier("objectMapper") ObjectMapper objectMapper, ResourceMessageMapper resourceMessageMapper) {
        this.objectMapper = objectMapper;
        this.resourceMessageMapper = resourceMessageMapper;
    }

    @SneakyThrows
    public UserResource getUserResourceFromResponses(
            MessageModel userResponse,
            MessageModel catResponse
    ) {
        UserMessageRes userMessageRes = objectMapper.readValue(
                userResponse.getPayload().get("User").toString(),
                UserMessageRes.class
        );
        List<CatIdMessageRes> catIdMessageRes = objectMapper.readValue(
                catResponse.getPayload().get("Cats").toString(),
                new TypeReference<List<CatIdMessageRes>>() {}
        );

        return resourceMessageMapper.toUserResource(userMessageRes, catIdMessageRes);
    }

    @SneakyThrows
    public List<CatIdMessageRes> getCatIdMessageResFromResponses(MessageModel response) {
        return objectMapper.readValue(
                response.getPayload().get("Cats").toString(),
                new TypeReference<List<CatIdMessageRes>>() {}
        );
    }
}
