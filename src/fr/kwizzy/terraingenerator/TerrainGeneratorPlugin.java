package fr.kwizzy.terraingenerator;

import fr.kwizzy.terraingenerator.beans.ConfigManager;
import fr.kwizzy.terraingenerator.commands.TUndo;
import fr.kwizzy.terraingenerator.commands.Terrain;
import fr.kwizzy.terraingenerator.commands.TerrainTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Par Alexis le 25/02/2016.
 */

public class TerrainGeneratorPlugin extends JavaPlugin{


    private static TerrainGeneratorPlugin instance;
    private static File datafolder;
//    final String output = "Thank for purshase %%__USER__%%";

    @Override
    public void onEnable() {

        instance = this;
        datafolder = getDataFolder();

        loadCommands();
        loadEvents();

        saveDefaultConfig();
        new ConfigManager();
//        System.out.println(output);

    }

    @Override
    public void onDisable() {

    }

    public static TerrainGeneratorPlugin getInstance()
    {
        return instance;
    }

    public static File getDatafolder()
    {
        return datafolder;
    }

    public static void registerEvents(Plugin plugin, Listener listener)
    {
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    private void loadEvents(){
//        registerEvents(this, new );
    }

    private void loadCommands(){
        getCommand("terrain").setExecutor(new Terrain());
        getCommand("terrain").setTabCompleter(new TerrainTabCompleter());
        getCommand("tundo").setExecutor(new TUndo());
    }

}
