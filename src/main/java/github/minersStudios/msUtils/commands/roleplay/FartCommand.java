package github.minersStudios.msUtils.commands.roleplay;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class FartCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player))
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		if (player.getWorld() == Main.getWorldDark() || !Main.getAuthmeApi().isAuthenticated(player))
			return true;
		PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
		if (playerInfo.isMuted())
			return ChatUtils.sendWarning(player, Component.text("Вы замучены"));
		Location location = player.getLocation();
		player.getWorld().playSound(location.add(0, 0.4, 0), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
		player.getWorld().spawnParticle(Particle.REDSTONE, location, 15, 0.0D, 0.0D, 0.0D, 0.5D, new Particle.DustOptions(Color.fromBGR(33, 54, 75), 10));
		return ChatUtils.sendRPEventMessage(player, Component.text(playerInfo.getPronouns().getFartMessage()));
	}
}
