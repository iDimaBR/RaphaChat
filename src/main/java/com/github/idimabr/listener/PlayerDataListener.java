package com.github.idimabr.listener;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.controller.DataController;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerDataListener implements Listener {

    private RaphaChat plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        final Player player = e.getPlayer();
        final DataController dataController = plugin.getDataController();
        dataController.loadData(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        final Player player = e.getPlayer();
        final DataController dataController = plugin.getDataController();
        dataController.removeData(player);
    }
}
