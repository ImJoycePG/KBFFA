package net.imjoycepg.mc.Handlers;

import net.imjoycepg.mc.KBFFA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralEvent implements Listener {

    @EventHandler
    public void onPlayerJoinServer(PlayerJoinEvent event){
        Player player = event.getPlayer();
        event.setJoinMessage(null);

        if(KBFFA.getInstance().getSettings().getString("MainLobby") != null){
            player.teleport(KBFFA.getInstance().getLocationUtil().deserialize(KBFFA.getInstance().getSettings().getString("MainLobby")));
        }else{
            Bukkit.getLogger().severe(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("General.noMainLobby"));
        }

    }

    @EventHandler
    public void onPlayerLeaveServer(PlayerQuitEvent event){
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        KBFFA.getInstance().getArenaManager().removePlayer(player);
    }
}
