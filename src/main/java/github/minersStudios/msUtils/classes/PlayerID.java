package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerID {

    private final File idFile;
    private final YamlConfiguration yamlConfiguration;

    public PlayerID(){
        this.idFile = new File(Main.plugin.getDataFolder(), "ids.yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(idFile);
    }

    /**
     * Adds player id in "plugins/msUtils/ids.yml"
     */
    public void addPlayer(@Nonnull UUID uuid){
        List<Object> list = new ArrayList<>(this.yamlConfiguration.getValues(true).values());
        int ID = list.size() > 0 ? (int) list.get(list.size() - 1) + 1 : 0;
        this.yamlConfiguration.set(uuid.toString(), ID);
        try {
            this.yamlConfiguration.save(this.idFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @param uuid player's uuid
     * @return player's id int
     */
    public int getPlayerID(@Nonnull UUID uuid){
        return this.yamlConfiguration.getInt(uuid.toString());
    }

    /**
     * @param ID player's ID
     * @return player by ID
     */
    @Nullable public OfflinePlayer getPlayerByID(int ID){
        Map<String, Object> map = this.yamlConfiguration.getValues(true);
        return map.containsValue(ID) ? Main.plugin.getServer().getOfflinePlayer(UUID.fromString(Objects.requireNonNull(getKeyByValue(map, ID)))) : null;
    }

    @Nullable private static <String, Object> String getKeyByValue(@Nonnull Map<String, Object> map, @Nonnull Object value) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}