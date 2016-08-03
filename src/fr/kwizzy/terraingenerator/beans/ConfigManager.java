package fr.kwizzy.terraingenerator.beans;

import fr.kwizzy.terraingenerator.TerrainGeneratorPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Par Alexis le 23/07/2016.
 */

public class ConfigManager
{

    private static FileConfiguration c = TerrainGeneratorPlugin.getInstance().getConfig();
    public static int    level = c.getInt("level");
    public static int    octave = c.getInt("octave");
    public static double scale = c.getInt("scale");
    public static double frequency = c.getDouble("frequency");
    public static double amplitude = c.getDouble("amplitude");
    public static double amplifier = c.getDouble("amplifier");
    public static boolean truncate = c.getBoolean("truncate_enabled");
    public static int truncateBlock = c.getInt("truncate_blocks");
    public static int truncateTime = c.getInt("truncate_time");
}
