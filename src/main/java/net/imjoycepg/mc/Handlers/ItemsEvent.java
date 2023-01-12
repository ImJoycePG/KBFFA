package net.imjoycepg.mc.Handlers;

import net.imjoycepg.mc.KBFFA;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemsEvent implements Listener {

    @EventHandler
    public void onItemDropEvent(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if(KBFFA.getInstance().getArenaManager().isInGame(player)){
            event.setCancelled(true);
        }
    }
}
