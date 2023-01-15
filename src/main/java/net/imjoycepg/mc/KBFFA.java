package net.imjoycepg.mc;

import lombok.Getter;
import net.imjoycepg.mc.Commands.ArenaCMD;
import net.imjoycepg.mc.Commands.MainCMD;
import net.imjoycepg.mc.Game.ArenaFile;
import net.imjoycepg.mc.Game.ArenaManager;
import net.imjoycepg.mc.Game.ArrowTask;
import net.imjoycepg.mc.Game.MapChangeTask;
import net.imjoycepg.mc.Handlers.ArenaEvent;
import net.imjoycepg.mc.Handlers.GeneralEvent;
import net.imjoycepg.mc.Handlers.ItemsEvent;
import net.imjoycepg.mc.Handlers.LobbyEvent;
import net.imjoycepg.mc.Util.ConfigFile;
import net.imjoycepg.mc.Util.LocationUtil;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class KBFFA extends JavaPlugin {
    @Getter
    private static KBFFA instance;
    private ConfigFile settings, messages, items;

    private final ArenaManager arenaManager = new ArenaManager();
    private final MapChangeTask mapChangeTask = new MapChangeTask();
    private final ArrowTask arrowTask = new ArrowTask();
    private final ArenaFile arenaFile = new ArenaFile();
    private final LocationUtil locationUtil = new LocationUtil();

    @Override
    public void onEnable() {
        instance = this;
        settings = new ConfigFile(this, "config.yml");
        messages = new ConfigFile(this, "messages.yml");
        items = new ConfigFile(this, "items.yml");


        getCommand("kbffa").setExecutor(new MainCMD());
        getCommand("arena").setExecutor(new ArenaCMD());

        getServer().getPluginManager().registerEvents(new GeneralEvent(), this);
        getServer().getPluginManager().registerEvents(new LobbyEvent(), this);
        getServer().getPluginManager().registerEvents(new ArenaEvent(), this);
        getServer().getPluginManager().registerEvents(new ItemsEvent(), this);

        arenaManager.loadArenas();
        mapChangeTask.runTaskTimer(this, 0, 20L * 60L * settings.getInt("Time.ChangeMap"));
        arrowTask.schedulerArrowTask();
    }

    @Override
    public void onDisable() {
        settings.save();
        messages.save();
        arenaManager.ServerOff();
    }
}
