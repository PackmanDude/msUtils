package com.github.minersstudios.msutils.player;

import com.github.minersstudios.msutils.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerID {
	private static final File idFile = new File(Main.getInstance().getDataFolder(), "ids.yml");
	private final YamlConfiguration yamlConfiguration;

	public PlayerID() {
		this.yamlConfiguration = YamlConfiguration.loadConfiguration(idFile);
	}

	/**
	 * Adds player ID in "plugins/msUtils/ids.yml"
	 */
	private int addPlayer(@NotNull UUID uuid) {
		List<Object> IDs = new ArrayList<>(this.yamlConfiguration.getValues(true).values());
		int ID = this.createNewID(IDs, IDs.size());
		this.yamlConfiguration.set(uuid.toString(), ID);
		try {
			this.yamlConfiguration.save(idFile);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return ID;
	}

	/**
	 * @param uuid       player's uuid
	 * @param addPlayer  if true and player ID = null, creates new id
	 * @param zeroIfNull if true and player ID = null, returns -1
	 * @return player's ID int
	 */
	public int getPlayerID(@NotNull UUID uuid, boolean addPlayer, boolean zeroIfNull) {
		return this.yamlConfiguration.getValues(true).containsKey(uuid.toString()) ? this.yamlConfiguration.getInt(uuid.toString())
				: addPlayer ? this.addPlayer(uuid)
				: zeroIfNull ? 0 : -1;
	}

	/**
	 * @param ID player's ID
	 * @return player by ID
	 */
	public @Nullable OfflinePlayer getPlayerByID(int ID) {
		String uuid = getUUIDByID(ID);
		return uuid == null ? null : Bukkit.getOfflinePlayer(UUID.fromString(uuid));
	}

	private @Nullable String getUUIDByID(int ID) {
		for (Map.Entry<String, Object> entry : this.yamlConfiguration.getValues(true).entrySet()) {
			if (Objects.equals(ID, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	private int createNewID(@NotNull List<Object> IDs, int ID) {
		return IDs.contains(ID) ? createNewID(IDs, ID + 1) : ID;
	}
}