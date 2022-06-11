package github.minersStudios.msUtils.classes;

import github.minersStudios.msUtils.Main;
import github.minersStudios.msUtils.enums.DiskType;
import github.minersStudios.msUtils.enums.Pronouns;
import github.minersStudios.msUtils.enums.ResourcePackType;
import github.minersStudios.msUtils.utils.ChatUtils;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public final class PlayerInfo {
    private final File dataFile;
    private final YamlConfiguration yamlConfiguration;
    @Getter private final UUID uuid;
    private Player onlinePlayer;
    private String firstname, lastname, patronymic;

    public PlayerInfo(@Nonnull UUID uuid){
        this.dataFile = new File(Main.plugin.getDataFolder(), "players/" + uuid + ".yml");
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(dataFile);
        this.uuid = uuid;
    }

    /**
     * Sets player ip in data file
     *
     * @param ip player ip
     */
    public void setIP(String ip){
        if(this.getOnlinePlayer() == null) return;
        this.yamlConfiguration.set("ip", ip);
        this.savePlayerDataFile();
    }

    /**
     * Gets player ip from data file
     *
     * @return player ip
     */
    public String getIP(){
        return this.yamlConfiguration.getString("ip");
    }

    /**
     * Gets offline player by UUID
     *
     * @return offline player
     */
    @Nonnull
    public OfflinePlayer getOfflinePlayer(){
        return Bukkit.getOfflinePlayer(uuid);
    }

    /**
     * Gets online player by offline player
     *
     * @return online player if it's online, else null
     */
    @Nullable
    public Player getOnlinePlayer(){
        return this.onlinePlayer == null
                ? this.onlinePlayer = this.getOfflinePlayer().getPlayer() != null && this.getOfflinePlayer().isOnline()
                    ? this.getOfflinePlayer().getPlayer()
                    : null
                : this.onlinePlayer;
    }

    /**
     * Gets player ID by UUID
     *
     * @return player ID
     */
    public int getID(){
        return new PlayerID().getPlayerID(this.uuid);
    }

    /**
     * Gets player nickname by offline player
     *
     * @return player nickname
     */
    public String getNickname(){
        return getOfflinePlayer().getName();
    }


    /**
     * Gets player firstname from data file
     *
     * @return player firstname if it's != null, else returns default firstname
     */
    @Nonnull
    public String getFirstname(){
        return this.firstname == null ? this.firstname = this.yamlConfiguration.getString("name.firstname", "Иван") : this.firstname;
    }

    /**
     * Sets player firstname in data file
     *
     * @param firstname player's first name
     */
    public void setFirstname(@Nonnull String firstname){
        if(!this.hasPlayerDataFile()) return;
        this.yamlConfiguration.set("name.firstname", firstname);
        this.savePlayerDataFile();
    }

    /**
     * Gets player lastname from data file
     *
     * @return player lastname if it's != null, else returns default lastname
     */
    public String getLastname(){
        return this.lastname == null ? this.lastname = this.yamlConfiguration.getString("name.lastname", "Иванов") : this.lastname;
    }

    /**
     * Sets player lastname in data file
     *
     * @param lastname player's last name
     */
    public void setLastName(@Nonnull String lastname){
        if(!this.hasPlayerDataFile()) return;
        this.yamlConfiguration.set("name.lastname", lastname);
        this.savePlayerDataFile();
    }


    /**
     * Gets player patronymic from data file
     *
     * @return player patronymic if it's != null, else returns default patronymic
     */
    @Nonnull
    public String getPatronymic(){
        return this.patronymic == null ? this.patronymic = this.yamlConfiguration.getString("name.patronymic", "Иваныч") : this.patronymic;
    }

    /**
     * Sets patronymic of player in data file
     *
     * @param patronymic player's patronymic
     */
    public void setPatronymic(@Nonnull String patronymic){
        if(!this.hasPlayerDataFile()) return;
        this.yamlConfiguration.set("name.patronymic", patronymic);
        this.savePlayerDataFile();
    }

    /**
     * Gets player pronouns from data file
     *
     * @return player pronouns if it's != null, else returns null
     */
    @Nullable
    public Pronouns getPronouns(){
        return Pronouns.getPronounsByString(this.yamlConfiguration.getString("pronouns", "NULL"));
    }

    /**
     * Sets player pronouns in data file
     *
     * @param pronouns player pronouns
     */
    public void setPronouns(@Nonnull Pronouns pronouns){
        if(!this.hasPlayerDataFile()) return;
        this.yamlConfiguration.set("pronouns", pronouns.name());
        this.savePlayerDataFile();
    }

    /**
     * Gets player resource pack type from data file
     *
     * @return player resource pack type if it's != null, else returns null
     */
    @Nullable
    public ResourcePackType getResourcePackType(){
        return ResourcePackType.getResourcePackByString(this.yamlConfiguration.getString("resource-pack", "NULL"));
    }

    /**
     * Sets resource pack type in data file
     *
     * @param resourcePackType resource pack type : "FULL/LITE/NONE"
     */
    public void setResourcePackType(@Nonnull ResourcePackType resourcePackType){
        if(!this.hasPlayerDataFile()) return;
        this.yamlConfiguration.set("resource-pack", resourcePackType.name());
        savePlayerDataFile();
    }

    /**
     * Gets player disk type from data file
     *
     * @return player disk type if it's != null, else returns default disk type
     */
    @Nonnull
    public DiskType getDiskType(){
        return DiskType.getDiskTypeByString(this.yamlConfiguration.getString("disk-type", "DROPBOX"));
    }

    /**
     * Sets disk type in data file
     *
     * @param diskType disk type : "DROPBOX/YANDEX_DISK"
     */
    public void setDiskType(@Nonnull DiskType diskType){
        if(!this.hasPlayerDataFile()) return;
        this.yamlConfiguration.set("disk-type", diskType.name());
        savePlayerDataFile();
    }

    /**
     * @return True if player is muted
     */
    public boolean isMuted(){
        return this.yamlConfiguration.getBoolean("bans.muted", false);
    }

    /**
     * Mutes/unmutes player
     *
     * @param value mute value
     * @param time mutes for time
     * @param reason mute reason
     */
    public void setMuted(boolean value, long time, @Nullable String reason){
        if(!this.hasPlayerDataFile()) return;
        this.yamlConfiguration.set("bans.muted", value);
        this.yamlConfiguration.set("time.muted-to", time);
        this.yamlConfiguration.set("bans.mute-reason", reason);
        if(value){
            if(this.getOnlinePlayer() != null){
                ChatUtils.sendWarning(this.getOnlinePlayer(), "Вы были замучены : " + "\n    - Причина : \"" + reason + "\"\n    - До : " + new Date(time));
            }
        } else if(this.getOnlinePlayer() != null){
            ChatUtils.sendFine(this.getOnlinePlayer(), "Вы были размучены");
        }
        savePlayerDataFile();
    }

    /**
     * Gets player mute reason if it's != null, else returns default reason
     *
     * @return player mute reason
     */
    @Nonnull
    public String getMuteReason(){
        return this.yamlConfiguration.getString("bans.mute-reason", "неизвестно");
    }

    /**
     * @return True if player is banned
     */
    public boolean isBanned(){
        return this.yamlConfiguration.getBoolean("bans.banned", false);
    }

    /**
     * Bans/unbans player
     *
     * @param value ban value
     * @param time bans for time
     * @param reason ban reason
     */
    public void setBanned(boolean value, long time, @Nullable String reason, @Nullable String source){
        if(!this.hasPlayerDataFile()) return;
        this.yamlConfiguration.set("bans.banned", value);
        this.yamlConfiguration.set("time.banned-to", time);
        this.yamlConfiguration.set("bans.ban-reason", reason);
        if(value){
            Bukkit.getBanList(BanList.Type.NAME).addBan(this.getNickname(), reason, new Date(time), source);
            Bukkit.getBanList(BanList.Type.IP).addBan(this.getIP(), reason, new Date(time), source);
            if(this.getOnlinePlayer() != null) {
                Bukkit.getBanList(BanList.Type.IP).addBan(Objects.requireNonNull(this.getOnlinePlayer().getAddress()).getHostName(), reason, new Date(time), source);
                this.getOnlinePlayer().kickPlayer(
                        ChatColor.RED + "\n§lВы были забанены"
                                + ChatColor.DARK_GRAY + "\n\n<---====+====--->"
                                + ChatColor.GRAY + "\nПричина :\n\""
                                + reason
                                + "\"\n До : \n"
                                + new Date(time)
                                + ChatColor.DARK_GRAY + "\n<---====+====--->\n"
                );
            }
        } else {
            Bukkit.getBanList(BanList.Type.NAME).pardon(this.getNickname());
            Bukkit.getBanList(BanList.Type.IP).pardon(this.getIP());
        }
        savePlayerDataFile();
    }

    /**
     * Gets player ban reason if it's != null, else returns default reason
     *
     * @return player ban reason
     */
    @Nonnull
    public String getBanReason(){
        return this.yamlConfiguration.getString("bans.ban-reason", "неизвестно");
    }

    /**
     * Gets player first join time in milliseconds
     *
     * @return player first join time
     */
    public long getFirstJoin(){
        return this.yamlConfiguration.getLong("time.first-join", 0);
    }

    /**
     * Gets player banned to time in milliseconds
     *
     * @return player banned to time
     */
    public long getBannedTo(){
        return this.yamlConfiguration.getLong("time.banned-to", 0);
    }

    /**
     * Gets player muted to time in milliseconds
     *
     * @return player muted to time
     */
    public long getMutedTo(){
        return this.yamlConfiguration.getLong("time.muted-to", 0);
    }

    /**
     * Teleport player to dark world and adds some effects
     */
    public void teleportToDarkWorld(){
        if(this.getOnlinePlayer() == null) return;
        final Player player = this.getOnlinePlayer();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(new Location(Main.worldDark,  0.0d, 0.0d, 0.0d));
        player.setGravity(false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 255, true, false));
    }

    /**
     * Teleport player to last leave location and removes some effects
     */
    public void teleportToLastLeaveLocation(){
        if(this.getOnlinePlayer() == null) return;
        final Player player = this.getOnlinePlayer();
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(this.getLastLeaveLocation());
        player.setGravity(true);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        ChatUtils.broadcastJoinMessage(this, this.getOnlinePlayer());
    }

    /**
     * Gets player last leave location from data file
     *
     * @return player last leave location
     */
    @Nonnull
    public Location getLastLeaveLocation(){
        return new Location(
                Bukkit.getWorld(this.yamlConfiguration.getString("locations.last-leave-location.world", "world")),
                this.yamlConfiguration.getDouble("locations.last-leave-location.x", 54.0d),
                this.yamlConfiguration.getDouble("locations.last-leave-location.y", 65.0d),
                this.yamlConfiguration.getDouble("locations.last-leave-location.z", -23.0d),
                (float) this.yamlConfiguration.getDouble("locations.last-leave-location.yaw", 0.0f),
                (float) this.yamlConfiguration.getDouble("locations.last-leave-location.pitch", 0.0f)
        );
    }

    /**
     * Sets last leave location of player
     *
     * @param world last leave world
     * @param x last leave x coordinate
     * @param y last leave y coordinate
     * @param z last leave z coordinate
     * @param yaw last leave yaw
     * @param pitch last leave pitch
     */
    public void setLastLeaveLocation(@Nonnull World world, double x, double y, double z, float yaw, float pitch){
        this.yamlConfiguration.set("locations.last-leave-location.world", world.getName());
        this.yamlConfiguration.set("locations.last-leave-location.x", x);
        this.yamlConfiguration.set("locations.last-leave-location.y", y);
        this.yamlConfiguration.set("locations.last-leave-location.z", z);
        this.yamlConfiguration.set("locations.last-leave-location.yaw", yaw);
        this.yamlConfiguration.set("locations.last-leave-location.pitch", pitch);
        this.savePlayerDataFile();
    }

    /**
     * Gets player last death location from data file
     *
     * @return player last death location
     */
    @Nonnull
    public Location getLastDeathLocation(){
        return new Location(
                Bukkit.getWorld(this.yamlConfiguration.getString("locations.last-death-location.world", "world")),
                this.yamlConfiguration.getDouble("locations.last-death-location.x"),
                this.yamlConfiguration.getDouble("locations.last-death-location.y"),
                this.yamlConfiguration.getDouble("locations.last-death-location.z"),
                (float) this.yamlConfiguration.getDouble("locations.last-death-location.yaw"),
                (float) this.yamlConfiguration.getDouble("locations.last-death-location.pitch")
        );
    }

    /**
     * Sets last death location of player
     *
     * @param world last death world
     * @param x last death x coordinate
     * @param y last death y coordinate
     * @param z last death z coordinate
     * @param yaw last death yaw
     * @param pitch last death pitch
     */
    public void setLastDeathLocation(@Nonnull World world, double x, double y, double z, float yaw, float pitch){
        this.yamlConfiguration.set("locations.last-death-location.world", world.getName());
        this.yamlConfiguration.set("locations.last-death-location.x", x);
        this.yamlConfiguration.set("locations.last-death-location.y", y);
        this.yamlConfiguration.set("locations.last-death-location.z", z);
        this.yamlConfiguration.set("locations.last-death-location.yaw", yaw);
        this.yamlConfiguration.set("locations.last-death-location.pitch", pitch);
        this.savePlayerDataFile();
    }

    /**
     * Creates new player data file in "plugins/msUtils/players/uuid.yml"
     */
    public void createPlayerDataFile(){
        if(this.getOnlinePlayer() != null){
            this.yamlConfiguration.set("nickname", this.getNickname());
            this.yamlConfiguration.set("ip", this.getOnlinePlayer().getAddress() == null ? null : this.getOnlinePlayer().getAddress().getHostName());
            this.yamlConfiguration.set("time.first-join", System.currentTimeMillis());
            this.savePlayerDataFile();
            ChatUtils.sendFine(null, "Создан файл с данными игрока : \"" + this.getNickname() + "\" с названием : \"" + this.uuid + ".yml\"");
        } else {
            ChatUtils.sendError(null, "PlayerInfo#createPlayerDataFile() online player = null");
        }
    }

    /**
     * Saves player data file in "plugins/msUtils/players/uuid.yml"
     */
    public void savePlayerDataFile() {
        try {
            this.yamlConfiguration.save(this.dataFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @return True if player data file exists
     */
    public boolean hasPlayerDataFile(){
        return this.dataFile.exists();
    }

    /**
     * @return True if player has name
     */
    public boolean hasName(){
        return this.yamlConfiguration.getString("name.firstname") != null
                && this.yamlConfiguration.getString("name.lastname") != null
                && this.yamlConfiguration.getString("name.patronymic") != null;
    }

}