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
                        KBFFA.getInstance().getArenaManager().saveArenas();
                        KBFFA.getInstance().getSettings().reloadYMLS();
                        KBFFA.getInstance().getMessages().reloadYMLS();
                    }
                }

                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("setmainlobby")){
                        KBFFA.getInstance().getSettings().c.set("MainLobby", KBFFA.getInstance().getLocationUtil().serialize(player.getLocation()));
                        player.sendMessage("El Lobby principal fue establecido");
                        KBFFA.getInstance().getSettings().save();
                    }
                }

                if (args.length == 2) {

                    String action = args[0];
                    String nameArena = args[1];

                    if(action.equalsIgnoreCase("create")){
                        Arena arena = new Arena();
                        arena.setNameArena(nameArena);
                        KBFFA.getInstance().getArenaManager().createArena(nameArena);
                        player.sendMessage("La arena fue creada");
                        return true;
                    }

                    if(action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("remove")){
                        KBFFA.getInstance().getArenaManager().removeArena(nameArena);
                        player.sendMessage("El mapa fue eliminado correctamente");
                        return true;
                    }

                    if(action.equalsIgnoreCase("setspawn")){
                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setSpawnArena(player.getLocation());
                        player.sendMessage("El spawn de inicio fue establecido.");
                        return true;
                    }

                    if(action.equalsIgnoreCase("setMinLobby")){
                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setMinLobby(player.getLocation());
                        player.sendMessage("El minimo del Lobby fue establecido");
                        return true;
                    }

                    if(action.equalsIgnoreCase("setMaxLobby")){
                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setMaxLobby(player.getLocation());
                        player.sendMessage("El maximo del Lobby fue establecido");
                        return true;
                    }

                    if(action.equalsIgnoreCase("setMinArena")){
                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setMinArena(player.getLocation());
                        player.sendMessage("El minimo de la arena fue establecido");
                        return true;
                    }

                    if(action.equalsIgnoreCase("setMaxArena")){
                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setMaxArena(player.getLocation());
                        player.sendMessage("El maximo de la arena fue establecido");
                        return true;
                    }

                    if(action.equalsIgnoreCase("save")){

                        Arena arena = KBFFA.getInstance().getArenaManager().getArena(nameArena);
                        arena.setGameState(GameState.WAITING);
                        arena.save();
                        player.sendMessage("El mapa fue guardado correctamente.");
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
