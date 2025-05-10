package minigame.minecraftFlagDuel;

import minigame.minecraftFlagDuel.capturePoint.DistanceListener;
import minigame.minecraftFlagDuel.capturePoint.TargetParticleEffect;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftFlagDuel extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new DistanceListener(), this);
        new TargetParticleEffect(5).start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
