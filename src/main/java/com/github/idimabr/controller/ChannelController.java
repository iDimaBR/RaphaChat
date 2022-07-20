package com.github.idimabr.controller;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.model.Channel;
import com.github.idimabr.model.ChannelType;
import com.github.idimabr.utils.ConfigUtil;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ChannelController {

    private final RaphaChat plugin;

    private Map<String, Channel> channelMap = Maps.newHashMap();

    public Channel getChannel(String name){
        return channelMap.get(name);
    }

    public Channel getChannelByCommand(String command){
        return channelMap
                .values()
                .stream()
                .filter(channel -> channel.getCommand().equalsIgnoreCase(command))
                .findAny()
                .orElse(null);
    }

    public void loadChannels(){
        final ConfigUtil config = plugin.getConfig();

        final ConfigurationSection section = config.getConfigurationSection("Channels");
        for (String keyName : section.getKeys(false)) {
            final ConfigurationSection channel = section.getConfigurationSection(keyName);
            final String name = channel.getString("name");
            final String command = channel.getString("command");
            final ChannelType type = ChannelType.valueOf(channel.getString("perform"));
            final int radius = channel.getInt("radius");
            final String tag = channel.getString("tag");
            final String format = channel.getString("format");

            channelMap.put(name.toLowerCase(), new Channel(name, command, radius, type, format, tag));
            plugin.getLogger().info("Channel '" + name + "' loaded.");
        }
    }
}
