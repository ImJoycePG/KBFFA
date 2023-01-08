package net.imjoycepg.mc.Game;

import net.imjoycepg.mc.KBFFA;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class MapChangeTask extends BukkitRunnable {
    private final ArenaManager arenaManager = new ArenaManager();

    @Override
    public void run() {

        if(arenaManager.getCurrentArena() != null) {
            KBFFA.getInstance().getLogger().severe("No hay mapa actual");
            return;
        }

        Arena nextArena = arenaManager.getRandomArena(GameState.WAITING);
        if(nextArena == null) {
            KBFFA.getInstance().getLogger().severe("No hay proximos mapas");
            return;
        }

        Arena currentArena = arenaManager.getCurrentArena();
        if(currentArena != null){
            currentArena.setGameState(GameState.WAITING);
            for(UUID uuid : currentArena.getPlayers()){
                Bukkit.getPlayer(uuid).teleport(nextArena.getSpawnArena());
            }
        }

        nextArena.setGameState(GameState.IN_GAME);

    }

}
