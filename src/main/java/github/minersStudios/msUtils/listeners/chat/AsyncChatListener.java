package github.minersStudios.msUtils.listeners.chat;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.ChatBuffer;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.annotation.Nonnull;
import java.util.Objects;

public class AsyncChatListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onAsyncPlayerChat(@Nonnull AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		if (player.getWorld() == Main.worldDark || !Main.authmeApi.isAuthenticated(player)) return;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());

		if (playerInfo.isMuted() && playerInfo.getMutedTo() - System.currentTimeMillis() < 0)
			playerInfo.setMuted(false, null);

		if (playerInfo.isMuted()) {
			ChatUtils.sendWarning(player, "Вы замучены");
			return;
		}

		String message = event.getMessage();
		if (Objects.equals(String.valueOf(message.charAt(0)), "!")) {
			message = ChatUtils.removeFirstChar(message);
			if (message.length() != 0)
				ChatUtils.sendMessageToChat(playerInfo, null, -1, message);
		} else if (Objects.equals(String.valueOf(message.charAt(0)), "*")) {
			message = ChatUtils.removeFirstChar(message);
			if (message.length() != 0)
				ChatUtils.sendRPEventMessage(player, 25, "* " + playerInfo.getGrayIDGoldName() + " " + message + "*");
		} else {
			ChatUtils.sendMessageToChat(playerInfo, player.getLocation(), 25, message);
			ChatBuffer.receiveMessage(player, message);
		}
	}
}