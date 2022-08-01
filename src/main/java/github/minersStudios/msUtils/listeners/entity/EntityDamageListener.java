package github.minersStudios.msUtils.listeners.entity;

import github.minersStudios.msUtils.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;

public class EntityDamageListener implements Listener {

	@EventHandler
	public void onEntityDamage(@Nonnull EntityDamageEvent event) {
		event.setCancelled(event.getEntity().getWorld() == Main.getWorldDark() && event.getEntity().getType() == EntityType.PLAYER);
	}
}
