package com.github.idimabr.listener;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.controller.ChannelController;
import com.github.idimabr.model.Channel;
import com.github.idimabr.model.PlayerData;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@AllArgsConstructor
public class ChatListener implements Listener {

    private RaphaChat plugin;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        final Player player = e.getPlayer();
        final ChannelController channelController = plugin.getChannelController();
        final PlayerData data = plugin.getDataController().getData(player.getUniqueId());
        if(data == null) return;

        final Channel channel = data.getChannel();
        if(channel == null){
            player.sendMessage("§cYour current channel is invalid, change using /chat command");
            player.sendMessage("§cChannels: §7" + channelController.getChannelList());
            e.setCancelled(true);
            return;
        }

        channel.typing(player, channelController.filterMessage(e.getMessage()));
        e.setCancelled(true);
    }
}
