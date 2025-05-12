package minigame.minecraftFlagDuel;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import minigame.minecraftFlagDuel.capturePoint.DistanceListener;
import minigame.minecraftFlagDuel.capturePoint.TargetParticleEffect;
import minigame.minecraftFlagDuel.capturePoint.TargetTeamListener;
import minigame.minecraftFlagDuel.capturePoint.VillagerShopManager;
import minigame.minecraftFlagDuel.commands.ShuffleTeam;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftFlagDuel extends JavaPlugin {

    @Override
    public void onEnable() {
//        getServer().getPluginManager().registerEvents(new DistanceListener(), this);
        new TargetParticleEffect(4).start();

        LiteralCommandNode<CommandSourceStack> shuffleTeams = Commands.literal("shuffleteam")
                .executes(ShuffleTeam::shuffleTeam).build();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(shuffleTeams);
        });

        this.getServer().getScheduler().runTaskTimer(this, () -> {
            TargetTeamListener.checkAndNotifyTeam(5.5);
        }, 0L, 1L); // 初始延迟0刻后开始，之后每隔20刻(即1秒)重复一次

        new VillagerShopManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
