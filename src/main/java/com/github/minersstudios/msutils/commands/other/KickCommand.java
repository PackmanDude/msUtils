package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerID;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.tabcompleters.AllLocalPlayers;
import com.github.minersstudios.msutils.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@MSCommand(command = "kick")
public class KickCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length == 0) return false;
		String reason = args.length > 1
				? ChatUtils.extractMessage(args, 1)
				: "неизвестно";
		if (args[0].matches("-?\\d+")) {
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

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return new AllLocalPlayers().onTabComplete(sender, command, label, args);
	}
}
