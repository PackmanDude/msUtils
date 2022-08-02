package com.github.MinersStudios.msUtils.commands.other;

import com.github.MinersStudios.msUtils.classes.PlayerID;
import com.github.MinersStudios.msUtils.classes.PlayerInfo;
import com.github.MinersStudios.msUtils.utils.ChatUtils;
import com.github.MinersStudios.msUtils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class KickCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (args.length < 1) return false;
		String reason = args.length > 1
				? ChatUtils.extractMessage(args, 1)
				: "неизвестно";
		if (args[0].matches("[0-99]+")) {
			OfflinePlayer offlinePlayer = new PlayerID().getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null) {
				return ChatUtils.sendError(sender, Component.text("Вы ошиблись айди, игрока привязанного к нему не существует"));
			}
			PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
			if (PlayerUtils.kickPlayer(offlinePlayer, "Вы были кикнуты", reason)) {
				return ChatUtils.sendFine(sender,
						Component.text("Игрок : \"")
								.append(playerInfo.getGrayIDGreenName())
								.append(Component.text("\" был кикнут :\n    - Причина : \""))
								.append(Component.text(reason))
				);
			}
			return ChatUtils.sendWarning(sender,
					Component.text("Игрок : \"")
							.append(playerInfo.getGrayIDGoldName())
							.append(Component.text("\" не в сети!"))
			);
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				return ChatUtils.sendError(sender, Component.text("Что-то пошло не так..."));
			}
			PlayerInfo playerInfo = new PlayerInfo(offlinePlayer.getUniqueId());
			if (PlayerUtils.kickPlayer(offlinePlayer, "Вы были кикнуты", reason)) {
				return ChatUtils.sendFine(sender,
						Component.text("Игрок : \"")
								.append(playerInfo.getGrayIDGreenName())
								.append(Component.text(" ("))
								.append(Component.text(args[0]))
								.append(Component.text(")\" был кикнут :\n    - Причина : \""))
								.append(Component.text(reason))
				);
			}
			return ChatUtils.sendWarning(sender,
					Component.text("Игрок : \"")
							.append(playerInfo.getGrayIDGoldName())
							.append(Component.text(" ("))
							.append(Component.text(args[0]))
							.append(Component.text(")\" не в сети!"))
			);
		}
		return ChatUtils.sendWarning(sender, Component.text("Ник не может состоять менее чем из 3 символов!"));
	}
}