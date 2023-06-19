package com.github.minersstudios.msutils.anomalies.actions;

import com.destroystokyo.paper.ParticleBuilder;
import com.github.minersstudios.msutils.anomalies.AnomalyAction;
import com.github.minersstudios.msutils.anomalies.AnomalyIgnorableItems;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpawnParticlesAction extends AnomalyAction {
    private final @NotNull List<ParticleBuilder> particleBuilderList;

    public SpawnParticlesAction(
            long time,
            int percentage,
            @NotNull List<ParticleBuilder> particleBuilderList
    ) {
        super(time, percentage);
        this.particleBuilderList = particleBuilderList;
    }

    @Override
    public void doAction(@NotNull Player player, @Nullable AnomalyIgnorableItems ignorableItems) {
        for (ParticleBuilder particleBuilder : this.particleBuilderList) {
            particleBuilder.location(player.getLocation()).spawn();
        }
    }

    public @NotNull List<ParticleBuilder> getParticles() {
        return this.particleBuilderList;
    }
}
