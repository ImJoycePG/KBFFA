package net.imjoycepg.mc.Game;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.imjoycepg.mc.KBFFA;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Arena {
    private final List<UUID> players = new ArrayList<>();


    private String nameArena;
    private GameState gameState;

    private Location spawnArena;

    private Location minArena;
    private Location maxArena;

    private Location minLobby;
    private Location maxLobby;

    public void save(){
        FileConfiguration config = KBFFA.getInstance().getArenaFile().getArenaFile(nameArena);

        config.set("SpawnArena", KBFFA.getInstance().getArenaManager().serializeLocation(spawnArena));

        config.set("MinArena", KBFFA.getInstance().getArenaManager().serializeLocation(minArena));

        config.set("MaxArena", KBFFA.getInstance().getArenaManager().serializeLocation(maxArena));

        config.set("MinLobby", KBFFA.getInstance().getArenaManager().serializeLocation(minLobby));

        config.set("MaxLobby", KBFFA.getInstance().getArenaManager().serializeLocation(maxLobby));

        KBFFA.getInstance().getArenaFile().saveMap(nameArena, config);
    }



}
