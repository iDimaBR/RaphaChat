package com.github.idimabr.controller;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.model.PlayerData;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;

@Getter
public class DataController {

    private RaphaChat plugin;
    private Map<UUID, PlayerData> dataMap = Maps.newHashMap();

    public PlayerData getData(UUID uuid){
        return dataMap.get(uuid);
    }

    public PlayerData getData(Player player){
        return getData(player.getUniqueId());
    }

    public void loadData(Player player) {
        plugin.getRepository().load(player.getUniqueId());
    }

    public void removeData(Player player) {
        dataMap.remove(player.getUniqueId());
    }
}
