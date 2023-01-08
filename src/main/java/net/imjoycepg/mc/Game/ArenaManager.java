package net.imjoycepg.mc.Game;

import net.imjoycepg.mc.KBFFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ArenaManager{
    private final List<Arena> arenas = new ArrayList<>();

    public Arena createArena(String nameArena){
        KBFFA.getInstance().getArenaFile().createMap(nameArena);

        FileConfiguration config = new YamlConfiguration();
        config.set("Name", nameArena);
        KBFFA.getInstance().getArenaFile().saveMap(nameArena, config);

        Arena arena = new Arena();
        arena.setNameArena(nameArena);

        arenas.add(arena);

        return arena;
    }

    public void addPlayer(Player player){
        if(isInGame(player)){
            player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Game.isInGame"));
            return;
        }

        Arena currentArena = getCurrentArena();

        if (currentArena == null) {
            for (Arena arena : getArenas()) {
                if (arena.getPlayers().size() > 0) {
                    currentArena = arena;
                    break;
                }
            }
        }
        if (currentArena != null) {
            FileConfiguration config = KBFFA.getInstance().getArenaFile().getArenaFile(currentArena.getNameArena());
            currentArena.getPlayers().add(player.getUniqueId());
            player.teleport(deserializeLocation(config.getString("SpawnArena")));
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.setFlying(false);
            player.setAllowFlight(false);
        } else {
            player.sendMessage("No hay ningún mapa disponible en este momento.");
        }
    }

    public String serializeLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }

    public Location deserializeLocation(String s) {
        String[] parts = s.split(",");
        World w = Bukkit.getServer().getWorld(parts[0]);
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);
        float yaw = Float.parseFloat(parts[4]);
        float pitch = Float.parseFloat(parts[5]);
        return new Location(w, x, y, z, yaw, pitch);
    }

    public Arena getCurrentArena(){
        for(Arena arena : arenas){
            if(arena.getGameState() == GameState.IN_GAME){
                return arena;
            }
        }
        return null;
    }

    public Arena getRandomArena(GameState state){
        List<Arena> filteredArenas = new ArrayList<>();
        for(Arena arena : arenas){
            if(arena.getGameState() == state){
                filteredArenas.add(arena);
            }
        }

        if(filteredArenas.size() == 0) return null;

        Random rand = new Random();
        return arenas.get(rand.nextInt(filteredArenas.size()));
    }

    public void removePlayer(Player player){
        Arena a = null;
        for(Arena arena : arenas){
            if(arena.getPlayers().contains(player.getUniqueId())){
                a = arena;
            }
        }

        if(a == null){
            player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Game.noIsGame"));
            return;
        }

        a.getPlayers().remove(player.getUniqueId());
        player.teleport(KBFFA.getInstance().getLocationUtil().deserialize(KBFFA.getInstance().getSettings().getString("MainLobby")));
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setFireTicks(0);
    }

    public void removeArena(String nameArena){
        Arena arena = getArena(nameArena);
        if(arena != null){
            arenas.remove(arena);
            KBFFA.getInstance().getArenaFile().deleteArenaFile(nameArena);
        }
    }

    public Arena getArena(String nameArena){
        for(Arena arena : arenas){
            if(arena.getNameArena().equalsIgnoreCase(nameArena)){
                return arena;
            }
        }
        return null;
    }

    public boolean isInGame(Player player){
        for(Arena arena : arenas){
            if(arena.getPlayers().contains(player.getUniqueId())){
                return true;
            }
        }
        return false;
    }

    public List<Arena> getArenas(){
        return arenas;
    }

    public void loadArenas(){
        File arenaFolder = new File(KBFFA.getInstance().getDataFolder(), "Arenas");
        if(!arenaFolder.exists()){
            arenaFolder.mkdir();
        }

        for(File file : Objects.requireNonNull(arenaFolder.listFiles())){
            if(file.getName().endsWith(".yml")){
                String name = file.getName().replace(".yml", "");

                FileConfiguration config = KBFFA.getInstance().getArenaFile().getArenaFile(name);

                Location spawnArena = deserializeLocation(config.getString("SpawnArena"));

                Location MinArena = deserializeLocation(config.getString("MinArena"));

                Location MaxArena = deserializeLocation(config.getString("MaxArena"));

                Location MinLobby = deserializeLocation(config.getString("MinLobby"));

                Location MaxLobby = deserializeLocation(config.getString("MaxLobby"));

                Arena arena = new Arena();
                arena.setNameArena(name);
                arena.setSpawnArena(spawnArena);
                arena.setMinArena(MinArena);
                arena.setMaxArena(MaxArena);
                arena.setMinLobby(MinLobby);
                arena.setMaxLobby(MaxLobby);
                arena.setGameState(GameState.WAITING);

                arenas.add(arena);
            }
        }
    }

    public void saveArenas(){
        for(Arena arena : arenas) {
            FileConfiguration config = KBFFA.getInstance().getArenaFile().getArenaFile(arena.getNameArena());

            config.set("SpawnArena", serializeLocation(arena.getSpawnArena()));

            config.set("MinArena", serializeLocation(arena.getMinArena()));

            config.set("MaxArena", serializeLocation(arena.getMaxArena()));

            config.set("MinLobby", serializeLocation(arena.getMinLobby()));

            config.set("MaxLobby", serializeLocation(arena.getMaxLobby()));



            KBFFA.getInstance().getArenaFile().saveMap(arena.getNameArena(), config);
        }
    }

}
