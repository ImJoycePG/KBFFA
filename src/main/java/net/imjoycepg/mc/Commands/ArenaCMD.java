package net.imjoycepg.mc.Commands;

import net.imjoycepg.mc.Game.Arena;
import net.imjoycepg.mc.Game.GameState;
import net.imjoycepg.mc.KBFFA;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("kbffa.admin")) {

                if (args.length == 0) {
                    for (String help : KBFFA.getInstance().getMessages().getStringList("Commands.Help-Admin")) {
                        player.sendMessage(help);
                    }
                }

                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("reload")){
                        KBFFA.getInstance().getSettings().reloadYMLS();
                        KBFFA.getInstance().getMessages().reloadYMLS();
                        player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("General.reloadYAML"));
                    }
                }

                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("setMainLobby")){
                        KBFFA.getInstance().getSettings().c.set("MainLobby", KBFFA.getInstance().getLocationUtil().serialize(player.getLocation()));
                        player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.SetMainLobby"));
                        KBFFA.getInstance().getSettings().save();
                    }
                }

                if (args.length == 2) {

                    String action = args[0];
                    String nameArena = args[1];

                    if(action.equalsIgnoreCase("create")){
                        if(KBFFA.getInstance().getArenaFile().getArenaFile(nameArena) != null){
                            player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.NoDuplicate"));
                            return true;
                        }

                        Arena arena = new Arena();
                        arena.setNameArena(nameArena);
                        KBFFA.getInstance().getArenaManager().createArena(nameArena);
                        player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.CreateArena"));

                        return true;
                    }

                    if(action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("remove")){
                        if(KBFFA.getInstance().getArenaFile().getArenaFile(nameArena) != null){
                            player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.NoExistArena"));
                            return true;
                        }

                        KBFFA.getInstance().getArenaManager().removeArena(nameArena);
                        player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.DeleteArena"));
                        return true;
                    }

                    if(action.equalsIgnoreCase("setspawn")){
                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setSpawnArena(player.getLocation());
                        player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.SetSpawn"));
                        return true;
                    }

                    if(action.equalsIgnoreCase("setMinLobby")){
                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setMinLobby(player.getLocation());
                        player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.SetMinLobby"));
                        return true;
                    }

                    if(action.equalsIgnoreCase("setMaxLobby")){
                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setMaxLobby(player.getLocation());
                        player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.SetMaxLobby"));
                        return true;
                    }

                    if(action.equalsIgnoreCase("setMinArena")){
                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setMinArena(player.getLocation());
                        player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.SetMinArena"));
                        return true;
                    }

                    if(action.equalsIgnoreCase("setMaxArena")){
                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setMaxArena(player.getLocation());
                        player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.SetMaxArena"));
                        return true;
                    }

                    if(action.equalsIgnoreCase("save")){

                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setGameState(GameState.WAITING);
                        if(arena.getSpawnArena() == null && arena.getMaxArena() == null && arena.getMinArena() == null &&
                            arena.getMinLobby() == null && arena.getMaxLobby() == null){
                            player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.ErrorSave"));
                            return true;
                        }

                        arena.save();
                        player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("Arena.SaveArena"));
                        return true;
                    }


                } else {
                    player.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("General.noPermission"));
                }

            } else {
                sender.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("General.noConsole"));
            }
        }
        return false;
    }

}
