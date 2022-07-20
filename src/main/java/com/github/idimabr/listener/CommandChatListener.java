package com.github.idimabr.listener;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.controller.ChannelController;
import com.github.idimabr.model.Channel;
import com.github.idimabr.model.PlayerData;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@AllArgsConstructor
public class CommandChatListener implements Listener {

    private RaphaChat plugin;

    @EventHandler
    public void onChat(PlayerCommandPreprocessEvent e){
        final String message = e.getMessage();
        if(!message.contains(" ")) return;

        final String[] args = message.split(" ");

        final Player player = e.getPlayer();
        final ChannelController channelController = plugin.getChannelController();

        final Channel channel = channelController.getChannelByCommand(args[0]);
        if(channel == null) return;

        channel.typing(player, message);
        e.setCancelled(true);
    }
}
