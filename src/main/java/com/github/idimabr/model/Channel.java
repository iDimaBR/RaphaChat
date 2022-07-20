package com.github.idimabr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@AllArgsConstructor @Getter @Setter
public class Channel {

    private final String name;
    private final String command;
    private int radius;
    private final ChannelType type;
    private String format;
    private String tag;

    public void typing(Player origin, String message){
        final String replaceFormat = format.replace("{channel}", name)
                .replace("{sender}", origin.getName())
                .replace("{message}", message.replace("&","§"));

        if(type.equals(ChannelType.ALL)) {
            Bukkit.getOnlinePlayers().forEach(target -> target.sendMessage(replaceFormat));
            return;
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            if(target.getWorld() != origin.getWorld() || target.getLocation().distance(origin.getLocation()) > radius) continue;
            target.sendMessage(replaceFormat);
        }
    }
}
