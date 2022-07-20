package com.github.idimabr.storage.adapter;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.model.Channel;
import com.github.idimabr.model.PlayerData;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import java.util.UUID;

public class DataAdapter implements SQLResultAdapter<PlayerData> {
    @Override
    public PlayerData adaptResult(SimpleResultSet rs) {
        final RaphaChat plugin = RaphaChat.getPlugin();
        final UUID uuid = UUID.fromString(rs.get("uuid"));
        final Channel channel = plugin.getChannelController().getChannel(rs.get("channel"));

        final PlayerData playerData = new PlayerData(uuid, channel);
        plugin.getDataController().getDataMap().put(uuid, playerData);

        return playerData;
    }
}
