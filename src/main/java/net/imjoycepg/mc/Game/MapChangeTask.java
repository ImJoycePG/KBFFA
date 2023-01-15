package net.imjoycepg.mc.Game;

import net.imjoycepg.mc.KBFFA;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class MapChangeTask extends BukkitRunnable {
    @Override
    public void run() {

        Arena currentArena = KBFFA.getInstance().getArenaManager().getCurrentArena();
        if (currentArena == null) {
            currentArena = KBFFA.getInstance().getArenaManager().getRandomArena(GameState.WAITING);
            if (currentArena == null) {
                KBFFA.getInstance().getLogger().info(KBFFA.getInstance().getMessages().getString("Game.noAvailable"));
                this.cancel();
                return;
            }
            currentArena.setGameState(GameState.IN_GAME);
        } else {
            Arena nextArena = KBFFA.getInstance().getArenaManager().getRandomArena(GameState.WAITING);

            if (nextArena == null) {
                KBFFA.getInstance().getLogger().info(KBFFA.getInstance().getMessages().getString("Game.noAvailable"));
                this.cancel();
                return;
            }

            for (int i = 5; i > 0; i--) {
                for (UUID uuid : currentArena.getPlayers()) {
                    Bukkit.getPlayer(uuid).sendMessage(KBFFA.getInstance().getMessages().getString("Game.coolDownArena")
                            .replace("%coolDownArena%", String.valueOf(i)));

                    Bukkit.getPlayer(uuid).sendTitle(ChatColor.DARK_RED + KBFFA.getInstance().getMessages().getString("Game.coolDownTitle")
                            .replace("%coolDownArenaTitle%", String.valueOf(i))
                            .replace("4", ChatColor.DARK_RED + "4")
                            .replace("3", ChatColor.RED + "3")
                            .replace("2", ChatColor.RED + "2")
                            .replace("1", ChatColor.GOLD + "1"), "");
                    Bukkit.getPlayer(uuid).playSound(Bukkit.getPlayer(uuid).getLocation(), Sound.NOTE_BASS , 1F, 1F);

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            FileConfiguration config = KBFFA.getInstance().getArenaFile().getArenaFile(nextArena.getNameArena());


            for (UUID uuid : currentArena.getPlayers()) {

                Bukkit.getPlayer(uuid).sendMessage(KBFFA.getInstance().getMessages().getString("Game.nextArena").replace("%nextArena%", nextArena.getNameArena()));
                Bukkit.getPlayer(uuid).playSound(Bukkit.getPlayer(uuid).getLocation(), Sound.EXPLODE , 1F, 1F);
                Bukkit.getPlayer(uuid).teleport(KBFFA.getInstance().getArenaManager().deserializeLocation(config.getString("SpawnArena")));
            }

            currentArena.setGameState(GameState.WAITING);
            nextArena.setGameState(GameState.IN_GAME);

        }
    }
}
