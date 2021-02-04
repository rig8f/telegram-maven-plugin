package io.github.rig8f;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "send", defaultPhase = LifecyclePhase.INSTALL)
public class SendTelegramMessageMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = true)
    private MavenProject project;

    @Parameter
    private String botToken;
    @Parameter
    private String chatId;
    @Parameter
    private String message;

    public void execute() {
        Log logger = getLog();
        if (botToken == null || botToken.isEmpty()) {
            logger.error("No bot token provided in configuration");
            return;
        }
        if (chatId == null || chatId.isEmpty()) {
            logger.error("No chat id provided in configuration");
            return;
        }
        if (message == null || message.isEmpty()) {
            //logger.warn("Message not provided in configuration, using default");
            if (project != null)
                message = "*Maven* " + project.getArtifactId() + " build finished";
            else
                message = "*Maven* build finished";
        }

        try {
            if (!TelegramUtils.sendMessage(botToken, chatId, message))
                logger.warn("Invalid Telegram server response");
        } catch (Exception e) {
            logger.error("Error calling Telegram servers", e);
        }
    }
}
