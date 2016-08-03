package fr.kwizzy.terraingenerator.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitServerInterface;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import fr.kwizzy.terraingenerator.TerrainGeneratorPlugin;
import fr.kwizzy.terraingenerator.beans.ConfigManager;
import fr.kwizzy.terraingenerator.beans.Cuboid;
import fr.kwizzy.terraingenerator.beans.terrain.BasicGeneration;
import fr.kwizzy.terraingenerator.beans.JsonMessageBuilder;
import fr.kwizzy.terraingenerator.beans.terrain.TerrainInfo;
import fr.kwizzy.terraingenerator.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.primesoft.asyncworldedit.api.IAsyncWorldEdit;
import org.primesoft.asyncworldedit.api.blockPlacer.IBlockPlacer;
import org.primesoft.asyncworldedit.api.playerManager.IPlayerEntry;
import org.primesoft.asyncworldedit.api.worldedit.IAsyncEditSessionFactory;
import org.primesoft.asyncworldedit.api.worldedit.IThreadSafeEditSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Par Alexis le 22/07/2016.
 */

public class Terrain implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args)
    {
        if (commandSender instanceof Player) {

            Player p = (Player) commandSender;
            if (args.length >= 1) {

                WorldEditPlugin worldEditPlugin = null;
                worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

                if (worldEditPlugin == null) {
                    System.out.println("** WORLDEDIT IS NOT LOADED **");
                    TerrainGeneratorPlugin.getInstance().onDisable();
                    return false;
                }
                Selection sel = worldEditPlugin.getSelection(p);
                if (!(sel != null && sel.getMaximumPoint() != null && sel.getMinimumPoint() != null)) {
                    p.sendMessage("§cYou need to select position §b1 §c& §b2 §cwith the worldedit wand.");
                    return false;
                }


                org.bukkit.util.Vector l0 = sel.getMaximumPoint().toVector();
                org.bukkit.util.Vector l1 = sel.getMinimumPoint().toVector();

                org.bukkit.Location loc_1 = new org.bukkit.Location(p.getWorld(), l0.getX(), l0.getY(), l0.getZ());
                org.bukkit.Location loc_2 = new org.bukkit.Location(p.getWorld(), l1.getX(), l1.getY(), l1.getZ());

                Cuboid c = new Cuboid(loc_1, loc_2);

                String surface = "2,159:5";
                String underground = "1,1:6";
                int level = ConfigManager.level;
                int octave = ConfigManager.octave;
                double amplifier = ConfigManager.amplifier;
                double scale = ConfigManager.scale;
                double frequency = ConfigManager.frequency;
                double amplitude = ConfigManager.amplitude;
                long seed = loc_1.getWorld().getSeed();
                boolean withoutAir = false;
                try {
                    if (containArgument("-l:", args)) {
                        level = Integer.parseInt(getValue("-l:", args));
                    }
                    if(containArgument("-amf:", args)){
                        amplifier = Double.parseDouble(getValue("-amf:", args));
                    }
                    if(containArgument("-sc:", args)){
                        scale = Double.parseDouble(getValue("-sc:", args));
                    }
                    if(containArgument("-f:", args)){
                        frequency = Double.parseDouble(getValue("-f:", args));
                    }
                    if(containArgument("-amt:", args)){
                        amplitude = Double.parseDouble(getValue("-amt:", args));
                    }
                    if(containArgument("-se:", args)){
                        seed = Long.parseLong(getValue("-se:", args));
                    }
                    if(containArgument("-su:", args)){
                        surface = getValue("-su:", args);
                    }
                    if(containArgument("-un:", args)){
                        underground = getValue("-un:", args);
                    }
                    if(containArgument("-oc:", args)){
                        octave = Integer.parseInt(getValue("-oc:", args));
                    }
                    if(containStrict("-a", args)){
                        withoutAir = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    p.sendMessage("§cUne erreur est survenue dans la commande !");
                    return false;
                }

                if (containArgument("-random", args)) {
                    if(!containArgument("-amf:", args))
                        amplifier = Utils.randomInteger(1, 20);
                    if(!containArgument("-sc:", args))
                        scale = Utils.randomInteger(5, 256);
                    if(!containArgument("-f:", args))
                        frequency = Utils.randDouble(0.1, 1.1);
                    if(!containArgument("-amt:", args))
                        amplitude = Utils.randDouble(0.1, 1.1);
                    if(!containArgument("-se:", args))
                        seed = Utils.randomInteger(-90000, 90000);
                    if(!containArgument("-oc:", args)){
                        octave = Utils.randomInteger(1, 20);
                    }
                }
                IAsyncWorldEdit iAsyncWorldEdit = (IAsyncWorldEdit) Bukkit.getPluginManager().getPlugin("AsyncWorldEdit");
                BukkitWorld bw = new BukkitWorld(p.getWorld());
                IAsyncEditSessionFactory sessionFactory = (IAsyncEditSessionFactory) WorldEdit.getInstance().getEditSessionFactory();
                IPlayerEntry playerEntry = iAsyncWorldEdit.getPlayerManager().getPlayer(p.getUniqueId());

                //Start BlockBag of Player
                Method privateStringMethod = null;
                WorldEditPlugin wp = null;
                try {
                    privateStringMethod = WorldEditPlugin.class.getDeclaredMethod("getInstance");
                    privateStringMethod.setAccessible(true);
                    wp = (WorldEditPlugin) privateStringMethod.invoke(null);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                BukkitServerInterface bsi = new BukkitServerInterface(wp, Bukkit.getServer());
                BukkitPlayer bp = new BukkitPlayer(wp, bsi, p);
                BlockBag blockBag = WorldEdit.getInstance().getSessionManager().findByName(p.getName()).getBlockBag(bp);
                //End BlockBag of Player

                IThreadSafeEditSession threadSafeEditSession = sessionFactory.getThreadSafeEditSession(bw, -1, blockBag, playerEntry);
                IBlockPlacer blockPlacer = iAsyncWorldEdit.getBlockPlacer();
                TerrainInfo t = new TerrainInfo(c, level, seed, scale, frequency, amplitude, amplifier, p, surface, underground, withoutAir, octave);
                LocalSession session = WorldEdit.getInstance().getSession(bp);
                blockPlacer.performAsAsyncJob(threadSafeEditSession, playerEntry, "§9TerrainGeneration §dPlugin§f", new BasicGeneration(t));
                session.remember((EditSession) threadSafeEditSession);
            } else {

                help(p);

            }

        }
        return false;
    }

    public boolean containArgument(String argument, String[] total)
    {
        for (String s : total) {
            if (s.contains(argument)) {
                return true;
            }
        }
        return false;
    }

    public boolean containStrict(String argument, String[] total)
    {
        for (String s : total) {
            if (s.equals(argument)) {
                return true;
            }
        }
        return false;
    }

    public String getValue(String argument, String[] total)
    {
        for (String s : total) {
            if (s.contains(argument)) {
                String[] split = s.split(Pattern.quote(":"));
                StringBuilder complete = new StringBuilder();
                for (int i = 1; i < split.length; i++) {
                    if(i != 1)
                        complete.append(":").append(split[i]);
                    else
                        complete.append(split[i]);
                }
                return complete.toString();
            }
        }
        return null;
    }

    public static void help(Player p){
        JsonMessageBuilder j = new JsonMessageBuilder();
        j.newJComp("Command: /terrain, \n" +
                   "Argument:\n" +
                   "  -l:integer = §elevel\n" +
                   "  -su:id = §eblock id at surface (support %)\n" +
                   "  -un:id = §eblock id for under surface (support %)\n" +
                   "  -amf:double = §eamplifier\n" +
                   "  -amt:double = §eamplitude\n" +
                   "  -se:integer = §eseed of the generation\n" +
                   "  -oc:integer = §eoctave modification\n" +
                   "  -f:double = §efrequency\n" +
                   "  -sc:double = §escale (High = flat, low = mountains)\n" +
                   "  -random = §erandom parametters\n" +
                   "  -a = §egenerate without air\n" +
                   "§aEach of its arguments are not required.\n" +
                   " ").setColor(ChatColor.GREEN)
                .addHoverText("Click here to get the command")
                .addCommandSuggestion("/terrain "
                + "-l:" + (int) p.getLocation().getY() + " "
                + "-su:2,159:5" + " "
                + "-un:1,1:5" + " "
                + "-amf:" + ConfigManager.amplifier + " "
                + "-sc:" + ConfigManager.scale + " "
                + "-f:" + ConfigManager.frequency + " "
                + "-amt:" + ConfigManager.amplitude + " "
                + "-se:17")
                .build(j).send(p);
    }
}
