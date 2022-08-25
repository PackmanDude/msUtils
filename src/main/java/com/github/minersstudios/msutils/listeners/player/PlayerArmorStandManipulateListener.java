package com.github.minersstudios.msUtils.listeners.player;

import com.github.minersstudios.msUtils.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import javax.annotation.Nonnull;

public class PlayerArmorStandManipulateListener implements Listener {

	@EventHandler
	public void onPlayerArmorStandManipulate(@Nonnull PlayerArmorStandManipulateEvent event) {
		event.setCancelled(PlayerUtils.getSeats().containsValue(event.getRightClicked()));
	}
}
