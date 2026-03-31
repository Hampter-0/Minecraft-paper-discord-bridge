package nl.hampternom.mCPocketDiscordBridge;

import org.bukkit.plugin.java.JavaPlugin;
import nl.hampternom.mCPocketDiscordBridge.ChatListener;

public final class MCPocketDiscordBridge extends JavaPlugin {

    private ChatListener chatListener;
    private StatsCommand statsCommand;
    private AdvancementsCommand advancementsCommand;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        chatListener = new ChatListener(this);
        statsCommand = new StatsCommand(this);
        advancementsCommand = new AdvancementsCommand(this);
        getServer().getPluginManager().registerEvents(chatListener, this);

        if (getCommand("discordstats") == null) {
            getLogger().warning("discordstats command is null!");
        } else {
            getLogger().info("discordstats command registered!");
            getCommand("discordstats").setExecutor((sender, command, label, args) -> {
                if (args.length == 0) return false;
                statsCommand.handle(args[0]);
                return true;
            });
        }

        if (getCommand("discordadvancements") == null) {
            getLogger().warning("discordadvancements command is null!");
        } else {
            getLogger().info("discordadvancements command registered!");
            getCommand("discordadvancements").setExecutor((sender, command, label, args) -> {
                if (args.length == 0) return false;
                advancementsCommand.handle(args[0]);
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

    public AdvancementsCommand getAdvancementsCommand() {
        return advancementsCommand;
    }
}