package com.github.minersstudios.msutils.commands.other;

import com.github.minersstudios.msutils.enums.Crafts;
import com.github.minersstudios.msutils.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class CraftsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!(sender instanceof Player player)) {
            return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
        }
        player.openInventory(Crafts.getInventory(0));
        return true;
    }
}