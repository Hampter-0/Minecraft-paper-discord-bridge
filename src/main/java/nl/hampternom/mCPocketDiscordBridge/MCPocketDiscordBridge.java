package nl.hampternom.mCPocketDiscordBridge;

import org.bukkit.plugin.java.JavaPlugin;
import nl.hampternom.mCPocketDiscordBridge.ChatListener;

public final class MCPocketDiscordBridge extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getLogger().info("MCPocketDiscordBridge enabled");
    }
}