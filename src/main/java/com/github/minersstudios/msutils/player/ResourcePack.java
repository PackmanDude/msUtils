package com.github.minersstudios.msutils.player;

import com.github.minersstudios.msutils.inventory.ResourcePackMenu;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.logging.Level;

import static com.github.minersstudios.msutils.MSUtils.getConfigCache;
import static com.github.minersstudios.msutils.MSUtils.getInstance;

public class ResourcePack {
    private final @NotNull String hash;
    private final @NotNull String url;
    private static String user;
    private static String repo;
    public static String tagName;

    public ResourcePack(
            @NotNull String hash,
            @NotNull String url
    ) {
        this.hash = hash;
        this.url = url;
    }

    public static void init() {
        user = getConfigCache().user;
        repo = getConfigCache().repo;
        Map.Entry<Boolean, String> latestTagName = getLatestTagName();
        tagName = latestTagName.getValue();
        boolean hasNoUpdates =
                tagName.equals(getConfigCache().version)
                && latestTagName.getKey()
                && getConfigCache().fullHash != null
                && getConfigCache().liteHash != null;

        String fullFileName = String.format(getConfigCache().fullFileName, tagName);
        String liteFileName = String.format(getConfigCache().liteFileName, tagName);

        String fullUrl = "https://github.com/" + user + "/" + repo + "/releases/download/" + tagName + "/" + fullFileName;
        String liteUrl = "https://github.com/" + user + "/" + repo + "/releases/download/" + tagName + "/" + liteFileName;

        String fullHash = hasNoUpdates
                ? getConfigCache().fullHash
                : generateHash(fullUrl, fullFileName);
        String liteHash = hasNoUpdates
                ? getConfigCache().liteHash
                : generateHash(liteUrl, liteFileName);

        Type.FULL.resourcePack = new ResourcePack(fullHash, fullUrl);
        Type.LITE.resourcePack = new ResourcePack(liteHash, liteUrl);

        if (!hasNoUpdates) {
            YamlConfiguration configYaml = getConfigCache().configYaml;

            configYaml.set("resource-pack.version", tagName);
            configYaml.set("resource-pack.full.hash", fullHash);
            configYaml.set("resource-pack.lite.hash", liteHash);

            deleteResourcePackFiles(getConfigCache().fullFileName);
            deleteResourcePackFiles(getConfigCache().liteFileName);

            getConfigCache().save();
        }
    }

    private static void deleteResourcePackFiles(@NotNull String fileName) throws SecurityException {
        File[] files = getInstance().getPluginFolder().listFiles();

        if (files != null) {
            for (File fileFromList : files) {
                if (
                        fileFromList.getName().matches(String.format(fileName, ".*"))
                        && !fileFromList.delete()
                ) {
                    throw new SecurityException("File deletion failed");
                }
            }
        }
    }

    @Contract("_, _ -> new")
    private static @NotNull String generateHash(
            @NotNull String url,
            @NotNull String fileName
    ) throws RuntimeException {
        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());
            FileOutputStream out = new FileOutputStream(getInstance().getPluginFolder() + "/" + fileName);

            out.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            out.close();
            readableByteChannel.close();
            return bytesToHexString(createSha1(new File(getInstance().getPluginFolder(), fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte @NotNull [] createSha1(@NotNull File file) throws RuntimeException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            int n = 0;
            byte[] buffer = new byte[1024];

            while (n != -1) {
                n = fileInputStream.read(buffer);
                if (n > 0) {
                    digest.update(buffer, 0, n);
                }
            }

            fileInputStream.close();
            return digest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull String bytesToHexString(byte @NotNull [] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            int value = b & 0xFF;

            if (value < 16) {
                stringBuilder.append("0");
            }

            stringBuilder.append(Integer.toHexString(value));
        }
        return stringBuilder.toString();
    }

    private static @NotNull Map.Entry<Boolean, String> getLatestTagName() throws RuntimeException {
        URI uri = URI.create("https://api.github.com/repos/" + user + "/" + repo + "/tags");

        try {
            String json = IOUtils.toString(uri, StandardCharsets.UTF_8);
            JsonArray tags = JsonParser.parseString(json).getAsJsonArray();
            JsonObject latestTag = tags.get(0).getAsJsonObject();
            return Map.entry(true, latestTag.get("name").getAsString());
        } catch (IOException e) {
            String configTagName = getConfigCache().version;

            if (configTagName == null) {
                throw new RuntimeException("Apparently the API rate limit has been exceeded\nRequest URL : " + uri, e);
            }

            Bukkit.getLogger().log(Level.SEVERE, "Apparently the API rate limit has been exceeded. Plugin will use existing version\nRequest URL : " + uri, e);
            return Map.entry(false, configTagName);
        }
    }

    /**
     * Sets resource pack for player
     *
     * @param playerInfo player info
     */
    public static void setResourcePack(@NotNull PlayerInfo playerInfo) {
        if (playerInfo.getOnlinePlayer() == null) return;

        Player player = playerInfo.getOnlinePlayer();
        PlayerSettings playerSettings = playerInfo.getPlayerFile().getPlayerSettings();
        ResourcePack.Type type = playerSettings.getResourcePackType();

        if (
                Type.FULL.getUrl() == null
                || Type.FULL.getHash() == null
                || Type.LITE.getUrl() == null
                || Type.LITE.getHash() == null
        ) {
            playerInfo.kickPlayer("Вы были кикнуты", "Сервер ещё не запущен");
            return;
        }

        assert type.getUrl() != null;
        assert type.getHash() != null;

        if (type == ResourcePack.Type.NULL) {
            ResourcePackMenu.open(player);
        } else {
            player.setResourcePack(type.getUrl(), type.getHash());
        }
    }

    public enum Type {
        FULL, LITE, NONE, NULL;

        private @Nullable ResourcePack resourcePack;

        Type() {
            this.resourcePack = null;
        }

        public @Nullable String getHash() {
            return this.resourcePack == null ? null : this.resourcePack.hash;
        }

        public @Nullable String getUrl() {
            return this.resourcePack == null ? null : this.resourcePack.url;
        }
    }
}
