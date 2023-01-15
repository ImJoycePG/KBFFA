package net.imjoycepg.mc.Game;

import lombok.Getter;
import net.imjoycepg.mc.KBFFA;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

public class ArenaManager{
    @Getter
    private final List<Arena> arenas = new ArrayList<>();

    public void createArena(String nameArena){
        KBFFA.getInstance().getArenaFile().createMap(nameArena);

        FileConfiguration config = new YamlConfiguration();
        config.set("Name", nameArena);
        KBFFA.getInstance().getArenaFile().saveMap(nameArena, config);

        Arena arena = new Arena();
        arena.setNameArena(nameArena);

        arenas.add(arena);

    }

    public void addPlayer(Player player){
        if(isInGame(player)){
            player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Game.isInGame"));
            return;
        }

        player.sendMessage(StringUtils.repeat(" \n", 100));

        Arena currentArena = getCurrentArena();

        if (currentArena == null) {
            for (Arena arena : arenas) {
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
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1, true , false));

            giveItems(player);

            for(UUID uuid : currentArena.getPlayers()){
                Bukkit.getPlayer(uuid).sendMessage(KBFFA.getInstance().getMessages().getString("Game.JoinPlayer")
                        .replace("%player%", player.getName()));
            }

        } else {
            player.sendMessage(KBFFA.getInstance().getMessages().getString("Game.noAvailable"));
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
        Random random = new Random();
        int randomIndex = random.nextInt(arenas.size());
        for(Arena arena : arenas){
            if(arena.getGameState() == state){
                return arenas.get(randomIndex);
            }
        }

        return null;
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

        for(UUID uuid : a.getPlayers()){
            Bukkit.getPlayer(uuid).sendMessage(KBFFA.getInstance().getMessages().getString("Game.LeavePlayer")
                    .replace("%player%", player.getName()));
        }

        a.getPlayers().remove(player.getUniqueId());
        player.teleport(KBFFA.getInstance().getLocationUtil().deserialize(KBFFA.getInstance().getSettings().getString("MainLobby")));
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setFireTicks(0);
        player.removePotionEffect(PotionEffectType.SPEED);

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

    public boolean isInsideCuboid(Location blockLocation, String section1, String section2){
        double x = blockLocation.getX();
        double y = blockLocation.getY();
        double z = blockLocation.getZ();

        FileConfiguration config = KBFFA.getInstance().getArenaFile().getArenaFile(getCurrentArena().getNameArena());

        Location point1 = deserializeLocation(config.getString(section1));
        Location point2 = deserializeLocation(config.getString(section2));

        double minX = Math.min(point1.getX(), point2.getX());
        double maxX = Math.max(point1.getX(), point2.getX());
        double minY = Math.min(point1.getY(), point2.getY());
        double maxY = Math.max(point1.getY(), point2.getY());
        double minZ = Math.min(point1.getZ(), point2.getZ());
        double maxZ = Math.max(point1.getZ(), point2.getZ());

        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public void giveItems(Player player){
        if(isInGame(player)){
            ItemStack stick = new ItemStack(Material.STICK, 1);
            ItemMeta stickMeta = stick.getItemMeta();
            stickMeta.setDisplayName(KBFFA.getInstance().getMessages().getString("Items.StickName"));
            stickMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            stick.setDurability((short) 0);
            stick.setItemMeta(stickMeta);

            ItemStack blocks = new ItemStack(Material.getMaterial(159), 64);

            ItemStack bow = new ItemStack(Material.BOW, 1);
            ItemMeta bowMeta = bow.getItemMeta();
            bowMeta.setDisplayName(KBFFA.getInstance().getMessages().getString("Items.BowName"));
            bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
            bow.setDurability((short) 0);
            bow.setItemMeta(bowMeta);

            ItemStack arrow = new ItemStack(Material.ARROW, 1);

            ItemStack jumpPad = new ItemStack(Material.GOLD_PLATE, 1);



            player.getInventory().setItem(0, stick);
            player.getInventory().setItem(1, blocks);
            player.getInventory().setItem(8, bow);
            player.getInventory().setItem(27, arrow);
            player.getInventory().setItem(2, jumpPad);
        }
    }

    public void ServerOff(){
        for(UUID uuid : getCurrentArena().getPlayers()){
            Bukkit.getPlayer(uuid).getInventory().clear();
        }

        getCurrentArena().getPlayers().clear();
    }
}
