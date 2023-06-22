package com.github.minersstudios.msutils.utils;

import com.github.minersstudios.msutils.MSUtils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class MSPlayerUtils {
    /**
     * Regex supports all <a href="https://jrgraphix.net/r/Unicode/0400-04FF">cyrillic</a> characters
     */
    public static final @NotNull String NAME_REGEX = "[-Ѐ-ӿ]+";

    private MSPlayerUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Adds player to the "hide_tags" {@link Team} and sets the {@link Scoreboard} for player
     *
     * @param player the player
     */
    public static void hideNameTag(@NotNull Player player) {
        MSUtils.getScoreboardHideTagsTeam().addEntry(player.getName());
        player.setScoreboard(MSUtils.getScoreboardHideTags());
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean matchesNameRegex(@Nullable String string) {
        return string != null && string.matches(NAME_REGEX);
    }
}
