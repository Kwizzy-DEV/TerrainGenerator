package fr.kwizzy.terraingenerator.beans;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Par Alexis le 26/06/2016.
 */

public class JsonMessageBuilder
{
    public List<JComp> jsonComp = new ArrayList<>();

    /**
     *Utilisation :
     *  JsonMessageBuilder jm = new JsonMessageBuilder();
     *  jm.newJComp("§eLe chat est").addHoverText("§cIl est bleu en fait :/").build(jm);
     *  jm.newJComp(" §1noir").addHoverText("§cEt bah non il est noir").addURL("www.chaton.com").build(jm);
     *  jm.send(Player...)
     */

    public JsonMessageBuilder() {
    }

    public JComp newJComp(String text){
        JComp jc = new JComp();
        jc.addText(text);
        return jc;
    }

    /**
     *
     * @param c multiple json component
     */
    public void addComponent(JComp... c){
        jsonComp.addAll(Arrays.asList(c));
    }

    public void send(){
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    public void send(boolean clearAfter, Player... player){
        for (Player p : player){
            send(p);
        }
        if(clearAfter)
            jsonComp.clear();
    }

    public void send(Collection<Player> player){
        player.forEach(this::send);
    }

    public void send(Player p){
        List<JComp> instanceJsonComp = jsonComp;
        StringBuilder temp = new StringBuilder();
        for (JComp jComp : instanceJsonComp)
            temp.append(jComp.component);

        temp.deleteCharAt(temp.length()-1);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " " +  "[\"\"," + temp + "]");
    }


    public static class JComp{

        private StringBuilder component = new StringBuilder();

        private JsonMessageBuilder jsonMessageBuilder ;

        public JComp addText(String message){
            component.append("\"text\":\"").append(message).append("\",");
            return this;
        }

        public JComp addCommandExecutor(String command){
            component.append("\"clickEvent\":{ \"action\":\"run_command\", \"value\":\"").append(command).append("\" },");
            return this;
        }

        public JComp addCommandSuggestion(String command){
            component.append("\"clickEvent\":{ \"action\":\"suggest_command\", \"value\":\"").append(command).append("\" },");
            return this;
        }

        public JComp addURL(String link){
            component.append("\"clickEvent\":{ \"action\":\"open_url\", \"value\":\"").append(link).append("\" },");
            return this;
        }

        public JComp addChatSuggestion(String text){
            component.append("\"insertion\":\"").append(text).append("\"},");
            return this;
        }

        public JComp setBold(boolean bold){
            component.append(" \"bold\": ").append(bold).append(",");
            return this;
        }

        public JComp setItalic(boolean italic){
            component.append(" \"italic\": ").append(italic).append(",");
            return this;
        }

        public JComp setUnderlined(boolean underlined){
            component.append(" \"italic\": ").append(underlined).append(",");
            return this;
        }

        public JComp setStrikethrough(boolean strikethrough){
            component.append(" \"italic\": ").append(strikethrough).append(",");
            return this;
        }

        public JComp setObfuscated(boolean obfuscated){
            component.append(" \"italic\": ").append(obfuscated).append(",");
            return this;
        }

        public JComp setColor(ChatColor cc){
            component.append("\"color\": \"").append(cc.name().toLowerCase()).append("\",");
            return this;
        }

        public JComp addHoverText(String... text){
            String txt = "";
            for (int i = 0; i < text.length ; i++) {
                if (i + 1 == text.length){
                    txt = txt + text[i];
                }else {
                    txt = txt + text[i] + "\\u000a";
                }
            }
            component.append("\"hoverEvent\":{ \"action\":\"show_text\", \"value\":\"").append(txt).append("\" },");
            return this;
        }



        public JsonMessageBuilder build(JsonMessageBuilder jsonMessageBuilder){
            component.deleteCharAt(component.length()-1);
            String finalComp = "{" + component + "},";
            component = new StringBuilder(finalComp);
            jsonMessageBuilder.jsonComp.add(this);
            return jsonMessageBuilder;
        }

    }

}
