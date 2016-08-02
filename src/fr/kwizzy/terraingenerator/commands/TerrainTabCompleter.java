package fr.kwizzy.terraingenerator.commands;

import fr.kwizzy.terraingenerator.beans.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Par Alexis le 02/08/2016.
 */

public class TerrainTabCompleter implements TabCompleter
{


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        List<String> newList = new ArrayList<>();
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;

            Terrain.help(p);

            newList.add("-l:" + (int) p.getLocation().getY());
            newList.add("-su:2,159:5");
            newList.add("-un:1,1:5");
            newList.add("-amf:" + ConfigManager.amplifier);
            newList.add("-sc:" + ConfigManager.scale);
            newList.add("-f:" + ConfigManager.frequency);
            newList.add("-amt:" + ConfigManager.amplitude);
            newList.add("-se:17");
            newList.add("-random");
            newList.add("-a");

        }
        return newList;
    }
}
