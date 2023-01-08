package net.imjoycepg.mc.Game;

import net.imjoycepg.mc.KBFFA;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ArenaFile {


    public void createMap(String arenaName){
        File arenaFile = new File(KBFFA.getInstance().getDataFolder() + File.separator + "Arenas", arenaName + ".yml");
        if(!arenaFile.exists()){
            try{
                arenaFile.createNewFile();
            }catch (IOException ex){
                KBFFA.getInstance().getLogger().severe(ex.getMessage());
            }
        }
    }

    public FileConfiguration getArenaFile(String arenaName){
        File arenaFile = new File(KBFFA.getInstance().getDataFolder() + File.separator + "Arenas", arenaName + ".yml");
        return YamlConfiguration.loadConfiguration(arenaFile);
    }

    public void saveMap(String arenaName, FileConfiguration arenaFile){
        try{
            arenaFile.save(new File(KBFFA.getInstance().getDataFolder() + File.separator + "Arenas", arenaName + ".yml"));
        }catch (IOException ex){
            KBFFA.getInstance().getLogger().severe(ex.getMessage());
        }
    }

    public void deleteArenaFile (String arenaName){
        File arenaFile = new File(KBFFA.getInstance().getDataFolder() + File.separator + "Arenas", arenaName + ".yml");
        arenaFile.delete();
    }
}
