package net.imjoycepg.mc.Commands;

import net.imjoycepg.mc.KBFFA;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCMD implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            if(args.length == 0){
                for(String help : KBFFA.getInstance().getMessages().getStringList("Commands.Help-User")){
                    player.sendMessage(help);
                }
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("join")){
                    KBFFA.getInstance().getArenaManager().addPlayer(player);
                }

                if(args[0].equalsIgnoreCase("leave")){
                    KBFFA.getInstance().getArenaManager().removePlayer(player);
                }
            }


        }else{
            sender.sendMessage(KBFFA.getInstance().getMessages().getString("General.prefix") + KBFFA.getInstance().getMessages().getString("General.noConsole"));
        }

        return false;
    }
}
