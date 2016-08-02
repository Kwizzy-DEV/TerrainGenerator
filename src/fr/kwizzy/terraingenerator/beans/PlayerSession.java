package fr.kwizzy.terraingenerator.beans;



import fr.kwizzy.terraingenerator.TerrainGeneratorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Par Alexis le 23/07/2016.
 */

public class PlayerSession
{
    private static List<PlayerSession> playerSession = new ArrayList<>();

    private LinkedList<List<MetaMaterial>> undo = new LinkedList<>();

    private boolean operationProgress = false;
    private Player p;

    private PlayerSession(Player player)
    {
        this.p = player;
        playerSession.add(this);
    }

    /**
     * Add a blockList with location in MetaMaterial
     */
    public void addHistory(List<MetaMaterial> l){
        undo.add(l);
    }

    /**
     * Undo to older transformations of the terrain
     */
    public void undo(){
        if(operationProgress){
            p.sendMessage("§cAn operation is already in progress");
            return;
        }
        if(!undo.isEmpty()){
            List<MetaMaterial> currList = undo.getLast();
            undo.pollLast();
            operationProgress = true;
            new BukkitRunnable(){
                int index = 0;
                @Override
                public void run()
                {
                    int tempindex = 0;
                    if(ConfigManager.truncate){
                        tempindex = index + (ConfigManager.truncateBlock*255*16*16);
                    }
                    else
                    {
                        tempindex = index + currList.size();
                    }

                    for (; index < tempindex; index++) {
                        MetaMaterial m = currList.get(index);
                        if(m.getLocation() != null) {
//                            m.transformBlock();
                        }
                    }
                    if(index+1 > currList.size()){
                        operationProgress = false;
                        p.sendMessage("§aUndo successfull");
                        this.cancel();
                    }
                }
            }.runTaskTimer(TerrainGeneratorPlugin.getInstance(), 0, ConfigManager.truncateTime);
        }
    }

    public static PlayerSession getSessionPlayer(Player p){
        for (PlayerSession session : playerSession) {
            if(session.p.getUniqueId().equals(p.getUniqueId())){
                return session;
            }
        }
        return new PlayerSession(p);
    }

}
