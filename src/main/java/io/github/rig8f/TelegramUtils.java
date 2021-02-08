package io.github.rig8f;

import okhttp3.*;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;

public class TelegramUtils {
    private static final OkHttpClient client = new OkHttpClient();

    private TelegramUtils() {
    }

    public static boolean sendMessage(String botToken, String chatId, String message) throws IOException {
        return sendMessageGet(botToken, chatId, message);
    }

    private static boolean sendMessageGet(String botToken, String chatId, String message) throws IOException {
        String baseUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        HttpUrl url = HttpUrl.parse(baseUrl).newBuilder()
                .addQueryParameter("chat_id", chatId)
                .addQueryParameter("text", message.replace("-", "\\-"))
                .addQueryParameter("parse_mode", "MarkdownV2")
                .build();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return (response.isSuccessful() && response.code() == 200);
    }

    private static boolean sendMessagePost(String botToken, String chatId, String message) throws IOException {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("chat_id", chatId)
                .addFormDataPart("text", message)
                .addFormDataPart("parse_mode", "MarkdownV2")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        return (response.isSuccessful() && response.code() == 200);
    }
}
