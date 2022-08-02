package com.github.MinersStudios.msUtils.listeners.player;

import com.github.MinersStudios.msUtils.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import javax.annotation.Nonnull;

public class PlayerChangedWorldListener implements Listener {

	@EventHandler
	public void onPlayerChangedWorld(@Nonnull PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		Main.getScoreboardHideTagsTeam().addEntry(player.getName());
		player.setScoreboard(Main.getScoreboardHideTags());
	}
}