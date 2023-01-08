package net.imjoycepg.mc;

import lombok.Getter;
import net.imjoycepg.mc.Commands.ArenaCMD;
import net.imjoycepg.mc.Commands.MainCMD;
import net.imjoycepg.mc.Game.ArenaFile;
import net.imjoycepg.mc.Game.ArenaManager;
import net.imjoycepg.mc.Game.GameState;
import net.imjoycepg.mc.Game.MapChangeTask;
import net.imjoycepg.mc.Util.ConfigFile;
import net.imjoycepg.mc.Util.LocationUtil;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class KBFFA extends JavaPlugin {
    @Getter
    private static KBFFA instance;
    private ConfigFile settings, messages;

    private final ArenaManager arenaManager = new ArenaManager();
    private final MapChangeTask mapChangeTask = new MapChangeTask();
    private final ArenaFile arenaFile = new ArenaFile();
    private final LocationUtil locationUtil = new LocationUtil();



    @Override
    public void onEnable() {
        instance = this;
        settings = new ConfigFile(this, "config.yml");
        messages = new ConfigFile(this, "messages.yml");


        this.getCommand("kbffa").setExecutor(new MainCMD());
        this.getCommand("arena").setExecutor(new ArenaCMD());
        arenaManager.loadArenas();

        arenaManager.getArenas().get(0).setGameState(GameState.IN_GAME);

        mapChangeTask.runTaskTimer(this, 0, 20L * 60L * settings.getInt("Time.ChangeMap"));
    }

    @Override
    public void onDisable() {
        settings.save();
        messages.save();
    }
}
