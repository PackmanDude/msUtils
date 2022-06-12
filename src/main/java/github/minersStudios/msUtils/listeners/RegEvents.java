package github.minersStudios.msUtils.listeners;

import github.minersStudios.msUtils.listeners.chat.AsyncChatListener;
import github.minersStudios.msUtils.listeners.chat.DiscordSRVListener;
import github.minersStudios.msUtils.listeners.player.*;
import github.scarsz.discordsrv.DiscordSRV;
import org.bukkit.plugin.PluginManager;

import static github.minersStudios.msUtils.Main.plugin;

public class RegEvents {

    public RegEvents(){
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        DiscordSRV.api.subscribe(new DiscordSRVListener());
        pluginManager.registerEvents(new PlayerInteractEntityListener(), plugin);
        pluginManager.registerEvents(new PlayerChangedWorldListener(), plugin);
        pluginManager.registerEvents(new PlayerMoveListener(), plugin);
        pluginManager.registerEvents(new AsyncChatListener(), plugin);
        pluginManager.registerEvents(new PlayerDropItemListener(), plugin);
        pluginManager.registerEvents(new PlayerInteractListener(), plugin);
        pluginManager.registerEvents(new EntityDamageListener(), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(), plugin);
        pluginManager.registerEvents(new InventoryClickListener(), plugin);
        pluginManager.registerEvents(new InventoryCloseListener(), plugin);
        pluginManager.registerEvents(new PlayerResourcePackStatusListener(), plugin);
        pluginManager.registerEvents(new PlayerDeathListener(), plugin);
        pluginManager.registerEvents(new PlayerJoinListener(), plugin);
    }
}
