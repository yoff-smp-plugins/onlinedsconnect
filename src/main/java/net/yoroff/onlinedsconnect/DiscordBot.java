package net.yoroff.onlinedsconnect;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class DiscordBot {

    private final String token;
    private final String guildId;
    private final String statusChannelId;
    private final String playersChannelId;
    private final JavaPlugin plugin;
    private JDA jda;
    private boolean ready = false;

    public DiscordBot(String token, String guildId, String statusChannelId, String playersChannelId, JavaPlugin plugin) {
        this.token = token;
        this.guildId = guildId;
        this.statusChannelId = statusChannelId;
        this.playersChannelId = playersChannelId;
        this.plugin = plugin;
    }

    public void connect() {
        try {
            jda = JDABuilder.createLight(token, Collections.emptyList())
                    .build();
            jda.awaitReady();
            ready = true;
            updateStatus(true);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to connect to Discord: " + e.getMessage());
        }
    }

    public boolean isReady() {
        return ready && jda != null;
    }

    public void updateStatus(boolean online) {
        if (!isReady()) return;
        Guild guild = jda.getGuildById(guildId);
        if (guild == null) return;

        VoiceChannel channel = guild.getVoiceChannelById(statusChannelId);
        if (channel != null) {
            String status = online ? "┏『🟢』・Сервер онлайн" : "┏『🔴』・Сервер оффлайн";
            channel.getManager().setName(status).queue(
                    null,
                    throwable -> plugin.getLogger().warning("Failed to update status channel: " + throwable.getMessage())
            );
        }
    }

    public void updatePlayerCount(int count) {
        if (!isReady()) return;
        Guild guild = jda.getGuildById(guildId);
        if (guild == null) return;

        VoiceChannel channel = guild.getVoiceChannelById(playersChannelId);
        if (channel != null) {
            String name = "┗『🎮』・Игроков: " + count;
            channel.getManager().setName(name).queue(
                    null,
                    throwable -> plugin.getLogger().warning("Failed to update player count channel: " + throwable.getMessage())
            );
        }
    }

    public void shutdown() {
        if (jda != null) {
            try {
                updateStatus(false);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            jda.shutdownNow();
        }
    }
}