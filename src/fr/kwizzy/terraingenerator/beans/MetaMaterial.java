package fr.kwizzy.terraingenerator.beans;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Par Alexis le 23/07/2016.
 */

public class MetaMaterial implements Cloneable
{

    private Material m;
    private int data = 0;
    private Location location;

    public MetaMaterial(Material m)
    {
        this.m = m;
    }

    public Byte getData()
    {
        return (byte) data;
    }

    public void setData(int data)
    {
        this.data = data;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public void transformBlock(){
        if(location != null) {
            Block b = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            if(b.getType().equals(m) && b.getData() == getData()){
                return;
            }
            b.setType(m);
            b.setData(getData());
        }
    }

    public Material getMaterial()
    {
        return m;
    }


    @Override
    public String toString()
    {
        return "MetaMaterial{" +
                "m=" + m +
                ", data=" + data +
                ", location=" + location +
                '}';
    }

    public static MetaMaterial stringToMetaMaterial(String s){
        MetaMaterial m;
        if(s.contains(":")){
            String partsData[] = s.split(":");
            m = giveMaterialById(Integer.parseInt(partsData[0]), Integer.parseInt(partsData[1]));
            return m;
        }
        else
        {
            m = giveMaterialById(Integer.parseInt(s), 0);
            return m;
        }
    }

    public static MetaMaterial giveMaterialById(int id, int data) {
        for (Material m : Material.values()) {
            if (m.getId() == id) {
                MetaMaterial b = new MetaMaterial(m);
                b.setData(data);
                return b;
            }

        }
        return new MetaMaterial(Material.AIR);
    }

    @Override
    public MetaMaterial clone()
    {
        MetaMaterial m = new MetaMaterial(getMaterial());
        m.setData(getData());
        m.setLocation(getLocation());
        return m;
    }
}
