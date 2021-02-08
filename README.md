# Telegram Maven plugin

A Maven plugin to notify successful builds using a Telegram bot.

## Usage

The package is not published, download the repository to `mvn install` it.

The plugin exposes a singe `send` goal that binds by default to the `install` phase.
Binding to another step, or to multiple steps, is also possible:
explicitly set the `<phase>` as in the example below.

A valid Telegram Bot token is needed, along with the user's ID: 

- chat with [@BotFather](https://t.me/BotFather) to create a new bot and obtain the token
- search and send a message to the bot using the mobile app
- visit https://api.telegram.org/bot<TOKEN>/getUpdates to grab the user ID from the JSON response
- fill `<configuration>` parameters

The message is optional; if not provided, it defaults to “Maven _artifact_ _goal_ finished”.

For more info visit the [official documentation](https://core.telegram.org/bots/api). 

```xml
<plugins>
...
<plugin>
    <groupId>io.github.rig8f</groupId>
    <artifactId>telegram-maven-plugin</artifactId>
    <version>1.1.0</version>
    <configuration>
        <botToken>???</botToken>
        <chatId>???</chatId>
        <message>(optional)</message>
    </configuration>
    <executions>
        <execution>
            <id>send-message-compile</id>
            <phase>compile</phase>
            <goals>
                <goal>send</goal>
            </goals>
        </execution>
        <execution>
            <id>send-message-package</id>
            <phase>package</phase>
            <goals>
                <goal>send</goal>
            </goals>
        </execution>
    </executions>
</plugin>
...
</plugins>
```
