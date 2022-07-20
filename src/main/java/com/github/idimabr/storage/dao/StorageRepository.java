package com.github.idimabr.storage.dao;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.model.PlayerData;
import com.github.idimabr.storage.adapter.DataAdapter;
import com.google.common.collect.Maps;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class StorageRepository {

    private RaphaChat plugin;

    public void createTable() {
        this.executor().updateQuery("CREATE TABLE IF NOT EXISTS players(" +
                "`uuid` varchar(36) PRIMARY KEY NOT NULL, " +
                "`channel` varchar(50) NOT NULL" +
                ")");
    }

    public void changeChannel(Player player, String channel) {
        final UUID uuid = player.getUniqueId();
        this.executor().updateQuery("REPLACE INTO players(uuid, channel) VALUES(?,?)",
                statement -> {
                    statement.set(1, uuid.toString());
                    statement.set(2, channel);
                });
    }

    public Set<PlayerData> load(UUID uuid) {
        return this.executor().resultManyQuery(
                "SELECT * FROM players WHERE UUID = ?;",
                statement -> statement.set(1, uuid.toString()),
                DataAdapter.class
        );
    }

    private SQLExecutor executor() {
        return new SQLExecutor(plugin.getConnection());
    }

}