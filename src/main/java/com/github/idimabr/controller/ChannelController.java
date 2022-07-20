package com.github.idimabr.controller;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.model.Channel;
import com.github.idimabr.model.ChannelType;
import com.github.idimabr.model.PlayerData;
import com.github.idimabr.utils.ConfigUtil;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;

@RequiredArgsConstructor
@Getter
public class ChannelController {

    private final RaphaChat plugin;

    private Map<String, Channel> channelMap = Maps.newHashMap();
    private Map<String, String> filters = Maps.newHashMap();

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

    public void loadFilters(){
        final ConfigUtil config = plugin.getConfig();
        final ConfigurationSection section = config.getConfigurationSection("Filters");
        for (String keyName : section.getKeys(false)) {
            final ConfigurationSection filter = section.getConfigurationSection(keyName);
            final String word = filter.getString("filter");
            final String replaced = filter.getString("replaced");
            filters.put(word, replaced);
        }
        plugin.getLogger().log(Level.INFO,"Loaded " + filters.size() + " filters.");
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
            final String color = channel.getString("color");
            final String format = channel.getString("format");

            channelMap.put(
                    Objects.requireNonNull(name).toLowerCase(),
                    new Channel(
                            name,
                            command,
                            radius,
                            type,
                            format,
                            Objects.requireNonNull(tag),
                            Objects.requireNonNull(color)
                    ));
            plugin.getLogger().log(Level.INFO,"Channel '" + name + "' loaded.");
        }
    }

    public String filterMessage(String message){
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            final String word = entry.getKey();
            final String replaced = entry.getValue();
            if (!message.contains(word)) continue;

            message = message.replaceAll(word, Matcher.quoteReplacement(replaced));
        }
        return message;
    }

    public void addChannel(String name, Channel channel){
        final ConfigUtil config = plugin.getConfig();

        config.set("Channels." + name + ".name", channel.getName());
        config.set("Channels." + name + ".command", channel.getCommand());
        config.set("Channels." + name + ".perform", channel.getType().name());
        config.set("Channels." + name + ".radius", channel.getRadius());
        config.set("Channels." + name + ".tag", channel.getTag().replace("§","&"));
        config.set("Channels." + name + ".color", channel.getColor().replace("§","&"));
        config.set("Channels." + name + ".format", channel.getFormat());
        config.save();

        channelMap.put(name, channel);
    }

    public void removeChannel(String channel) {
        final ConfigUtil config = plugin.getConfig();
        final DataController dataController = plugin.getDataController();

        for (PlayerData data : dataController.getDataMap().values())
            if(data.getChannel().getName().equalsIgnoreCase(channel))
                data.setChannel(null);

        config.set("Channels." + channel, null);
        config.save();
        channelMap.remove(channel);
    }

    public String getChannelList() {
        return StringUtils.join(channelMap.keySet(), ", ");
    }
}
