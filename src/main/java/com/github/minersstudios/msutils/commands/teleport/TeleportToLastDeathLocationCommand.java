package com.github.minersstudios.msutils.commands.teleport;

import com.github.minersstudios.mscore.command.MSCommand;
import com.github.minersstudios.mscore.command.MSCommandExecutor;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.PlayerUtils;
import com.github.minersstudios.msutils.MSUtils;
import com.github.minersstudios.msutils.player.IDMap;
import com.github.minersstudios.msutils.player.PlayerInfo;
import com.github.minersstudios.msutils.player.PlayerInfoMap;
import com.github.minersstudios.msutils.tabcompleters.AllLocalPlayers;
import com.github.minersstudios.msutils.utils.IDUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

@MSCommand(
        command = "teleporttolastdeathlocation",
        aliases = {
                "teleporttolastdeathloc",
                "tptolastdeathlocation",
                "tptolastdeathloc",
                "tptolastdeath"
        },
        usage = " ꀑ §cИспользуй: /<command> [id/никнейм]",
        description = "Телепортирует игрока на его последнее место смерти",
        permission = "msutils.teleporttolastdeathlocation",
        permissionDefault = PermissionDefault.OP
)
public class TeleportToLastDeathLocationCommand implements MSCommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String @NotNull ... args
    ) {
        if (args.length == 0) return false;

        if (IDUtils.matchesIDRegex(args[0])) {
            IDMap idMap = MSUtils.getConfigCache().idMap;
            OfflinePlayer offlinePlayer = idMap.getPlayerByID(args[0]);

            if (offlinePlayer == null) {
                ChatUtils.sendError(sender, "Вы ошиблись айди, игрока привязанного к нему не существует");
                return true;
            }

            teleportToLastDeathLocation(sender, offlinePlayer);
            return true;
        }

        if (args[0].length() > 2) {
            OfflinePlayer offlinePlayer = PlayerUtils.getOfflinePlayerByNick(args[0]);

            if (offlinePlayer == null) {
                ChatUtils.sendError(sender, "Данного игрока не существует");
                return true;
            }

            teleportToLastDeathLocation(sender, offlinePlayer);
            return true;
        }

        ChatUtils.sendWarning(sender, "Ник не может состоять менее чем из 3 символов!");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String @NotNull ... args
    ) {
        return new AllLocalPlayers().onTabComplete(sender, command, label, args);
    }

    private static void teleportToLastDeathLocation(
            @NotNull CommandSender sender,
            @NotNull OfflinePlayer offlinePlayer
    ) {
        if (!offlinePlayer.hasPlayedBefore() || offlinePlayer.getName() == null) {
            ChatUtils.sendWarning(sender, "Данный игрок ещё ни разу не играл на сервере");
            return;
        }

        PlayerInfoMap playerInfoMap = MSUtils.getConfigCache().playerInfoMap;
        PlayerInfo playerInfo = playerInfoMap.getPlayerInfo(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        Location lastDeathLocation = playerInfo.getPlayerFile().getLastDeathLocation();

        if (offlinePlayer.getPlayer() == null) {
            ChatUtils.sendWarning(sender,
                    text("Игрок : \"")
                    .append(playerInfo.getGrayIDGreenName())
                    .append(text(" ("))
                    .append(text(offlinePlayer.getName()))
                    .append(text(")\" не в сети!"))
            );
            return;
        }

        if (lastDeathLocation == null) {
            ChatUtils.sendWarning(sender,
                    text("Игрок : \"")
                    .append(playerInfo.getGrayIDGreenName())
                    .append(text(" ("))
                    .append(text(offlinePlayer.getName()))
                    .append(text(")\" не имеет последней точки смерти!"))
            );
            return;
        }

        playerInfo.teleportToLastDeathLocation();
        ChatUtils.sendFine(sender,
                text("Игрок : \"")
                .append(playerInfo.getGrayIDGreenName())
                .append(text(" ("))
                .append(text(offlinePlayer.getName()))
                .append(text(")\" был телепортирован на последние координаты смерти"))
        );
    }

    @Override
    public @Nullable CommandNode<?> getCommandNode() {
        return LiteralArgumentBuilder.literal("teleporttolastdeathlocation")
                .then(RequiredArgumentBuilder.argument("id/никнейм", StringArgumentType.word()))
                .build();
    }
}
