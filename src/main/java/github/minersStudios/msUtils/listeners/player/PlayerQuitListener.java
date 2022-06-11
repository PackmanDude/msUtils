package github.minersStudios.msUtils.listeners.player;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.classes.PlayerInfo;
import github.minersStudios.msUtils.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerInfo playerInfo = new PlayerInfo(player.getUniqueId());
        event.setQuitMessage(null);
        if (playerInfo.hasPlayerDataFile() && player.getWorld() != Main.worldDark) {
            Location playerLocation = player.getLocation();
            playerInfo.setLastLeaveLocation(player.getWorld(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());
            if(playerInfo.hasName()){
                ChatUtils.broadcastLeaveMessage(playerInfo, player);
            }
        }
    }
}