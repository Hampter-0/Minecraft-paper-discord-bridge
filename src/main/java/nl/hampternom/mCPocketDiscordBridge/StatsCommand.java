package nl.hampternom.mCPocketDiscordBridge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.FileReader;
import java.util.UUID;

public class StatsCommand {

    private final MCPocketDiscordBridge plugin;

    public StatsCommand(MCPocketDiscordBridge plugin) {
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
                File statsFile = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "stats/" + uuid + ".json");

                if (!statsFile.exists()) {
                    plugin.getChatListener().sendToDiscord("No stats found for " + username);
                    return;
                }

                JsonObject stats = JsonParser.parseReader(new FileReader(statsFile)).getAsJsonObject();
                JsonObject custom = stats.getAsJsonObject("stats")
                        .getAsJsonObject("minecraft:custom");

                long playtimeTicks = getLong(custom, "minecraft:play_time");
                long deaths = getLong(custom, "minecraft:deaths");
                long jumps = getLong(custom, "minecraft:jump");
                long distanceWalked = getLong(custom, "minecraft:walk_one_cm");
                long mobsKilled = getLong(custom, "minecraft:mob_kills");
                long blocksPlaced = getLong(stats.getAsJsonObject("stats")
                        .getAsJsonObject("minecraft:used"), null);
                long blocksBroken = getLong(stats.getAsJsonObject("stats")
                        .getAsJsonObject("minecraft:mined"), null);

                long playtimeMinutes = playtimeTicks / 1200;
                long hours = playtimeMinutes / 60;
                long minutes = playtimeMinutes % 60;
                double distanceKm = distanceWalked / 100000.0;

                String message = String.format(
                        "**Stats for %s**\n" +
                                "Playtime: %dh %dm\n" +
                                "Deaths: %d\n" +
                                "Jumps: %d\n" +
                                "Distance Walked: %.2f km\n" +
                                "Mobs Killed: %d\n" +
                                "Blocks Broken: %d\n" +
                                "Blocks Placed: %d",
                        username, hours, minutes, deaths, jumps, distanceKm, mobsKilled, blocksBroken, blocksPlaced
                );

                plugin.getChatListener().sendToDiscord(message);

            } catch (Exception e) {
                plugin.getLogger().warning("Stats error: " + e.getMessage());
                plugin.getChatListener().sendToDiscord("Error fetching stats for " + username);
            }
        });
    }

    private long getLong(JsonObject obj, String key) {
        if (obj == null) return 0;
        if (key == null) {
            long total = 0;
            for (JsonElement el : obj.entrySet().stream().map(e -> e.getValue()).toList()) {
                total += el.getAsLong();
            }
            return total;
        }
        JsonElement el = obj.get(key);
        return el != null ? el.getAsLong() : 0;
    }
}