package com.github.idimabr.controller;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.model.PlayerData;
import com.github.idimabr.storage.dao.StorageRepository;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class DataController {

    private final RaphaChat plugin;
    private Map<UUID, PlayerData> dataMap = Maps.newHashMap();

    public PlayerData getData(UUID uuid){
        return dataMap.get(uuid);
    }

    public PlayerData getData(Player player){
        return getData(player.getUniqueId());
    }

    public void loadData(Player player) {
        final StorageRepository repository = plugin.getRepository();
        final Set<PlayerData> load = repository.load(player.getUniqueId());
        if(load == null || load.isEmpty())
            dataMap.put(
                    player.getUniqueId(),
                    new PlayerData(
                            player.getUniqueId(),
                            plugin.getChannelController().getChannel("global")
                    )
            );
    }

    public void removeData(Player player) {
        dataMap.remove(player.getUniqueId());
    }
}
