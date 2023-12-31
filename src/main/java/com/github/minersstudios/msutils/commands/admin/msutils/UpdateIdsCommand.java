package com.github.minersstudios.msutils.commands.admin.msutils;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.config.ConfigCache;
import com.github.minersstudios.msutils.player.PlayerInfo;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.Component.text;

public class UpdateIdsCommand {

    public static void runCommand(@NotNull CommandSender sender) {
        long time = System.currentTimeMillis();
        ConfigCache configCache = MSUtils.getConfigCache();

        configCache.idMap.reloadIds();

        for (PlayerInfo playerInfo : configCache.playerInfoMap.getMap().values()) {
            playerInfo.initNames();
        }

        ChatUtils.sendFine(sender,
                text("Список айди был успешно перезагружен за ")
                .append(text(System.currentTimeMillis() - time))
                .append(text("ms"))
        );
    }
}
