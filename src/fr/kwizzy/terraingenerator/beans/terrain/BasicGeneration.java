package fr.kwizzy.terraingenerator.beans.terrain;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import fr.kwizzy.terraingenerator.beans.JsonMessageBuilder;
import fr.kwizzy.terraingenerator.beans.MetaMaterial;
import fr.kwizzy.terraingenerator.utils.Utils;
import org.bukkit.Material;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.primesoft.asyncworldedit.api.utils.IFuncParamEx;
import org.primesoft.asyncworldedit.api.worldedit.ICancelabeEditSession;

import java.util.Random;


/**
 * Par Alexis le 01/08/2016.
 */

public class BasicGeneration implements IFuncParamEx<Integer, ICancelabeEditSession, MaxChangedBlocksException>
{
    TerrainInfo tInfo;


    public BasicGeneration(TerrainInfo f)
    {
        this.tInfo = f;
    }

    @Override
    public Integer execute(ICancelabeEditSession editSession) throws MaxChangedBlocksException
    {
        Random r = new Random(tInfo.seed);
        SimplexOctaveGenerator octave = new SimplexOctaveGenerator(r, tInfo.octave);
        octave.setScale(1. / tInfo.scale);
        for (int x = tInfo.cubo.xMin; x <= tInfo.cubo.xMax; ++x) {
            for (int z = tInfo.cubo.zMin; z <= tInfo.cubo.zMax; ++z) {
                placeNoise(x, z, octave, editSession);
            }
        }
        return 32768;
    }

    /**
     * Place blocs with noise
     *
     * @param octave
     */
    public void placeNoise(int x, int z, SimplexOctaveGenerator octave, ICancelabeEditSession editSession)
    {
        double noise = octave.noise(x, z, tInfo.frequency, tInfo.amplitude) * tInfo.amplifier;
        int y;
        for (y = 0; y < tInfo.level + noise; ++y) {
            if (y > 255) {
                MetaMaterial m = tInfo.materialsUnderground.getChanceKey();
                setBlock(x, 255, z, m, editSession);
                continue;
            }
            MetaMaterial m = tInfo.materialsUnderground.getChanceKey();
            setBlock(x, y, z, m, editSession);
        }
        if (y > 255) {
            MetaMaterial m = tInfo.materialsSurface.getChanceKey();
            setBlock(x, 255, z, m, editSession);
            return;
        }
        MetaMaterial m = tInfo.materialsSurface.getChanceKey();
        setBlock(x, y, z, m, editSession);
        if(!tInfo.withoutAir) {
            airUp(x, y, z, editSession);
        }
    }


    /**
     * Check if block up is not air, if its not air -> to air
     * call again this function with the current block.
     *
     */
    private void airUp(int x, int y, int z, ICancelabeEditSession editSession)
    {
        setBlock(x, y+1, z, new MetaMaterial(Material.AIR), editSession);
        if (y > 255) {
            return;
        }
        airUp(x, y+1, z, editSession);

    }

    private void sendFinally()
    {
        tInfo.p.sendMessage("§aThe terrain has been generated !");
        StringBuilder s = new StringBuilder();
        s.append("§eSeed: §7").append(tInfo.seed).append("\n");
        s.append("§eScale: §7").append(tInfo.scale).append("\n");
        s.append("§eFrequency: §7").append(tInfo.frequency).append("\n");
        s.append("§eAmplitude: §7").append(tInfo.amplitude).append("\n");
        s.append("§eAmplifier: §7").append(tInfo.amplifier).append("\n");
        s.append("§eSurface: §7").append(tInfo.surface).append("\n");
        s.append("§eUnderground: §7").append(tInfo.underground).append("\n");
        s.append("§eGlobal level: §7").append(tInfo.level);
        JsonMessageBuilder j = new JsonMessageBuilder();
        j.newJComp(s.toString()).addCommandSuggestion("/terrain " + tInfo.level + " " + tInfo.surface + " " + tInfo.underground + " " + tInfo.amplifier
                                                              + " " + tInfo.scale + " " + Utils.roundDouble(tInfo.frequency, 5) + " " + Utils.roundDouble(tInfo.amplitude, 5) + " " + tInfo.seed)
                .addHoverText("Click here for get the command")
                .build(j).send(tInfo.p);
    }

    private void setBlock(int x, int y, int z, MetaMaterial metaMaterial, ICancelabeEditSession editSession)
    {
        MetaMaterial m = metaMaterial.clone();
        Vector v = new Vector(x, y, z);
        BaseBlock b = new BaseBlock(m.getMaterial().getId(), m.getData());
        try {
            editSession.setBlock(v, b);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }

    }

}