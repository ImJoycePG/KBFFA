package net.imjoycepg.mc.Game;

import net.imjoycepg.mc.KBFFA;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.UUID;

public class ArrowTask implements Runnable{
    private final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

    @Override
    public void run() {
        for(UUID uuid : KBFFA.getInstance().getArenaManager().getCurrentArena().getPlayers()){
            PlayerInventory inventory = Bukkit.getPlayer(uuid).getInventory();
            if(inventory.contains(Material.ARROW)) continue;
            ItemStack arrow = new ItemStack(Material.ARROW, 1);
            inventory.setItem(27, arrow);
            Bukkit.getPlayer(uuid).playSound(Bukkit.getPlayer(uuid).getLocation(), Sound.ORB_PICKUP , 1F, 1F);
        }
    }

    public void schedulerArrowTask(){
        scheduler.scheduleSyncRepeatingTask(KBFFA.getInstance(), this, 0L, 20*30L);
    }
}
