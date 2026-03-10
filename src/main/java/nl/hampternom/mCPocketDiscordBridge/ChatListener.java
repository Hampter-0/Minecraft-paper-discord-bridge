package nl.hampternom.mCPocketDiscordBridge;

import org.bukkit.event.Listener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ChatListener implements Listener {

    private final MCPocketDiscordBridge plugin;

    public ChatListener(MCPocketDiscordBridge plugin) {
        this.plugin = plugin;
    }

    public void sendToDiscord(String message) {
        String webhookUrl = plugin.getConfig().getString("webhook-url");
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = "{\"content\": \"" + message + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            conn.getResponseCode();
            conn.disconnect();
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to send to Discord: " + e.getMessage());
        }
    }
}