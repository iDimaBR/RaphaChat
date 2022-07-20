package com.github.idimabr.model;

import com.github.idimabr.RaphaChat;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter @Setter
public class Channel {

    private final String name;
    private final String command;
    private int radius;
    private final ChannelType type;
    private String format;
    private String tag;
    private String color;

    public Channel(String name, String command, int radius, ChannelType type, String format, String tag, String color) {
        this.name = name;
        this.command = command;
        this.radius = radius;
        this.type = type;
        this.format = format;
        this.tag = tag.replace("&","§");
        this.color = color.replace("&","§");
    }

    public void typing(Player origin, String message){
        final String replaceFormat = format
                .replace("&","§")
                .replace("{channel}", tag)
                .replace("{sender}", origin.getName())
                .replace("{color}", color)
                .replace("{message}", message.replace("&","§"))
                .replace("{group}", RaphaChat.getChat().getPlayerPrefix(origin).replace("&","§"));

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
