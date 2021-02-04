package io.github.rig8f;

import okhttp3.*;

import java.io.IOException;

public class TelegramUtils {
    private static final OkHttpClient client = new OkHttpClient();

    private TelegramUtils() {
    }

    public static boolean sendMessage(String botToken, String chatId, String message) throws IOException {
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
