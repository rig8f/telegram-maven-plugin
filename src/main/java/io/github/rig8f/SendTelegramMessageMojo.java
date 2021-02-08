package io.github.rig8f;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "send", defaultPhase = LifecyclePhase.INSTALL,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, threadSafe = true)
public class SendTelegramMessageMojo extends AbstractMojo {

    @Parameter(property = "project", readonly = true)
    private MavenProject project;
    @Parameter(property = "mojoExecution", readonly = true)
    private MojoExecution execution;

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
            String artifact = "artifact";
            if (project != null && project.getArtifactId() != null)
                artifact = project.getArtifactId();

            String goal = "goal";
            if (execution != null && execution.getLifecyclePhase() != null)
                goal = execution.getLifecyclePhase().toLowerCase();

            message = "*Maven* " + artifact + " " + goal + " finished";
        }

        try {
            if (!TelegramUtils.sendMessage(botToken, chatId, message))
                logger.warn("Invalid Telegram server response");
        } catch (Exception e) {
            logger.error("Error calling Telegram servers", e);
        }
    }
}
