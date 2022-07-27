package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.utils.EventListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;

@EventListener
public class PlayerMoveListener implements Listener {

	@EventHandler
	public void onPlayerMove(@Nonnull PlayerMoveEvent event) {
		event.setCancelled(event.getPlayer().getWorld() == Main.worldDark);
	}
}
