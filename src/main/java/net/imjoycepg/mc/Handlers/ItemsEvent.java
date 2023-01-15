package net.imjoycepg.mc.Handlers;

import net.imjoycepg.mc.KBFFA;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ItemsEvent implements Listener {

    @EventHandler
    public void onItemDropEvent(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if(KBFFA.getInstance().getArenaManager().isInGame(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickupEvent(PlayerPickupItemEvent event){
        Player player = event.getPlayer();
        if(KBFFA.getInstance().getArenaManager().isInGame(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlocksPlaceEvent(BlockPlaceEvent event){
        final Block block = event.getBlock();
        Player player = event.getPlayer();
        if(KBFFA.getInstance().getArenaManager().isInGame(player)) {
            if(block.getType() == Material.STAINED_CLAY) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.setType(Material.AIR);
                    }
                }.runTaskLater(KBFFA.getInstance(), 5 * 20);
            }
            if(block.getType() == Material.GOLD_PLATE){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.setType(Material.AIR);
                    }
                }.runTaskLater(KBFFA.getInstance(), 20 * 5L);
            }
        }
    }

    @EventHandler
    public void onPlayerJumpPads(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if(block == null) return;
        if(KBFFA.getInstance().getArenaManager().isInGame(player)) {
            if(block.getType() == Material.GOLD_PLATE && event.getAction() == Action.PHYSICAL){
                player.setVelocity(player.getLocation().getDirection().multiply(1.5D));
                player.setVelocity(new Vector(player.getVelocity().getX(), 5, player.getVelocity().getZ()));
            }
        }
    }
}
