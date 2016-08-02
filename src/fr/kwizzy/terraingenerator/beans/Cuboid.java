package fr.kwizzy.terraingenerator.beans;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Random;

public class Cuboid
{
    public int xMin;
    public int xMax;
    public int yMin;
    public int yMax;
    public int zMin;
    public int zMax;
    public double xMinCentered;
    public double xMaxCentered;
    public double yMinCentered;
    public double yMaxCentered;
    public double zMinCentered;
    public double zMaxCentered;
    public World world;
    public ArrayList<Block> bL;

    public Cuboid(final Location point1, final Location point2)
    {
        this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());
        this.world = point1.getWorld();
        this.xMinCentered = this.xMin + 0.5;
        this.xMaxCentered = this.xMax + 0.5;
        this.yMinCentered = this.yMin + 0.5;
        this.yMaxCentered = this.yMax + 0.5;
        this.zMinCentered = this.zMin + 0.5;
        this.zMaxCentered = this.zMax + 0.5;
    }

    public boolean isIn(final Location loc)
    {
        return loc.getWorld() == this.world && loc.getBlockX() >= this.xMin && loc.getBlockX() <= this.xMax
                && loc.getBlockY() >= this.yMin && loc.getBlockY() <= this.yMax && loc.getBlockZ() >= this.zMin
                && loc.getBlockZ() <= this.zMax;
    }

    public boolean isInWithMarge(final Location loc, final double marge)
    {
        return loc.getWorld() == this.world && loc.getX() >= this.xMinCentered - marge
                && loc.getX() <= this.xMaxCentered + marge && loc.getY() >= this.yMinCentered - marge
                && loc.getY() <= this.yMaxCentered + marge && loc.getZ() >= this.zMinCentered - marge
                && loc.getZ() <= this.zMaxCentered + marge;
    }

    public int getXWidth()
    {
        return this.xMax - this.xMin + 1;
    }

    public int getZWidth()
    {
        return this.zMax - this.zMin + 1;
    }

    public int getHeight()
    {
        return this.yMax - this.yMin + 1;
    }

    public int getTotalBlockSize()
    {
        return this.getHeight() * this.getXWidth() * this.getZWidth();
    }

    public Location getPoint1()
    {
        return new Location(this.world, (double) this.xMin, (double) this.yMin, (double) this.zMin);
    }

    public Location getPoint2()
    {
        return new Location(this.world, (double) this.xMax, (double) this.yMax, (double) this.zMax);
    }

    public ArrayList<Block> blockList()
    {
        final ArrayList<Block> bL = new ArrayList<Block>();
        int xMinX = this.xMin;
        int xMaxX = this.xMax;
        int zMinZ = this.zMin;
        int zMaxZ = this.zMax;
        int yMinY = this.zMin;
        int yMaxY = this.zMax;
        World w = world;


        for (int x = xMinX; x <= xMaxX; ++x) {
            for (int y = yMinY; y <= yMaxY; ++y) {
                for (int z = zMinZ; z <= zMaxZ; ++z) {
                    final Block b = w.getBlockAt(x, y, z);
                    bL.add(b);
                }
            }
        }

        return bL;
    }

    public ArrayList<Block> blockListStained()
    {
        bL = new ArrayList<>();
        int xMinX = this.xMin;
        int xMaxX = this.xMax;
        int zMinZ = this.zMin;
        int zMaxZ = this.zMax;
        World w = world;
        for (int x = xMinX; x <= xMaxX; ++x) {
            for (int z = zMinZ; z <= zMaxZ; ++z) {
                final Block b = w.getBlockAt(x, 10, z);
                bL.add(b);
            }
        }
        return bL;

    }

    public double getDistance()
    {
        return this.getPoint1().distance(this.getPoint2());
    }

    public double getDistanceSquared()
    {
        return this.getPoint1().distanceSquared(this.getPoint2());
    }

    public Location getRandomLocation()
    {
        final Random rand = new Random();
        final int x = rand.nextInt(Math.abs(this.xMax - this.xMin) + 1) + this.xMin;
        final int y = rand.nextInt(Math.abs(this.yMax - this.yMin) + 1) + this.yMin;
        final int z = rand.nextInt(Math.abs(this.zMax - this.zMin) + 1) + this.zMin;
        return new Location(this.world, (double) x, (double) y, (double) z);
    }

    public Location getCenter()
    {
        return new Location(this.world, (double) ((this.xMax - this.xMin) / 2 + this.xMin),
                            (double) ((this.yMax - this.yMin) / 2 + this.yMin), (double) ((this.zMax - this.zMin) / 2 + this.zMin));
    }
}
