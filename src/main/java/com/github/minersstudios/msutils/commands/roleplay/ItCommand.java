package com.github.minersstudios.msutils.commands.roleplay;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.utils.MSPlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.github.minersstudios.msutils.utils.MessageUtils.RolePlayActionType.IT;
import static com.github.minersstudios.msutils.utils.MessageUtils.sendRPEventMessage;
import static net.kyori.adventure.text.Component.text;

@MSCommand(
		command = "it",
		usage = " ꀑ §cИспользуй: /<command> [действие]",
		description = "Описывает действие, от третьего лица"
)
public class ItCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(
			@NotNull CommandSender sender, 
			@NotNull Command command, 
			@NotNull String label, 
			String @NotNull ... args
	) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, "Только игрок может использовать эту команду!");
			return true;
		}
		PlayerInfo playerInfo = MSPlayerUtils.getPlayerInfo(player);
		if (!playerInfo.isOnline()) return true;
		if (args.length == 0) return false;
		if (playerInfo.isMuted()) {
			ChatUtils.sendWarning(player, "Вы замьючены");
			return true;
		}
		sendRPEventMessage(player, text(ChatUtils.extractMessage(args, 0)), IT);
		return true;
	}
}
