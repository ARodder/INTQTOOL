package dev.roder.intqtoolbackend.Websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        //TODO: Find a way to handle thrown error when user is not of right role.
        messages
                .nullDestMatcher().authenticated()
                .simpDestMatchers("/topic/**/**").hasAnyRole("ADMIN","TEACHER")
                .simpTypeMatchers(SimpMessageType.MESSAGE,SimpMessageType.SUBSCRIBE).denyAll()
                .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
