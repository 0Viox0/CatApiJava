package Api.securityConfig.useDetailsConfig;

import Api.exceptionHandlers.MessageExceptionHandler;
import Api.senders.UserSender;
import MessagingEntities.MessageModel;
import MessagingEntities.factories.MessageModelFactory;
import MessagingEntities.user.UserSecurityDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserSender userSender;
    private final ObjectMapper objectMapper;
    private final MessageExceptionHandler messageExceptionHandler;

    @Autowired
    public CustomUserDetailService(
            UserSender userSender,
            @Qualifier("objectMapper") ObjectMapper objectMapper,
            MessageExceptionHandler messageExceptionHandler) {
        this.userSender = userSender;
        this.objectMapper = objectMapper;
        this.messageExceptionHandler = messageExceptionHandler;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        try {
            MessageModel message = MessageModelFactory.getRegularMessage();

            message.setOperation("getSecurityUserByName");
            message.setHeaders(new HashMap<>() {{
                put("Username", username);
            }});

            MessageModel response = userSender.sendMessage(message);

            messageExceptionHandler.checkMessageForExceptions(response);

            UserSecurityDetails userSecurityDetails = objectMapper.readValue(
                    response.getPayload().get("User").toString(),
                    UserSecurityDetails.class
            );

            return new CustomUserDetails(userSecurityDetails);
        } catch (JsonProcessingException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}