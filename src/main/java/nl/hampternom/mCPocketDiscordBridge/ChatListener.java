package nl.hampternom.mCPocketDiscordBridge;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ChatListener implements Listener {

    private final MCPocketDiscordBridge plugin;

    public ChatListener(MCPocketDiscordBridge plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        String username = event.getPlayer().getName();
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        if (message.startsWith("!")) {
            sendToDiscord("**[MC Command]** " + username + ": " + message);
            return;
        }

        sendToDiscord("**" + username + "**: " + message);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        sendToDiscord("**" + event.getPlayer().getName() + "** joined the game :)");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        sendToDiscord("**" + event.getPlayer().getName() + "** left the game :(");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        String deathMessage = event.getDeathMessage() != null ?
                PlainTextComponentSerializer.plainText().serialize(event.deathMessage()) :
                event.getPlayer().getName() + " died";
        sendToDiscord("noob " + deathMessage);
    }

    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent event) {
        io.papermc.paper.advancement.AdvancementDisplay display = event.getAdvancement().getDisplay();
        if (display == null) return;
        if (!display.doesAnnounceToChat()) return;

        String username = event.getPlayer().getName();
        String title = PlainTextComponentSerializer.plainText().serialize(display.title());
        sendToDiscord("**" + username + "** got the advancement **" + title + "**");
    }

    public void sendToDiscord(String message) {
        String webhookUrl = plugin.getConfig().getString("webhook-url");
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = "{\"content\": \"" + message.replace("\"", "\\\"") + "\"}";

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