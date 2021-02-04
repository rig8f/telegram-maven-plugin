package io.github.rig8f;

import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.ExecutionEvent;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

//@Component(role = EventSpy.class)
public class BuildEventSpy extends AbstractEventSpy {

    @Requirement
    private Logger logger;

    private String botToken;
    private String chatId;
    private String message;

    @Override
    public void onEvent(Object event) {
        if (event instanceof ExecutionEvent) {
            ExecutionEvent exEvent = (ExecutionEvent) event;
            if (exEvent.getType() != ExecutionEvent.Type.ProjectSucceeded &&
                    exEvent.getType() != ExecutionEvent.Type.ProjectFailed) {
                return;
            }

            if (botToken == null || botToken.isEmpty()) {
                logger.error("No bot token provided in configuration");
                return;
            }
            if (chatId == null || chatId.isEmpty()) {
                logger.error("No chat id provided in configuration");
                return;
            }
            if (message == null || message.isEmpty()) {
                logger.warn("Message not provided in configuration, using defaults");
                if (exEvent.getType() == ExecutionEvent.Type.ProjectSucceeded)
                    message = "*Maven* BUILD SUCCESS";
                else if (exEvent.getType() == ExecutionEvent.Type.ProjectFailed)
                    message = "*Maven* BUILD FAILED";
            }

            try {
                if (!TelegramUtils.sendMessage(botToken, chatId, message))
                    logger.warn("Invalid Telegram server response");
            } catch (Exception e) {
                logger.error("Error calling Telegram servers", e);
            }
        }
    }
}
