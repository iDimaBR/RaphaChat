package com.github.idimabr.listener;

import com.github.idimabr.RaphaChat;
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
        final PlayerData data = plugin.getDataController().getData(player.getUniqueId());
        final Channel channel = data.getChannel();
        channel.typing(player, e.getMessage());
        e.setCancelled(true);
    }
}
