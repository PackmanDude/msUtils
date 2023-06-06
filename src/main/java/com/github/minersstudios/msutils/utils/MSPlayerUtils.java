package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;

public final class MSPlayerUtils {

	private MSPlayerUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static @NotNull Map<UUID, PlayerInfo> getPlayerMap() {
		return getConfigCache().playerInfoMap;
	}
	
	public static @NotNull PlayerInfo getPlayerInfo(
			@NotNull UUID uuid,
			@NotNull String nickname
	) {
		PlayerInfo playerInfo = getPlayerMap().get(uuid);
		if (playerInfo == null) {
			playerInfo = new PlayerInfo(uuid, nickname);
			getPlayerMap().put(uuid, playerInfo);
		}
		return playerInfo;
	}

	public static @NotNull PlayerInfo getPlayerInfo(@NotNull Player player) {
		UUID uuid = player.getUniqueId();
		PlayerInfo playerInfo = getPlayerMap().get(uuid);
		if (playerInfo == null) {
			playerInfo = new PlayerInfo(player);
			getPlayerMap().put(uuid, playerInfo);
		}
		return playerInfo;
	}

	public static boolean hideNameTag(@Nullable Player player) {
		if (player == null) return false;
		MSUtils.scoreboardHideTagsTeam.addEntry(player.getName());
		player.setScoreboard(MSUtils.scoreboardHideTags);
		return true;
	}
}
