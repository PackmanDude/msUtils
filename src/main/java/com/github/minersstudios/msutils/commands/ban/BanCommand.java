package com.github.minersstudios.msutils.commands.ban;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.CommandUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.tabcompleters.AllPlayers;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

@MSCommand(
		command = "ban",
		usage = " ꀑ §cИспользуй: /<command> [id/никнейм] [время][s/m/h/d/M/y] [причина]",
		description = "Покажи кто тут главный и забань игрока",
		permission = "msutils.ban",
		permissionDefault = PermissionDefault.OP
)
public class BanCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(
			@NotNull CommandSender sender, 
			@NotNull Command command, 
			@NotNull String label, 
			String @NotNull ... args
	) {
		if (args.length < 2 || !args[1].matches("\\d+[smhdMy]")) return false;
		Date date = CommandUtils.getDateFromString(args[1]);
		String reason = args.length > 2
				? ChatUtils.extractMessage(args, 2)
				: "неизвестно";
		if (args[0].matches("-?\\d+")) {
			OfflinePlayer offlinePlayer = IDUtils.getPlayerByID(Integer.parseInt(args[0]));
			if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore() || offlinePlayer.getName() == null) {
				ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
				return true;
			}
			PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), offlinePlayer.getName());
			if (playerInfo.isBanned()) {
				ChatUtils.sendWarning(sender,
						text("Игрок : \"")
						.append(playerInfo.createGrayIDGoldName())
						.append(text("\" уже забанен"))
				);
				return true;
			}
			playerInfo.setBanned(true, date, reason, sender);
			return true;
		}
		if (args[0].length() > 2) {
			OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);
			if (offlinePlayer == null) {
				ChatUtils.sendError(sender, "Кажется, что-то пошло не так...");
				return true;
			}
			PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(offlinePlayer.getUniqueId(), args[0]);
			if (playerInfo.isBanned()) {
				ChatUtils.sendWarning(sender,
						text("Игрок : \"")
						.append(playerInfo.createGrayIDGoldName())
						.append(text(" ("))
						.append(text(args[0]))
						.append(text(")\" уже забанен"))
				);
				return true;
			}
			playerInfo.setBanned(true, date, reason, sender);
			return true;
		}
		ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		return new AllPlayers().onTabComplete(sender, command, label, args);
	}
}
