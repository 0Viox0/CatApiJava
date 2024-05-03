package Api.models.mappers;

import Api.models.cat.CatColor;
import Api.models.cat.CatIdResource;
import Api.models.user.UserResource;
import MessagingEntities.cat.CatIdMessageRes;
import MessagingEntities.user.UserMessageRes;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceMessageMapper {

    public CatIdResource toCatIdResource(CatIdMessageRes cat) {
        return new CatIdResource(
                cat.id(),
                cat.name(),
                CatColor.fromString(cat.color().toString()),
                cat.breed()
        );
    }

    public UserResource toUserResource(
            UserMessageRes userMessageRes,
            List<CatIdMessageRes> catIdMessageRes
    ) {
        return new UserResource(
                userMessageRes.id(),
                userMessageRes.name(),
                userMessageRes.dateOfBirth(),
                catIdMessageRes.stream()
                        .filter((cat) -> cat.ownerId() == userMessageRes.id())
                        .map(this::toCatIdResource)
                        .toList()
        );
    }
}
