package com.github.minersstudios.msutils.listeners.player;

import com.github.minersstudios.mscore.listener.MSListener;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerInfoMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

@MSListener
public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void onPlayerTeleport(@NotNull PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        PlayerInfoMap playerInfoMap = MSUtils.getConfigCache().playerInfoMap;
        PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(player);

        playerInfo.unsetSitting();

        if (
                event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE
                && playerInfo.isInWorldDark()
        ) {
            event.setCancelled(true);
        }
    }
}
