package nl.hampternom.mCPocketDiscordBridge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AdvancementsCommand {

    private final MCPocketDiscordBridge plugin;

    public AdvancementsCommand(MCPocketDiscordBridge plugin) {
        this.plugin = plugin;
    }

    public void handle(String username) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(username);
                if (player == null) {
                    plugin.getChatListener().sendToDiscord("No player found with name: " + username);
                    return;
                }

                UUID uuid = player.getUniqueId();
                File advFile = new File(Bukkit.getWorlds().get(0).getWorldFolder(),
                        "advancements/" + uuid + ".json");

                if (!advFile.exists()) {
                    plugin.getChatListener().sendToDiscord("No advancements found for " + username);
                    return;
                }

                JsonObject advancements = JsonParser.parseReader(new FileReader(advFile)).getAsJsonObject();

                List<String> completed = new ArrayList<>();

                for (Map.Entry<String, JsonElement> entry : advancements.entrySet()) {
                    String key = entry.getKey();
                    if (key.equals("DataVersion")) continue;
                    if (key.startsWith("minecraft:recipes/")) continue;

                    JsonObject advancement = entry.getValue().getAsJsonObject();
                    JsonElement done = advancement.get("done");
                    if (done != null && done.getAsBoolean()) {
                        String name = key.contains("/") ? key.substring(key.lastIndexOf("/") + 1) : key;
                        name = name.replace("_", " ");
                        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                        completed.add(name);
                    }
                }

                String message = String.format(
                        "**Advancements for %s**\n" +
                                "Completed: %d\n\n" +
                                "%s",
                        username,
                        completed.size(),
                        String.join(", ", completed)
                );

                if (message.length() > 1900) {
                    message = message.substring(0, 1900) + "...";
                }

                plugin.getChatListener().sendToDiscord(message);

            } catch (Exception e) {
                plugin.getLogger().warning("Advancements error: " + e.getMessage());
                plugin.getChatListener().sendToDiscord("Error fetching advancements for " + username);
            }
        });
    }
}