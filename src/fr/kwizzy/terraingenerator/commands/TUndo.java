package fr.kwizzy.terraingenerator.commands;

import fr.kwizzy.terraingenerator.beans.PlayerSession;
import org.bukkit.entity.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Par Alexis le 23/07/2016.
 */

public class TUndo implements CommandExecutor
{


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(commandSender instanceof Player){
            PlayerSession ps = PlayerSession.getSessionPlayer((Player) commandSender);
            ps.undo();
        }
        return false;
    }
}
