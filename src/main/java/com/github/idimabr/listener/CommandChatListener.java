package com.github.idimabr.listener;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.controller.ChannelController;
import com.github.idimabr.model.Channel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@AllArgsConstructor
public class CommandChatListener implements Listener {

    private RaphaChat plugin;

    @EventHandler
    public void onChat(PlayerCommandPreprocessEvent e){
        if(e.isCancelled()) return;

        final String preMessage = e.getMessage();
        if(!preMessage.contains(" ")) return;

        final String[] args = preMessage.split(" ");
        final Player player = e.getPlayer();
        final ChannelController channelController = plugin.getChannelController();

        final Channel channel = channelController.getChannelByCommand(args[0].replace("/", ""));
        if(channel == null) return;

        final String message = StringUtils.join(args, " ", 1, args.length);
        channel.typing(player, channelController.filterMessage(message));
        e.setCancelled(true);
    }
}
