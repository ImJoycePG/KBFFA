package net.imjoycepg.mc.Handlers;

import net.imjoycepg.mc.KBFFA;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class LobbyEvent implements Listener {

    @EventHandler
    public void onProtectBreakLobby(BlockBreakEvent event){
        Block block = event.getBlock();

        if(KBFFA.getInstance().getArenaManager().isInsideCuboid(block.getLocation(), "MinLobby", "MaxLobby")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProtectPlaceLobby(BlockPlaceEvent event){
        Block block = event.getBlock();

        if(KBFFA.getInstance().getArenaManager().isInsideCuboid(block.getLocation(), "MinLobby", "MaxLobby")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFallLobby(EntityDamageEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL || event.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK){
            if(event.getEntity() instanceof  Player){
                Player player = (Player) event.getEntity();
                if(KBFFA.getInstance().getArenaManager().isInsideCuboid(player.getLocation(),
                        "MinLobby", "MaxLobby")) {

                    event.setCancelled(true);

                }
            }
        }
    }

    @EventHandler
    public void onProtectPvPLobby(EntityDamageByEntityEvent event){
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();

        if (attacker instanceof Player && victim instanceof Player) {
            if(KBFFA.getInstance().getArenaManager().isInsideCuboid(attacker.getLocation(),
                    "MinLobby", "MaxLobby") && KBFFA.getInstance().getArenaManager().isInsideCuboid(victim.getLocation(),
                            "MinLobby", "MaxLobby")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProtectFoodLobby(FoodLevelChangeEvent event){
        Player player = (Player) event.getEntity();
        if(KBFFA.getInstance().getArenaManager().isInsideCuboid(player.getLocation(),
                "MinLobby", "MaxLobby")){
            event.setCancelled(true);
        }
    }

}
