package fr.kwizzy.terraingenerator.beans.terrain;

import fr.kwizzy.terraingenerator.beans.Cuboid;
import fr.kwizzy.terraingenerator.beans.MetaMaterial;
import fr.kwizzy.terraingenerator.beans.Probability;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

/**
 * Par Alexis le 02/08/2016.
 */

public class TerrainInfo
{

    Cuboid cubo;
    Player p;
    int level;
    long seed;
    double scale;
    double frequency;
    double amplitude;
    double amplifier;
    String surface;
    String underground;
    boolean withoutAir;

    Probability<MetaMaterial> materialsSurface;
    Probability<MetaMaterial> materialsUnderground;

    public TerrainInfo(Cuboid cubo,
                       int level,
                       long seed,
                       double scale,
                       double frequency,
                       double amplitude,
                       double amplifier,
                       Player p,
                       String surface,
                       String underground,
                       boolean withoutAir)
    {
        this.cubo = cubo;
        this.level = level;
        this.seed = seed;
        this.scale = scale;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.amplifier = amplifier;
        this.p = p;
        this.surface = surface;
        this.underground = underground;
        this.withoutAir = withoutAir;
        setProbabilityList();
    }

    /**
     * Set the probability list
     */
    private void setProbabilityList()
    {
        materialsSurface = materialGenerator(surface);
        materialsUnderground = materialGenerator(underground);
        if (materialsSurface.getArray().isEmpty() || materialsSurface == null) {
            materialsSurface = materialGenerator("2");
        }
        if (materialsUnderground.getArray().isEmpty() || materialsUnderground == null) {
            materialsUnderground = materialGenerator("1,1:2");
        }
    }

    /**
     * @param s String with id and meta id like > 5,5:1...
     *          Can accept % like -> 11%5,10%5:1
     * @return probability list with meta material
     */
    private Probability<MetaMaterial> materialGenerator(String s)
    {
        Probability<MetaMaterial> materialProbability = new Probability<>();
        Probability<MetaMaterial> nullObject = null;
        if (s.contains(",")) {
            if (s.contains("%")) {
                String parts[] = s.split(Pattern.quote(","));
                for (String part : parts) {
                    if (part.contains("%")) {
                        String percentage[] = part.split(Pattern.quote("%"));
                        MetaMaterial m = null;
                        int percent = 0;
                        try {
                            m = stringToMetaMaterial(percentage[1]);
                            percent = Integer.parseInt(percentage[0]);
                        } catch (Exception e) {
                            return nullObject;
                        }
                        materialProbability.add(m, percent);
                    } else {
                        materialProbability.add(stringToMetaMaterial(part), 20);
                    }

                }
            } else {
                String parts[] = s.split(Pattern.quote(","));
                for (String part : parts) {
                    materialProbability.add(stringToMetaMaterial(part), 50);
                }
            }
        } else {
            if (s.contains("%")) {
                String percentage[] = s.split(Pattern.quote("%"));
                MetaMaterial m;
                int percent;
                try {
                    m = stringToMetaMaterial(percentage[1]);
                    percent = Integer.parseInt(percentage[0]);
                } catch (Exception e) {
                    return nullObject;
                }
                materialProbability.add(m, percent);
                materialProbability.add(new MetaMaterial(Material.AIR), 100 - percent);
            } else {
                materialProbability.add(stringToMetaMaterial(s), 100);
            }
        }
        return materialProbability;
    }

    /**
     * @param s String s like 1 or 1:1
     * @return a MetaMaterial
     */
    private MetaMaterial stringToMetaMaterial(String s)
    {
        MetaMaterial m;
        if (s.contains(":")) {
            String partsData[] = s.split(":");
            m = MetaMaterial.giveMaterialById(Integer.parseInt(partsData[0]), Integer.parseInt(partsData[1]));
            return m;
        } else {
            m = MetaMaterial.giveMaterialById(Integer.parseInt(s), 0);
            return m;
        }
    }


}
