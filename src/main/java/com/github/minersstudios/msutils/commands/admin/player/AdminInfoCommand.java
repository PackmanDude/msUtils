package com.github.minersstudios.msutils.commands.admin.player;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerFile;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerName;
import com.github.minersstudios.msutils.player.PlayerSettings;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class AdminInfoCommand {

	public static boolean runCommand(
			@NotNull CommandSender sender,
			@NotNull PlayerInfo playerInfo
	) {
		PlayerFile playerFile = playerInfo.getPlayerFile();
		PlayerName playerName = playerFile.getPlayerName();
		PlayerSettings playerSettings = playerFile.getPlayerSettings();
		Location
				lastLeaveLocation = playerFile.getLastLeaveLocation(),
				lastDeathLocation = playerFile.getLastDeathLocation();

		if (lastLeaveLocation == null) {
			lastLeaveLocation = new Location(MSUtils.getOverworld(), 0, 0, 0);
		}

		if (lastDeathLocation == null) {
			lastDeathLocation = new Location(MSUtils.getOverworld(), 0, 0, 0);
		}

		ChatUtils.sendInfo(sender,
				"UUID : " + playerInfo.getOfflinePlayer().getUniqueId()
				+ "\n ID : " + playerInfo.getID(false, false)
				+ "\n Nickname : " + playerName.getNickname()
				+ "\n Firstname : " + playerName.getFirstName()
				+ "\n Lastname : " + playerName.getLastName()
				+ "\n Patronymic : " + playerName.getPatronymic()
				+ "\n RP-type : " + playerSettings.getResourcePackType()
				+ "\n Muted : " + playerInfo.isMuted()
				+ "\n Banned : " + playerInfo.isBanned()
				+ "\n First join : " + playerFile.getFirstJoin()
				+ "\n Mute reason : " + playerInfo.getMuteReason()
				+ "\n Muted to : " + playerInfo.getMutedTo()
				+ "\n Ban reason : " + playerInfo.getBanReason()
				+ "\n Banned to : " + playerInfo.getBannedTo()
				+ "\n Last death world : " + lastDeathLocation.getWorld().getName()
				+ "\n Last death X : " + lastDeathLocation.getX()
				+ "\n Last death Y : " + lastDeathLocation.getY()
				+ "\n Last death Z : " + lastDeathLocation.getZ()
				+ "\n Last death Yaw : " + lastDeathLocation.getYaw()
				+ "\n Last death Pitch : " + lastDeathLocation.getPitch()
				+ "\n Last leave world : " + lastLeaveLocation.getWorld().getName()
				+ "\n Last leave X : " + lastLeaveLocation.getX()
				+ "\n Last leave Y : " + lastLeaveLocation.getY()
				+ "\n Last leave Z : " + lastLeaveLocation.getZ()
				+ "\n Last leave Yaw : " + lastLeaveLocation.getYaw()
				+ "\n Last leave Pitch : " + lastLeaveLocation.getPitch()
		);
		return true;
	}
}
