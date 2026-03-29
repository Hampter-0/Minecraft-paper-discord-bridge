package nl.hampternom.mCPocketDiscordBridge;

import org.bukkit.plugin.java.JavaPlugin;
import nl.hampternom.mCPocketDiscordBridge.ChatListener;

public final class MCPocketDiscordBridge extends JavaPlugin {

    private ChatListener chatListener;
    private StatsCommand statsCommand;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        chatListener = new ChatListener(this);
        statsCommand = new StatsCommand(this);
        getServer().getPluginManager().registerEvents(chatListener, this);

        if (getCommand("discordstats") == null) {
            getLogger().warning("discordstats command is null!");
        } else {
            getLogger().info("discordstats command registered!");
            getCommand("discordstats").setExecutor((sender, command, label, args) -> {
                getLogger().info("discordstats called with " + args.length + " args");
                if (args.length == 0) return false;
                getLogger().info("Fetching stats for: " + args[0]);
                statsCommand.handle(args[0]);
                return true;
            });
        }

        getLogger().info("MCPocketDiscordBridge enabled");
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public StatsCommand getStatsCommand() {
        return statsCommand;
    }
}