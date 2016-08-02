package fr.kwizzy.terraingenerator.beans;

import fr.kwizzy.terraingenerator.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Par Alexis le 03/07/2016.
 */

public class Probability<E>
{

    private LinkedHashMap<E, Integer> map = new LinkedHashMap<>();
    private double total = 0;

    public Probability()
    {

    }


    /**
     *
     * @param result type
     * @param chance <= 100
     */
    public void add(E result, int chance)
    {
        chance = ((chance > 100) ? 100 : chance);
        map.put(result, chance);
    }

    public void remove(E key){
        map.remove(key);
    }

    public E getChanceKey()
    {
        int randIndex = Utils.randomInteger(0, map.size());
        int index = 0;
        for (Map.Entry<E, Integer> entry : map.entrySet()) {
            if(index == randIndex){
                if(Utils.chanceOf(entry.getValue())){
                    return entry.getKey();
                }
                else
                    break;
            }
            index++;
        }
        return getChanceKey();
    }

    public int getIndex(E s)
    {
        int i = 0;
        for (Map.Entry<E, Integer> entry : map.entrySet())
        {
            if (entry.getValue() == s)
            {
                return i;
            }
            i++;

        }
        return 0;

    }

    public E getKey(int index)
    {
        int i = 0;
        for (Map.Entry<E, Integer> entry : map.entrySet())
        {
            if (i == index)
            {
                return entry.getKey();
            }
            i++;

        }
        return null;
    }

    public Integer getChance(int index)
    {
        int i = 0;
        for (Map.Entry<E, Integer> entry : map.entrySet())
        {
            if (i == index)
            {
                return entry.getValue();
            }
            i++;

        }
        return null;
    }

    public int size()
    {
        return map.size();
    }

    public void clear()
    {
        map.clear();

    }


    public Set<Map.Entry<E, Integer>> Entry()
    {
        return map.entrySet();
    }

    public ArrayList<E> getArray()
    {
        ArrayList<E> i = map.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));
        return i;

    }

}
