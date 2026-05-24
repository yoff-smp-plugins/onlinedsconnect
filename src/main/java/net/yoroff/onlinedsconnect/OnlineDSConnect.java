package net.yoroff.onlinedsconnect;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public final class OnlineDSConnect extends JavaPlugin {

    private File configFile;
    private FileConfiguration config;
    private DiscordBot discordBot;
    private BukkitTask updateTask;

    @Override
    public void onEnable() {
        createCustomConfig();

        String token = config.getString("bot-token");
        String guildId = config.getString("guild-id");
        String statusChannelId = config.getString("status-channel-id");
        String playersChannelId = config.getString("player-count-channel-id");

        if (token == null || token.equals("YOUR_DISCORD_BOT_TOKEN") || guildId == null || guildId.equals("YOUR_GUILD_ID")) {
            getLogger().severe("Configure config.toml before using this plugin!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        discordBot = new DiscordBot(token, guildId, statusChannelId, playersChannelId, this);
        discordBot.connect();

        updateTask = getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (discordBot.isReady()) {
                int online = getServer().getOnlinePlayers().size();
                discordBot.updatePlayerCount(online);
            }
        }, 200L, 6000L);
    }

    @Override
    public void onDisable() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        if (discordBot != null) {
            discordBot.shutdown();
        }
    }

    private void createCustomConfig() {
        configFile = new File(getDataFolder(), "config.toml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                configFile.createNewFile();
                config = YamlConfiguration.loadConfiguration(configFile);
                config.set("bot-token", "YOUR_DISCORD_BOT_TOKEN");
                config.set("guild-id", "YOUR_GUILD_ID");
                config.set("status-channel-id", "YOUR_STATUS_CHANNEL_ID");
                config.set("player-count-channel-id", "YOUR_PLAYER_COUNT_CHANNEL_ID");
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            config = YamlConfiguration.loadConfiguration(configFile);
        }
    }
}