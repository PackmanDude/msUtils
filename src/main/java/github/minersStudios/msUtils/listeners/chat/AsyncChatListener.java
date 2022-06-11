package github.minersStudios.msUtils.listeners.chat;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import github.minersStudios.msUtils.utils.CommandUtils;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.annotation.Nonnull;
import java.util.Objects;

public class AsyncChatListener implements Listener {

    @EventHandler
    public void onChat(@Nonnull AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (player.getWorld() == Main.worldDark || !Main.authmeApi.isAuthenticated(player)) return;
        PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
        if (!playerInfo.isMuted()) {
            String message = event.getMessage();
            if(Objects.equals(String.valueOf(message.charAt(0)), "!")){
                ChatUtils.sendMessageToChat("[" + playerInfo.getID() + "] " + playerInfo.getFirstname() + " " + playerInfo.getLastname(), null, -1, ChatUtils.removeFirstChar(message));
                String discordMessage = "[" + playerInfo.getID() + "] " + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " : " + ChatUtils.removeFirstChar(message);
                DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordGlobalChannelID), discordMessage);
                DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), discordMessage);
                Main.chatBuffer.receiveChat(player, message);
            } else if(Objects.equals(String.valueOf(message.charAt(0)), "*")){
                CommandUtils.sendMessage(player, 25,  " ꀓ " + ChatColor.GOLD + "*" + ChatColor.GRAY + " [" + playerInfo.getID() + "] " + ChatColor.GOLD + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " " + ChatUtils.removeFirstChar(message) + "*");
                DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), "*" + " [" + playerInfo.getID() + "] " + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " " + ChatUtils.removeFirstChar(message) + "*");
            } else {
                ChatUtils.sendMessageToChat("[" + playerInfo.getID() + "] " + playerInfo.getFirstname() + " " + playerInfo.getLastname(), player.getLocation(), 25, message);
                DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(ChatUtils.discordLocalChannelID), "[" + playerInfo.getID() + "] " + playerInfo.getFirstname() + " " + playerInfo.getLastname() + " : " + message);
                Main.chatBuffer.receiveChat(player, message);
            }
        } else {
            ChatUtils.sendWarning(player, "Вы замучены");
        }
    }
}