package dev.roder.intqtoolbackend.Websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;


/**
 * Configures security for websocket connections
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    /**
     * Defines each subscription path and what type of user can connect to it.
     * QuizAnswer is accessible for users with ROLE_TEACHER and ROLE_ADMIN.
     * notifications is accessible for any user.
     *
     * @param messages webSocket message to apply the security to.
     */
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated()
                .simpDestMatchers("/topic/quizanswers/**").hasAnyRole("ADMIN","TEACHER")
                .simpDestMatchers("/topic/notifications/**").authenticated()
                .simpTypeMatchers(SimpMessageType.MESSAGE,SimpMessageType.SUBSCRIBE).denyAll()
                .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
