package net.imjoycepg.mc.Handlers;

import net.imjoycepg.mc.Game.Arena;
import net.imjoycepg.mc.KBFFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArenaEvent implements Listener {

    @EventHandler
    public void onProtectBreakArena(BlockBreakEvent event){
        Block block = event.getBlock();

        if(KBFFA.getInstance().getArenaManager().isInsideCuboid(block.getLocation(), "MinArena", "MaxArena")){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onProtectFoodArena(FoodLevelChangeEvent event){
        Player player = (Player) event.getEntity();
        if(KBFFA.getInstance().getArenaManager().isInsideCuboid(player.getLocation(),
                "MinArena", "MaxArena")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFallArena(EntityDamageEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL || event.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK){
            if(event.getEntity() instanceof  Player){
                Player player = (Player) event.getEntity();
                if(KBFFA.getInstance().getArenaManager().isInsideCuboid(player.getLocation(),
                        "MinArena", "MaxArena")) {
                    event.setDamage(0.0);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPvPDamage(EntityDamageByEntityEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            if(event.getEntity() instanceof Player){
                Player player = (Player) event.getEntity();
                if(KBFFA.getInstance().getArenaManager().isInsideCuboid(player.getLocation(),
                        "MinArena", "MaxArena")) {
                    event.setDamage(0.0);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerFastDeathArena(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location newPos = event.getTo();
        if(KBFFA.getInstance().getArenaManager().isInGame(player)) {
            if (newPos.getY() < 0) {
                player.damage(100);
            }
        }
    }

    @EventHandler
    public void onPlayerDeathServer(PlayerDeathEvent event){
        Player player = event.getEntity();
        if(KBFFA.getInstance().getArenaManager().isInGame(player)){
            event.setDroppedExp(0);
            event.setKeepInventory(true);
            player.removePotionEffect(PotionEffectType.SPEED);
        }
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onPlayerRespawnServer(PlayerRespawnEvent event){
        Player player = event.getPlayer();

        if(KBFFA.getInstance().getArenaManager().isInGame(player)) {
            Arena arena = KBFFA.getInstance().getArenaManager().getCurrentArena();
            FileConfiguration config = KBFFA.getInstance().getArenaFile().getArenaFile(arena.getNameArena());
            event.setRespawnLocation(KBFFA.getInstance().getArenaManager().deserializeLocation(config.getString("SpawnArena")));
            KBFFA.getInstance().getArenaManager().giveItems(player);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(KBFFA.getInstance(), () -> {
                if(!(player.hasPotionEffect(PotionEffectType.SPEED))){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 1), true);
                }
            }, (1));
        }
    }

    @EventHandler
    public void onWeatherArenaEvent(WeatherChangeEvent event){
        Arena arena = KBFFA.getInstance().getArenaManager().getCurrentArena();
        FileConfiguration config = KBFFA.getInstance().getArenaFile().getArenaFile(arena.getNameArena());

        World world = KBFFA.getInstance().getArenaManager().deserializeLocation(config.getString("SpawnArena")).getWorld();
        if(event.toWeatherState()){
            event.setCancelled(true);
            world.setStorm(false);
        }

    }

}
